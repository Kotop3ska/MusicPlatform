package ru.rsreu.morozov.gui.panels;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.data.DashboardStats;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

	public DashboardPanel(DAOFactory factory) {
		setLayout(new GridLayout(2, 3, 15, 15));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setBackground(Color.WHITE);

		try {
			DashboardStats s = factory.getDashboardDAO().getStats();
			add(card("Пользователей", String.valueOf(s.userCount())));
			add(card("Треков", String.valueOf(s.trackCount())));
			add(card("Альбомов", String.valueOf(s.albumCount())));
			add(card("Подписок", String.valueOf(s.subscriptionCount())));
			add(card("Артистов", String.valueOf(s.artistCount())));
			add(card("Жанров", String.valueOf(s.genreCount())));
		} catch (Exception e) {
			add(new JLabel("Ошибка: " + e.getMessage()));
		}
	}

	private JPanel card(String label, String value) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(new Color(245, 247, 250));
		p.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(220, 220, 220)),
				BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		JLabel v = new JLabel(value);
		v.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
		v.setForeground(new Color(50, 100, 200));
		JLabel l = new JLabel(label);
		l.setForeground(new Color(100, 100, 100));
		p.add(v, BorderLayout.NORTH);
		p.add(l, BorderLayout.CENTER);
		return p;
	}
}
