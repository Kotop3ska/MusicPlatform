package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.DashboardStats;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
	private final DAOFactory factory;
	private final JLabel[] values = new JLabel[6];

	public DashboardPanel(DAOFactory factory) {
		this.factory = factory;
		setLayout(new GridLayout(2, 3, 15, 15));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setBackground(Color.WHITE);

		String[] labels = {"Пользователей", "Треков", "Альбомов", "Подписок", "Артистов", "Жанров"};
		for (int i = 0; i < 6; i++) {
			values[i] = new JLabel("0");
			values[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
			values[i].setForeground(new Color(50, 100, 200));
			JLabel l = new JLabel(labels[i]);
			l.setForeground(new Color(100, 100, 100));
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(new Color(245, 247, 250));
			p.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(new Color(220, 220, 220)),
					BorderFactory.createEmptyBorder(15, 15, 15, 15)));
			p.add(values[i], BorderLayout.NORTH);
			p.add(l, BorderLayout.CENTER);
			add(p);
		}
		refresh();
	}

	public void refresh() {
		try {
			DashboardStats s = factory.getDashboardDAO().getStats();
			values[0].setText(String.valueOf(s.userCount()));
			values[1].setText(String.valueOf(s.trackCount()));
			values[2].setText(String.valueOf(s.albumCount()));
			values[3].setText(String.valueOf(s.subscriptionCount()));
			values[4].setText(String.valueOf(s.artistCount()));
			values[5].setText(String.valueOf(s.genreCount()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
