package ru.rsreu.morozov.datalayer;

public abstract class DAOFactory {

	public static DAOFactory getInstance(DBType dbType) {
		DAOFactory result = dbType.getDAOFactory();
		return result;
	}

	abstract public SubscriptionDAO getSubscriptionDAO();

	abstract public ArtistDAO getArtistDAO();

	abstract public LabelDAO getLabelDAO();

	abstract public DashboardDAO getDashboardDAO();

	abstract public AlbumDAO getAlbumDAO();

	abstract public TrackDAO getTrackDAO();

	abstract public GenreDAO getGenreDAO();

	abstract public UserDAO getUserDAO();

	abstract public ReviewDAO getReviewDAO();

	abstract public PlaylistDAO getPlaylistDAO();

	abstract public CollectionDAO getCollectionDAO();

}
