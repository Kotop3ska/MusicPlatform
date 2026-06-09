package ru.rsreu.morozov.datalayer.postgresql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                PostgreDAOFactory.instance = new PostgreDAOFactory();
                factory = PostgreDAOFactory.instance;

                factory.connected();
            }
        }
        return factory;
    }

    private void connected() throws SQLException {
        Resourcer resourcer = ProjectResourcer.getInstance();

        String url = resourcer.getString("connection.url");
        String user = resourcer.getString("connection.user");
        String password = resourcer.getString("connection.password");

        this.connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connection successfully!");
    }
}

