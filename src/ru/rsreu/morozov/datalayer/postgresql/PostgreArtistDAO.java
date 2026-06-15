package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.ArtistDAO;
import ru.rsreu.morozov.datalayer.data.Artist;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreArtistDAO implements ArtistDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.artist.select.all");
	private static final String ADD = resourcer.getString("sql.artist.add");
	private static final String UPDATE = resourcer.getString("sql.artist.update");
	private static final String DELETE = resourcer.getString("sql.artist.delete");
	private static final String TRACKS = resourcer.getString("sql.artist.tracks");

	public PostgreArtistDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Artist> getAllArtists() {
		List<Artist> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Artist(rs.getLong("artist_id"), rs.getString("artist_name"), rs.getString("country"), rs.getString("label_name")));
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public void addNewArtist(String name, String country, String labelName) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, name); ps.setString(2, country); ps.setString(3, labelName); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void updateArtist(long id, String name, String country, String labelName) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, name); ps.setString(3, country); ps.setString(4, labelName); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void deleteArtist(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public List<Track> getArtistTracks(long artistId) {
		List<Track> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(TRACKS)) {
			ps.setLong(1, artistId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(new Track(rs.getLong("track_id"), rs.getString("track_title"), "", rs.getString("album_title"), rs.getInt("duration_seconds"), rs.getInt("play_count"), rs.getString("genre_name")));
			}
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}
}
