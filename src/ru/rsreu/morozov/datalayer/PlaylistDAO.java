package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Playlist;
import ru.rsreu.morozov.datalayer.data.Track;

import java.util.List;

public interface PlaylistDAO {
	List<Playlist> getAllPlaylists();
	List<Track> getPlaylistDetail(long playlistId);
}
