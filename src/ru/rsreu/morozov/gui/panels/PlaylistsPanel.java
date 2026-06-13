package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlaylistsPanel extends TablePanel {
	private final DAOFactory factory;

	public PlaylistsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.user"),
				msg.getString("col.date"), msg.getString("col.track_count")});
		this.factory = factory;

		JButton detail = new JButton(msg.getString("btn.tracks"));
		detail.addActionListener(this::onDetail);
		addButtons(detail);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onDetail(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_playlist")); return; }
		long id = (long) model.getValueAt(row, 0);
		String name = (String) model.getValueAt(row, 1);
		var tracks = factory.getPlaylistDAO().getPlaylistDetail(id);
		String[] cols = {msg.getString("col.track_title"), msg.getString("col.artist"), msg.getString("col.seconds")};
		Object[][] data = new Object[tracks.size()][3];
		for (int i = 0; i < tracks.size(); i++) {
			var t = tracks.get(i);
			data[i] = new Object[]{t.title(), t.artistName(), t.duration()};
		}
		JTable table = new JTable(data, cols);
		table.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		table.setRowHeight(28);
		JOptionPane.showMessageDialog(this, new JScrollPane(table),
				String.format(msg.getString("dialog.playlist_tracks"), name), JOptionPane.PLAIN_MESSAGE);
	}

	private void refresh() {
		model.setRowCount(0);
		for (var p : factory.getPlaylistDAO().getAllPlaylists())
			model.addRow(new Object[]{p.id(), p.playlistName(), p.username(), p.createdAt(), p.trackCount()});
	}
}
