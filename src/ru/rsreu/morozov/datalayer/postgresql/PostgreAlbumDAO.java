package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.AlbumDAO;
import ru.rsreu.morozov.datalayer.data.Album;
import ru.rsreu.morozov.datalayer.data.Review;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreAlbumDAO implements AlbumDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.album.select.all");
	private static final String ADD = resourcer.getString("sql.album.add");
	private static final String UPDATE = resourcer.getString("sql.album.update");
	private static final String DELETE = resourcer.getString("sql.album.delete");
	private static final String TRACKS = resourcer.getString("sql.album.tracks");
	private static final String REVIEWS = resourcer.getString("sql.album.reviews");

	public PostgreAlbumDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Album> getAllAlbums() {
		List<Album> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) {
				Date d = rs.getDate("release_date");
				list.add(new Album(rs.getLong("album_id"), rs.getString("album_title"), rs.getString("artist_name"),
						d != null ? d.toLocalDate() : null, rs.getString("release_type"),
						rs.getDouble("avg_rating"), rs.getLong("review_count")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public void addNewAlbum(String title, String artistName, Date releaseDate, String releaseType) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, title); ps.setString(2, artistName); ps.setDate(3, releaseDate); ps.setString(4, releaseType); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void updateAlbum(long id, String title, String artistName, Date releaseDate, String releaseType) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, title); ps.setString(3, artistName); ps.setDate(4, releaseDate); ps.setString(5, releaseType); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void deleteAlbum(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public List<Track> getAlbumTracks(long albumId) {
		List<Track> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(TRACKS)) {
			ps.setLong(1, albumId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(new Track(rs.getLong("track_id"), rs.getString("track_title"), "", "", rs.getInt("duration_seconds"), rs.getInt("play_count"), rs.getString("genre_name")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public List<Review> getAlbumReviews(long albumId) {
		List<Review> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(REVIEWS)) {
			ps.setLong(1, albumId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Timestamp ts = rs.getTimestamp("review_date");
					list.add(new Review(rs.getLong("review_id"), rs.getString("username"), "", rs.getInt("rating"),
							ts != null ? ts.toLocalDateTime().toLocalDate() : null));
				}
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}
}
