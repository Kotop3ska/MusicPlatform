package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.Subscription;
import ru.rsreu.morozov.gui.FormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SubscriptionsPanel extends TablePanel {
	private final DAOFactory factory;

	public SubscriptionsPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.price"), msg.getString("col.days")});
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
		String[] r = FormDialog.show(this, msg.getString("dialog.add_subscription"),
				new String[]{msg.getString("field.name"), msg.getString("field.price"), msg.getString("field.days")}, null);
		if (r != null) {
			try {
				factory.getSubscriptionDAO().addNewSubscription(r[0], Double.parseDouble(r[1]), Integer.parseInt(r[2]));
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onEdit(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_row")); return; }
		Subscription s = factory.getSubscriptionDAO().getAllSubscriptions().get(row);
		String[] r = FormDialog.show(this, msg.getString("dialog.edit_subscription"),
				new String[]{msg.getString("field.name"), msg.getString("field.price"), msg.getString("field.days")},
				new String[]{s.name(), String.valueOf(s.price()), String.valueOf(s.durationDays())});
		if (r != null) {
			try {
				factory.getSubscriptionDAO().updateSubscription(s.id(), r[0], Double.parseDouble(r[1]), Integer.parseInt(r[2]));
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void onDelete(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) return;
		if (JOptionPane.showConfirmDialog(this, msg.getString("confirm.delete")) == JOptionPane.YES_OPTION) {
			try {
				Subscription s = factory.getSubscriptionDAO().getAllSubscriptions().get(row);
				factory.getSubscriptionDAO().deleteSubscription(s.id());
				refresh();
			} catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
		}
	}

	private void refresh() {
		model.setRowCount(0);
		for (Subscription s : factory.getSubscriptionDAO().getAllSubscriptions())
			model.addRow(new Object[]{s.id(), s.name(), s.price(), s.durationDays()});
	}
}
