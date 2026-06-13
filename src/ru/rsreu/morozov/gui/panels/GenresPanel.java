package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Genre;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GenresPanel extends TablePanel {
	private final DAOFactory factory;

	public GenresPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name")});
		this.factory = factory;

		JButton add = new JButton(msg.getString("btn.add"));
		JButton edit = new JButton(msg.getString("btn.edit"));
		JButton del = new JButton(msg.getString("btn.delete"));
		add.addActionListener(this::onAdd);
		edit.addActionListener(this::onEdit);
		del.addActionListener(this::onDelete);
		addButtons(add, edit, del);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onAdd(ActionEvent e) {
		String[] r = FormDialog.show(this, msg.getString("dialog.add_genre"),
				new String[]{msg.getString("field.name")}, null);
		if (r != null) {
			try { factory.getGenreDAO().addNewGenre(r[0]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Genre g = factory.getGenreDAO().getAllGenres().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_genre"),
				new String[]{msg.getString("field.name")}, new String[]{g.name()});
		if (r != null) {
			try { factory.getGenreDAO().updateGenre(g.id(), r[0]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try { factory.getGenreDAO().deleteGenre(factory.getGenreDAO().getAllGenres().get(row).id()); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void refresh() {
		model.setRowCount(0);
		for (Genre g : factory.getGenreDAO().getAllGenres())
			model.addRow(new Object[]{g.id(), g.name()});
	}
}
