package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TracksPanel extends TablePanel {
	private final DAOFactory factory;
	private final JTextField searchField = new JTextField(20);

	public TracksPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.track_title"), msg.getString("col.artist"),
				msg.getString("col.album_title"), msg.getString("col.seconds"), msg.getString("col.plays"), msg.getString("col.genre")});
		this.factory = factory;

		JButton add = new JButton(msg.getString("btn.add"));
		JButton edit = new JButton(msg.getString("btn.edit"));
		JButton del = new JButton(msg.getString("btn.delete"));
		JButton searchBtn = new JButton(msg.getString("btn.search"));
		add.addActionListener(this::onAdd);
		edit.addActionListener(this::onEdit);
		del.addActionListener(this::onDelete);
		searchBtn.addActionListener(this::onSearch);

		JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
		top.setBackground(Color.WHITE);
		top.add(new JLabel(msg.getString("field.search")));
		top.add(searchField);
		top.add(searchBtn);
		add(top, BorderLayout.NORTH);
		addButtons(add, edit, del);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onSearch(ActionEvent e) {
		String query = searchField.getText().trim();
		if (query.isEmpty()) { refresh(); return; }
		model.setRowCount(0);
		for (Track t : factory.getTrackDAO().searchTracks(query))
			model.addRow(new Object[]{t.id(), t.title(), t.artistName(), t.albumTitle(), t.duration(), t.playCount(), t.genreName()});
	}

	private void onAdd(ActionEvent e) {
		String[] r = FormDialog.show(this, msg.getString("dialog.add_track"),
				new String[]{msg.getString("field.name"), msg.getString("field.album"), msg.getString("field.genre"), msg.getString("field.seconds")}, null);
		if (r != null) {
			try { factory.getTrackDAO().addNewTrack(r[0], r[1], r[2], Integer.parseInt(r[3])); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Track t = getCurrentTrack(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_track"),
				new String[]{msg.getString("field.name"), msg.getString("field.album"), msg.getString("field.genre"), msg.getString("field.seconds")},
				new String[]{t.title(), t.albumTitle(), t.genreName(), String.valueOf(t.duration())});
		if (r != null) {
			try { factory.getTrackDAO().updateTrack(t.id(), r[0], r[1], r[2], Integer.parseInt(r[3])); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try { factory.getTrackDAO().deleteTrack(getCurrentTrack(row).id()); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private Track getCurrentTrack(int row) {
		long id = (long) model.getValueAt(row, 0);
		String title = (String) model.getValueAt(row, 1);
		String artist = (String) model.getValueAt(row, 2);
		String album = (String) model.getValueAt(row, 3);
		int dur = (int) model.getValueAt(row, 4);
		int plays = (int) model.getValueAt(row, 5);
		String genre = (String) model.getValueAt(row, 6);
		return new Track(id, title, artist, album, dur, plays, genre);
	}

	private void refresh() {
		model.setRowCount(0);
		for (Track t : factory.getTrackDAO().getAllTracks())
			model.addRow(new Object[]{t.id(), t.title(), t.artistName(), t.albumTitle(), t.duration(), t.playCount(), t.genreName()});
	}
}
