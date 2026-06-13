package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UsersPanel extends TablePanel {
	private final DAOFactory factory;

	public UsersPanel(DAOFactory factory) {
		super(new String[]{msg.getString("col.id"), msg.getString("col.name"), msg.getString("col.email"),
				msg.getString("col.subscription"), msg.getString("col.register_date")});
		this.factory = factory;

		JButton detail = new JButton(msg.getString("btn.details"));
		detail.addActionListener(this::onDetail);
		addButtons(detail);
		setRefreshAction(this::refresh);
		refresh();
	}

	private void onDetail(ActionEvent e) {
		int row = table.getSelectedRow();
		if (row < 0) { JOptionPane.showMessageDialog(this, msg.getString("error.select_user")); return; }
		var user = factory.getUserDAO().getAllUsers().get(row);
		JOptionPane.showMessageDialog(this,
				String.format(msg.getString("user.username"), user.username())
						+ "\n" + String.format(msg.getString("user.email"), user.email())
						+ "\n" + String.format(msg.getString("user.subscription"), user.subscriptionName())
						+ "\n" + String.format(msg.getString("user.registered"), user.createdAt()),
				msg.getString("dialog.user_details"), JOptionPane.INFORMATION_MESSAGE);
	}

	private void refresh() {
		model.setRowCount(0);
		for (var u : factory.getUserDAO().getAllUsers())
			model.addRow(new Object[]{u.id(), u.username(), u.email(), u.subscriptionName(), u.createdAt()});
	}
}
