package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Track;
import java.util.List;

public interface TrackDAO {
	List<Track> getAllTracks();
	List<Track> searchTracks(String query);
	void addNewTrack(String title, String albumTitle, String genreName, int durationSeconds);
	void updateTrack(long id, String title, String albumTitle, String genreName, int durationSeconds);
	void deleteTrack(long id);
}
