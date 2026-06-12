package ru.rsreu.morozov.datalayer;

public abstract class DAOFactory {

	public static DAOFactory getInstance(DBType dbType) {
		DAOFactory result = dbType.getDAOFactory();
		return result;
	}

	abstract public SubscriptionDAO getSubscriptionDAO();

	abstract public ArtistDAO getArtistDAO();

	abstract public LabelDAO getLabelDAO();

}
