package ru.rsreu.morozov.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class FormDialog extends JDialog {
	private static final ResourceBundle msg = ResourceBundle.getBundle("resources.messages");

	public static String[] show(Component parent, String title, String[] labels, String[] defaults) {
		JTextField[] fields = new JTextField[labels.length];
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
			panel.add(new JLabel(labels[i] + ":"), gbc);
			fields[i] = new JTextField(20);
			if (defaults != null && i < defaults.length && defaults[i] != null)
				fields[i].setText(defaults[i]);
			gbc.gridx = 1; gbc.weightx = 1;
			panel.add(fields[i], gbc);
		}

		int result = JOptionPane.showConfirmDialog(parent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result != JOptionPane.OK_OPTION) return null;

		String[] values = new String[labels.length];
		for (int i = 0; i < labels.length; i++)
			values[i] = fields[i].getText().trim();
		return values;
	}
}
