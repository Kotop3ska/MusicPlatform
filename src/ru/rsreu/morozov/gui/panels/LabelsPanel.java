package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Label;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LabelsPanel extends TablePanel {
	private final DAOFactory factory;

	public LabelsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), "Год основания"});
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
		String[] r = FormDialog.show(this, msg.getString("dialog.add_label"),
				new String[]{msg.getString("field.name"), "Год основания"}, null);
		if (r != null) {
			try { factory.getLabelDAO().addNewLabel(r[0], Integer.parseInt(r[1])); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Label l = factory.getLabelDAO().getAllLabels().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_label"),
				new String[]{msg.getString("field.name"), "Год основания"},
				new String[]{l.name(), String.valueOf(l.foundationYear())});
		if (r != null) {
			try { factory.getLabelDAO().updateLabel(l.id(), r[0], Integer.parseInt(r[1])); refresh(); }
			catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try {
				Label l = factory.getLabelDAO().getAllLabels().get(row);
				factory.getLabelDAO().deleteLabel(l.id());
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void refresh() {
		model.setRowCount(0);
		for (Label l : factory.getLabelDAO().getAllLabels())
			model.addRow(new Object[]{l.id(), l.name(), l.foundationYear()});
	}
}
