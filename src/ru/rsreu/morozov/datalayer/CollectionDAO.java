package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Collection;
import ru.rsreu.morozov.datalayer.data.Track;

import java.util.List;

public interface CollectionDAO {
	List<Collection> getAllCollections();
	void addNewCollection(String title, String description);
	void updateCollection(long id, String title, String description);
	void deleteCollection(long id);
	List<Track> getCollectionDetail(long collectionId);
	void addTrackToCollection(long collectionId, String trackTitle);
	void removeTrackFromCollection(long collectionId, String trackTitle);
}
