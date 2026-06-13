package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Artist;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ArtistsPanel extends TablePanel {
	private final DAOFactory factory;

	public ArtistsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.country"), msg.getString("col.label")});
		this.factory = factory;

		JButton add = new JButton(msg.getString("btn.add"));
		JButton edit = new JButton(msg.getString("btn.edit"));
		JButton del = new JButton(msg.getString("btn.delete"));
		JButton tracks = new JButton(msg.getString("btn.tracks"));
		add.addActionListener(this::onAdd);
		edit.addActionListener(this::onEdit);
		del.addActionListener(this::onDelete);
		tracks.addActionListener(this::onTracks);
		addButtons(add, edit, del, tracks);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onAdd(ActionEvent e) {
		String[] r = FormDialog.show(this, msg.getString("dialog.add_artist"),
				new String[]{msg.getString("field.name"), msg.getString("field.country"), msg.getString("field.label")}, null);
		if (r != null) {
			try { factory.getArtistDAO().addNewArtist(r[0], r[1], r[2]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Artist a = factory.getArtistDAO().getAllArtists().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_artist"),
				new String[]{msg.getString("field.name"), msg.getString("field.country"), msg.getString("field.label")},
				new String[]{a.name(), a.country(), a.labelName()});
		if (r != null) {
			try { factory.getArtistDAO().updateArtist(a.id(), r[0], r[1], r[2]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try {
				Artist a = factory.getArtistDAO().getAllArtists().get(row);
				factory.getArtistDAO().deleteArtist(a.id());
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onTracks(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_artist")); return; }
		Artist a = factory.getArtistDAO().getAllArtists().get(row);
		var tracks = factory.getArtistDAO().getArtistTracks(a.id());
		String[] cols = {msg.getString("col.track_title"), msg.getString("col.album_title"), msg.getString("col.seconds"), msg.getString("col.plays"), msg.getString("col.genre")};
		Object[][] data = new Object[tracks.size()][5];
		for (int i = 0; i < tracks.size(); i++) {
			var t = tracks.get(i);
			data[i] = new Object[]{t.title(), t.albumTitle(), t.duration(), t.playCount(), t.genreName()};
		}
		JTable table = new JTable(data, cols);
		table.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		table.setRowHeight(28);
		JOptionPane.showMessageDialog(this, new JScrollPane(table),
				String.format(msg.getString("dialog.artist_tracks"), a.name()), JOptionPane.PLAIN_MESSAGE);
	}

	private void refresh() {
		model.setRowCount(0);
		for (Artist a : factory.getArtistDAO().getAllArtists())
			model.addRow(new Object[]{a.id(), a.name(), a.country(), a.labelName()});
	}
}
