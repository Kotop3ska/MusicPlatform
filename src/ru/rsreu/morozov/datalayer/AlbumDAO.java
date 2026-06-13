package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Album;
import ru.rsreu.morozov.datalayer.data.Review;
import ru.rsreu.morozov.datalayer.data.Track;

import java.sql.Date;
import java.util.List;

public interface AlbumDAO {
	List<Album> getAllAlbums();
	void addNewAlbum(String title, String artistName, Date releaseDate, String releaseType);
	void updateAlbum(long id, String title, String artistName, Date releaseDate, String releaseType);
	void deleteAlbum(long id);
	List<Track> getAlbumTracks(long albumId);
	List<Review> getAlbumReviews(long albumId);
}
