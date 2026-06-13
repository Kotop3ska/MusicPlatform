package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Collection;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CollectionsPanel extends TablePanel {
	private final DAOFactory factory;

	public CollectionsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.description"), msg.getString("col.track_count")});
		this.factory = factory;

		JButton add = new JButton(msg.getString("btn.add"));
		JButton edit = new JButton(msg.getString("btn.edit"));
		JButton del = new JButton(msg.getString("btn.delete"));
		JButton tracks = new JButton(msg.getString("btn.tracks"));
		JButton addTrack = new JButton("+ Трек");
		JButton removeTrack = new JButton("- Трек");
		add.addActionListener(this::onAdd);
		edit.addActionListener(this::onEdit);
		del.addActionListener(this::onDelete);
		tracks.addActionListener(this::onTracks);
		addTrack.addActionListener(this::onAddTrack);
		removeTrack.addActionListener(this::onRemoveTrack);
		addButtons(add, edit, del, tracks, addTrack, removeTrack);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onAdd(ActionEvent e) {
		String[] r = FormDialog.show(this, msg.getString("dialog.add_collection"),
				new String[]{msg.getString("field.name"), msg.getString("field.description")}, null);
		if (r != null) {
			try { factory.getCollectionDAO().addNewCollection(r[0], r[1]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Collection c = factory.getCollectionDAO().getAllCollections().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_collection"),
				new String[]{msg.getString("field.name"), msg.getString("field.description")},
				new String[]{c.title(), c.description()});
		if (r != null) {
			try { factory.getCollectionDAO().updateCollection(c.id(), r[0], r[1]); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try { factory.getCollectionDAO().deleteCollection(factory.getCollectionDAO().getAllCollections().get(row).id()); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onTracks(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_collection")); return; }
		Collection c = factory.getCollectionDAO().getAllCollections().get(row);
		var tracks = factory.getCollectionDAO().getCollectionDetail(c.id());
		String[] cols = {msg.getString("col.track_title"), msg.getString("col.artist"), msg.getString("col.album_title"), msg.getString("col.genre"), msg.getString("col.seconds")};
		Object[][] data = new Object[tracks.size()][5];
		for (int i = 0; i < tracks.size(); i++) {
			var t = tracks.get(i);
			data[i] = new Object[]{t.title(), t.artistName(), t.albumTitle(), t.genreName(), t.duration()};
		}
		JTable table = new JTable(data, cols);
		table.setFont(new java.awt.Font(java.awt.Font.SANS_SERIF, java.awt.Font.PLAIN, 13));
		table.setRowHeight(28);
		JOptionPane.showMessageDialog(this, new JScrollPane(table),
				String.format(msg.getString("dialog.collection_tracks"), c.title()), JOptionPane.PLAIN_MESSAGE);
	}

	private void onAddTrack(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_collection")); return; }
		Collection c = factory.getCollectionDAO().getAllCollections().get(row);
		String trackTitle = JOptionPane.showInputDialog(this, msg.getString("field.track_title") + ":");
		if (trackTitle != null && !trackTitle.trim().isEmpty()) {
			try {
				factory.getCollectionDAO().addTrackToCollection(c.id(), trackTitle.trim());
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onRemoveTrack(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_collection")); return; }
		Collection c = factory.getCollectionDAO().getAllCollections().get(row);
		var tracks = factory.getCollectionDAO().getCollectionDetail(c.id());
		if (tracks.isEmpty()) { JOptionPane.showMessageDialog(this, "Коллекция пуста"); return; }

		String[] trackNames = tracks.stream().map(t -> t.title()).toArray(String[]::new);
		String selected = (String) JOptionPane.showInputDialog(this,
				msg.getString("field.track_title") + ":",
				msg.getString("btn.delete") + " трек",
				JOptionPane.PLAIN_MESSAGE, null, trackNames, trackNames[0]);
		if (selected != null) {
			try {
				factory.getCollectionDAO().removeTrackFromCollection(c.id(), selected);
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void refresh() {
		model.setRowCount(0);
		for (Collection c : factory.getCollectionDAO().getAllCollections())
			model.addRow(new Object[]{c.id(), c.title(), c.description(), c.trackCount()});
	}
}
