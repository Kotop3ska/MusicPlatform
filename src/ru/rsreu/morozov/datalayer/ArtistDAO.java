package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Artist;

import java.util.List;

public interface ArtistDAO {
	List<Artist> getAllArtists();
	void addNewArtist(String name, String country, String labelName);
	void updateArtist(String oldName, String newName, String country, String labelName);
	void deleteArtistByName(String name);
}
