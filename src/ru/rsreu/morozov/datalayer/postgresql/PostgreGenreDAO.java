package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.GenreDAO;
import ru.rsreu.morozov.datalayer.data.Genre;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreGenreDAO implements GenreDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.genre.select.all");
	private static final String ADD = resourcer.getString("sql.genre.add");
	private static final String UPDATE = resourcer.getString("sql.genre.update");
	private static final String DELETE = resourcer.getString("sql.genre.delete");

	public PostgreGenreDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Genre> getAllGenres() {
		List<Genre> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public void addNewGenre(String name) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, name); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void updateGenre(long id, String name) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, name); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void deleteGenre(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}
}
