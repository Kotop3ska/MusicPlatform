package ru.rsreu.morozov.gui.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ResourceBundle;

public class TablePanel extends JPanel {

	protected final DefaultTableModel model;
	protected final JTable table;
	protected static final ResourceBundle msg = ResourceBundle.getBundle("resources.messages");
	private Runnable refreshAction;

	public TablePanel(String[] columns) {
		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		setBackground(Color.WHITE);

		model = new DefaultTableModel(columns, 0) {
			public boolean isCellEditable(int r, int c) { return false; }
		};
		table = new JTable(model);
		table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		table.setRowHeight(30);
		table.setSelectionBackground(new Color(200, 220, 255));
		table.setSelectionForeground(Color.BLACK);
		table.setGridColor(new Color(230, 230, 230));
		table.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		table.getTableHeader().setBackground(new Color(245, 245, 245));

		add(new JScrollPane(table), BorderLayout.CENTER);
	}

	public void setRefreshAction(Runnable action) {
		this.refreshAction = action;
	}

	protected void addButtons(JButton... buttons) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panel.setBackground(Color.WHITE);
		for (JButton b : buttons) {
			b.setFocusPainted(false);
			panel.add(b);
		}
		JButton refresh = new JButton(msg.getString("btn.refresh"));
		refresh.setFocusPainted(false);
		refresh.addActionListener(e -> { if (refreshAction != null) refreshAction.run(); });
		panel.add(refresh);
		add(panel, BorderLayout.SOUTH);
	}
}
