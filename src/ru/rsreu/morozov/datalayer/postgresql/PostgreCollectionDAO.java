package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.CollectionDAO;
import ru.rsreu.morozov.datalayer.data.Collection;
import ru.rsreu.morozov.datalayer.data.Track;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreCollectionDAO implements CollectionDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.collection.select.all");
	private static final String ADD = resourcer.getString("sql.collection.add");
	private static final String UPDATE = resourcer.getString("sql.collection.update");
	private static final String DELETE = resourcer.getString("sql.collection.delete");
	private static final String DETAIL = resourcer.getString("sql.collection.detail");
	private static final String ADD_TRACK = resourcer.getString("sql.collection.add_track");
	private static final String REMOVE_TRACK = resourcer.getString("sql.collection.remove_track");

	public PostgreCollectionDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Collection> getAllCollections() {
		List<Collection> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Collection(rs.getLong("collection_id"), rs.getString("title"), rs.getString("description"), rs.getInt("track_count")));
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public void addNewCollection(String title, String description) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, title); ps.setString(2, description); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void updateCollection(long id, String title, String description) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, title); ps.setString(3, description); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void deleteCollection(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public List<Track> getCollectionDetail(long collectionId) {
		List<Track> list = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(DETAIL)) {
			ps.setLong(1, collectionId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) list.add(new Track(0, rs.getString("track_title"), rs.getString("artist_name"),
						rs.getString("album_title"), rs.getInt("duration_seconds"), 0, rs.getString("genre_name")));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public void addTrackToCollection(long collectionId, String trackTitle) {
		try (PreparedStatement ps = connection.prepareStatement(ADD_TRACK)) {
			ps.setLong(1, collectionId); ps.setString(2, trackTitle); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void removeTrackFromCollection(long collectionId, String trackTitle) {
		try (PreparedStatement ps = connection.prepareStatement(REMOVE_TRACK)) {
			ps.setLong(1, collectionId); ps.setString(2, trackTitle); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}
}
