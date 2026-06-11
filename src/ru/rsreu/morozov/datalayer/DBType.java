package ru.rsreu.morozov.datalayer;

import java.sql.SQLException;

import ru.rsreu.morozov.datalayer.postgresql.PostgreDAOFactory;

public enum DBType {

	POSTGRESQL {
		@Override
		public DAOFactory getDAOFactory() {
			DAOFactory postgreDAOFactory = null;
			try {
				postgreDAOFactory = PostgreDAOFactory.getInstance();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			return postgreDAOFactory;
		}
	};

	public static DBType getTypeByName(String dbType) {
		try {
			return DBType.valueOf(dbType.toUpperCase());
		} catch (Exception e) {
			throw new DBTypeException();
		}
	}

	public abstract DAOFactory getDAOFactory();
}
