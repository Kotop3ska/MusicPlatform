package ru.rsreu.morozov;

import ru.rsreu.morozov.datalayer.DAOFactory;
import ru.rsreu.morozov.datalayer.DBType;

public class ClientRunner {
	public static void main(String[] args) {

		DAOFactory factory = DAOFactory.getInstance(DBType.POSTGRESQL);

	}
}
