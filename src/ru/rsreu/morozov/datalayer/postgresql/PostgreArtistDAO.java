package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.ArtistDAO;
import ru.rsreu.morozov.datalayer.data.Artist;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreArtistDAO implements ArtistDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL_ARTISTS = PostgreArtistDAO.resourcer.getString("sql.artist.select.all");
	private static final String ADD_ARTIST = PostgreArtistDAO.resourcer.getString("sql.artist.add");
	private static final String UPDATE_ARTIST = PostgreArtistDAO.resourcer.getString("sql.artist.update");
	private static final String DELETE_ARTIST = PostgreArtistDAO.resourcer.getString("sql.artist.delete");

	public PostgreArtistDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Artist> getAllArtists() {
		List<Artist> artists = new ArrayList<>();

		try (Statement statement = this.connection.createStatement();
			 ResultSet rs = statement.executeQuery(PostgreArtistDAO.SELECT_ALL_ARTISTS)) {
			while (rs.next()) {
				Artist artist = new Artist(rs.getString("artist_name"),
						rs.getString("country"),
						rs.getString("label_name"));
				artists.add(artist);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			artists.add(Artist.DEFAULT);
		}
		return artists;
	}

	@Override
	public void addNewArtist(String name, String country, String labelName) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreArtistDAO.ADD_ARTIST)) {
			statement.setString(1, name);
			statement.setString(2, country);
			statement.setString(3, labelName);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateArtist(String oldName, String newName, String country, String labelName) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreArtistDAO.UPDATE_ARTIST)) {
			statement.setString(1, oldName);
			statement.setString(2, newName);
			statement.setString(3, country);
			statement.setString(4, labelName);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteArtistByName(String name) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreArtistDAO.DELETE_ARTIST)) {
			statement.setString(1, name);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
