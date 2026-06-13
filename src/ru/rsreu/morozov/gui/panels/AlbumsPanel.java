package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Album;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class AlbumsPanel extends TablePanel {
	private final DAOFactory factory;

	public AlbumsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.artist"),
				msg.getString("col.date"), msg.getString("col.type"), msg.getString("col.rating"), msg.getString("col.reviews_count")});
		this.factory = factory;

		JButton add = new JButton(msg.getString("btn.add"));
		JButton edit = new JButton(msg.getString("btn.edit"));
		JButton del = new JButton(msg.getString("btn.delete"));
		JButton tracks = new JButton(msg.getString("btn.tracks"));
		JButton reviews = new JButton(msg.getString("btn.reviews"));
		add.addActionListener(this::onAdd);
		edit.addActionListener(this::onEdit);
		del.addActionListener(this::onDelete);
		tracks.addActionListener(this::onTracks);
		reviews.addActionListener(this::onReviews);
		addButtons(add, edit, del, tracks, reviews);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onAdd(ActionEvent e) {
		String[] r = FormDialog.show(this, msg.getString("dialog.add_album"),
				new String[]{msg.getString("field.name"), msg.getString("field.artist"), msg.getString("field.date"), msg.getString("field.type")}, null);
		if (r != null) {
			try { factory.getAlbumDAO().addNewAlbum(r[0], r[1], Date.valueOf(LocalDate.parse(r[2])), r[3]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Album a = factory.getAlbumDAO().getAllAlbums().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_album"),
				new String[]{msg.getString("field.name"), msg.getString("field.artist"), msg.getString("field.date"), msg.getString("field.type")},
				new String[]{a.title(), a.artistName(), a.releaseDate() != null ? a.releaseDate().toString() : "", a.releaseType()});
		if (r != null) {
			try { factory.getAlbumDAO().updateAlbum(a.id(), r[0], r[1], Date.valueOf(LocalDate.parse(r[2])), r[3]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try {
				Album a = factory.getAlbumDAO().getAllAlbums().get(row);
				factory.getAlbumDAO().deleteAlbum(a.id());
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onTracks(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_album")); return; }
		Album a = factory.getAlbumDAO().getAllAlbums().get(row);
		var tracks = factory.getAlbumDAO().getAlbumTracks(a.id());
		String[] cols = {msg.getString("col.track_title"), msg.getString("col.seconds"), msg.getString("col.plays"), msg.getString("col.genre")};
		Object[][] data = new Object[tracks.size()][4];
		for (int i = 0; i < tracks.size(); i++) {
			var t = tracks.get(i);
			data[i] = new Object[]{t.title(), t.duration(), t.playCount(), t.genreName()};
		}
		JTable table = new JTable(data, cols);
		table.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		table.setRowHeight(28);
		JOptionPane.showMessageDialog(this, new JScrollPane(table),
				String.format(msg.getString("dialog.tracks_of"), a.title()), JOptionPane.PLAIN_MESSAGE);
	}

	private void onReviews(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_album")); return; }
		Album a = factory.getAlbumDAO().getAllAlbums().get(row);
		var reviews = factory.getAlbumDAO().getAlbumReviews(a.id());
		String[] cols = {msg.getString("col.user"), msg.getString("col.rating"), msg.getString("col.date")};
		Object[][] data = new Object[reviews.size()][3];
		for (int i = 0; i < reviews.size(); i++) {
			var r = reviews.get(i);
			data[i] = new Object[]{r.username(), r.rating(), r.reviewDate()};
		}
		JTable table = new JTable(data, cols);
		table.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		table.setRowHeight(28);
		JOptionPane.showMessageDialog(this, new JScrollPane(table),
				String.format(msg.getString("dialog.reviews_of"), a.title()), JOptionPane.PLAIN_MESSAGE);
	}

	private void refresh() {
		model.setRowCount(0);
		for (Album a : factory.getAlbumDAO().getAllAlbums())
			model.addRow(new Object[]{a.id(), a.title(), a.artistName(), a.releaseDate(), a.releaseType(),
					a.avgRating(), a.reviewCount()});
	}
}
