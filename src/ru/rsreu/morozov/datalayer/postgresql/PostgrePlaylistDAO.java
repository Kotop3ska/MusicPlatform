package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.PlaylistDAO;
import ru.rsreu.morozov.datalayer.data.Playlist;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgrePlaylistDAO implements PlaylistDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.playlist.select.all");
	private static final String DETAIL = resourcer.getString("sql.playlist.detail");

	public PostgrePlaylistDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Playlist> getAllPlaylists() {
		List<Playlist> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) {
				Timestamp ts = rs.getTimestamp("created_at");
				list.add(new Playlist(rs.getLong("playlist_id"), rs.getString("playlist_name"), rs.getString("username"),
						ts != null ? ts.toLocalDateTime().toLocalDate() : null, rs.getInt("track_count")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public List<Track> getPlaylistDetail(long playlistId) {
		List<Track> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(DETAIL)) {
			ps.setLong(1, playlistId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(new Track(0, rs.getString("track_title"), rs.getString("artist_name"), "", rs.getInt("duration_seconds"), 0, ""));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}
}
