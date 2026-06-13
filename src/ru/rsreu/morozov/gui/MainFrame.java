package ru.rsreu.morozov.gui;

import ru.rsreu.morozov.datalayer.*;
import ru.rsreu.morozov.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class MainFrame extends JFrame {
	private final CardLayout cards = new CardLayout();
	private final JPanel content = new JPanel(cards);
	private final ResourceBundle msg = ResourceBundle.getBundle("resources.messages");
	private final DAOFactory factory;

	public MainFrame() {
		super("Music Platform — Админ-панель");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1100, 700);
		setLocationRelativeTo(null);

		DAOFactory f = null;
		try {
			f = DAOFactory.getInstance(DBType.POSTGRESQL);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, String.format(msg.getString("error.connection"), e.getMessage()));
			System.exit(1);
		}
		factory = f;

		getContentPane().add(createSidebar(), BorderLayout.WEST);
		content.add(new DashboardPanel(factory), "dashboard");
		content.add(new SubscriptionsPanel(factory), "subscriptions");
		content.add(new UsersPanel(factory), "users");
		content.add(new ArtistsPanel(factory), "artists");
		content.add(new LabelsPanel(factory), "labels");
		content.add(new AlbumsPanel(factory), "albums");
		content.add(new TracksPanel(factory), "tracks");
		content.add(new GenresPanel(factory), "genres");
		content.add(new ReviewsPanel(factory), "reviews");
		content.add(new PlaylistsPanel(factory), "playlists");
		content.add(new CollectionsPanel(factory), "collections");
		getContentPane().add(content, BorderLayout.CENTER);

		cards.show(content, "dashboard");
	}

	private JPanel createSidebar() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(45, 52, 70));
		panel.setPreferredSize(new Dimension(210, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JLabel title = new JLabel("  " + msg.getString("window.app_title"));
		title.setForeground(Color.WHITE);
		title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
		title.setBorder(BorderFactory.createEmptyBorder(5, 10, 20, 0));
		panel.add(title);

		String[][] items = {
				{msg.getString("screen.dashboard"), "dashboard"},
				{msg.getString("screen.subscriptions"), "subscriptions"},
				{msg.getString("screen.users"), "users"},
				{msg.getString("screen.artists"), "artists"},
				{msg.getString("screen.labels"), "labels"},
				{msg.getString("screen.albums"), "albums"},
				{msg.getString("screen.tracks"), "tracks"},
				{msg.getString("screen.genres"), "genres"},
				{msg.getString("screen.reviews"), "reviews"},
				{msg.getString("screen.playlists"), "playlists"},
				{msg.getString("screen.collections"), "collections"}
		};

		for (String[] item : items) {
			JButton btn = new JButton(item[0]);
			btn.setMaximumSize(new Dimension(210, 40));
			btn.setBackground(new Color(45, 52, 70));
			btn.setForeground(new Color(200, 210, 230));
			btn.setBorderPainted(false);
			btn.setFocusPainted(false);
			btn.setHorizontalAlignment(SwingConstants.LEFT);
			btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 0));
			btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			btn.addActionListener(e -> cards.show(content, item[1]));
			btn.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(65, 72, 90)); }
				public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(new Color(45, 52, 70)); }
			});
			panel.add(btn);
		}
		return panel;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
	}
}
