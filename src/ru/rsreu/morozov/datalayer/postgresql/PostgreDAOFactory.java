package ru.rsreu.morozov.datalayer.postgresql;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import ru.rsreu.morozov.datalayer.ArtistDAO;
import ru.rsreu.morozov.datalayer.LabelDAO;
import ru.rsreu.morozov.datalayer.SubscriptionDAO;
import ru.rsreu.morozov.datalayer.DashboardDAO;
import ru.rsreu.morozov.datalayer.AlbumDAO;
import ru.rsreu.morozov.datalayer.TrackDAO;
import ru.rsreu.morozov.datalayer.GenreDAO;
import ru.rsreu.morozov.datalayer.UserDAO;
import ru.rsreu.morozov.datalayer.ReviewDAO;
import ru.rsreu.morozov.datalayer.PlaylistDAO;
import ru.rsreu.morozov.datalayer.CollectionDAO;
import ru.rsreu.morozov.resourcer.*;
import ru.rsreu.morozov.datalayer.DAOFactory;

public class PostgreDAOFactory extends DAOFactory {
	private static volatile PostgreDAOFactory instance;

	private Connection connection;

	private PostgreDAOFactory() {
	}

	public static PostgreDAOFactory getInstance() throws ClassNotFoundException, SQLException {
		PostgreDAOFactory factory = PostgreDAOFactory.instance;

		if (PostgreDAOFactory.instance == null) {
			synchronized (PostgreDAOFactory.class) {
				if (PostgreDAOFactory.instance == null) {
					PostgreDAOFactory.instance = new PostgreDAOFactory();
					factory = PostgreDAOFactory.instance;

					factory.connected();
				}
			}
		}
		return factory;
	}

	@Override
	public SubscriptionDAO getSubscriptionDAO() {
		return new PostgreSubscriptionDAO(this.connection);
	}

	@Override
	public ArtistDAO getArtistDAO() {
		return new PostgreArtistDAO(this.connection);
	}

	@Override
	public LabelDAO getLabelDAO() {
		return new PostgreLabelDAO(this.connection);
	}

	@Override
	public DashboardDAO getDashboardDAO() {
		return new PostgreDashboardDAO(this.connection);
	}

	@Override
	public AlbumDAO getAlbumDAO() {
		return new PostgreAlbumDAO(this.connection);
	}

	@Override
	public TrackDAO getTrackDAO() {
		return new PostgreTrackDAO(this.connection);
	}

	@Override
	public GenreDAO getGenreDAO() {
		return new PostgreGenreDAO(this.connection);
	}

	@Override
	public UserDAO getUserDAO() {
		return new PostgreUserDAO(this.connection);
	}

	@Override
	public ReviewDAO getReviewDAO() {
		return new PostgreReviewDAO(this.connection);
	}

	@Override
	public PlaylistDAO getPlaylistDAO() {
		return new PostgrePlaylistDAO(this.connection);
	}

	@Override
	public CollectionDAO getCollectionDAO() {
		return new PostgreCollectionDAO(this.connection);
	}

	private void connected() throws SQLException {
		Properties props = loadConnectionProperties();
		String url = props.getProperty("connection.url");
		String user = props.getProperty("connection.user");
		String password = props.getProperty("connection.password");

		this.connection = DriverManager.getConnection(url, user, password);
		System.out.println("Connection successfully!");
	}

	private Properties loadConnectionProperties() {
		Path configPath = Paths.get("config", "connection.properties");
		if (Files.exists(configPath)) {
			try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
				Properties props = new Properties();
				props.load(fis);
				System.out.println("Loaded connection config from: " + configPath.toAbsolutePath());
				return props;
			} catch (IOException e) {
				System.err.println("Failed to read " + configPath + ", falling back to classpath: " + e.getMessage());
			}
		}

		Resourcer resourcer = ProjectResourcer.getInstance("resources.connection");
		Properties props = new Properties();
		props.setProperty("connection.url", resourcer.getString("connection.url"));
		props.setProperty("connection.user", resourcer.getString("connection.user"));
		props.setProperty("connection.password", resourcer.getString("connection.password"));
		System.out.println("Loaded connection config from classpath");
		return props;
	}
}

