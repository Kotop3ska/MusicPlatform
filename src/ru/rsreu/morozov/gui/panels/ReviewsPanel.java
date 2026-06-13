package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ReviewsPanel extends TablePanel {
	private final DAOFactory factory;

	public ReviewsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.user"), msg.getString("col.album_title"),
				msg.getString("col.rating"), msg.getString("col.date")});
		this.factory = factory;

		JButton del = new JButton(msg.getString("btn.delete"));
		del.addActionListener(this::onDelete);
		addButtons(del);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_review")); return; }
		long id = (long) model.getValueAt(row, 0);
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete_review")) == JOptionPane.YES_OPTION) {
			try { factory.getReviewDAO().deleteReview(id); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void refresh() {
		model.setRowCount(0);
		for (var r : factory.getReviewDAO().getAllReviews())
			model.addRow(new Object[]{r.id(), r.username(), r.albumTitle(), r.rating(), r.reviewDate()});
	}
}
