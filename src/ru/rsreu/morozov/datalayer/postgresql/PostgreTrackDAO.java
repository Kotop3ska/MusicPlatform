package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.TrackDAO;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreTrackDAO implements TrackDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.track.select.all");
	private static final String SEARCH = resourcer.getString("sql.track.search");
	private static final String ADD = resourcer.getString("sql.track.add");
	private static final String UPDATE = resourcer.getString("sql.track.update");
	private static final String DELETE = resourcer.getString("sql.track.delete");

	public PostgreTrackDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Track> getAllTracks() {
		List<Track> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Track(rs.getLong("track_id"), rs.getString("track_title"), rs.getString("artist_name"),
					rs.getString("album_title"), rs.getInt("duration_seconds"), rs.getInt("play_count"), rs.getString("genre_name")));
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public List<Track> searchTracks(String query) {
		List<Track> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(SEARCH)) {
			ps.setString(1, query);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(new Track(rs.getLong("track_id"), rs.getString("track_title"), rs.getString("artist_name"),
						rs.getString("album_title"), rs.getInt("duration_seconds"), rs.getInt("play_count"), rs.getString("genre_name")));
			}
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public void addNewTrack(String title, String albumTitle, String genreName, int durationSeconds) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, title); ps.setString(2, albumTitle); ps.setString(3, genreName); ps.setInt(4, durationSeconds); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void updateTrack(long id, String title, String albumTitle, String genreName, int durationSeconds) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, title); ps.setString(3, albumTitle); ps.setString(4, genreName); ps.setInt(5, durationSeconds); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void deleteTrack(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}
}
