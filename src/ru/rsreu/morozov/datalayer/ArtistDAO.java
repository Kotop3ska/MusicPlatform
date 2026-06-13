package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Artist;
import ru.rsreu.morozov.datalayer.data.Track;

import java.util.List;

public interface ArtistDAO {
	List<Artist> getAllArtists();
	void addNewArtist(String name, String country, String labelName);
	void updateArtist(long id, String name, String country, String labelName);
	void deleteArtist(long id);
	List<Track> getArtistTracks(long artistId);
}
