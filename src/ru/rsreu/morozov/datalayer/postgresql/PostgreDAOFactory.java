package ru.rsreu.morozov.datalayer.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ru.rsreu.morozov.datalayer.ArtistDAO;
import ru.rsreu.morozov.datalayer.LabelDAO;
import ru.rsreu.morozov.datalayer.SubscriptionDAO;
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

	private void connected() throws SQLException {
		Resourcer resourcer = ProjectResourcer.getInstance();
		resourcer.setPropertyName("resources.connection");
		String url = resourcer.getString("connection.url");
		String user = resourcer.getString("connection.user");
		String password = resourcer.getString("connection.password");

		this.connection = DriverManager.getConnection(url, user, password);
		System.out.println("Connection successfully!");
	}
}

