package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.DashboardDAO;
import ru.rsreu.morozov.datalayer.data.DashboardStats;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;

public class PostgreDashboardDAO implements DashboardDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String STATS = resourcer.getString("sql.dashboard.stats");

	public PostgreDashboardDAO(Connection connection) { this.connection = connection; }

	@Override
	public DashboardStats getStats() {
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(STATS)) {
			if (rs.next()) return new DashboardStats(rs.getLong("user_count"), rs.getLong("track_count"),
					rs.getLong("album_count"), rs.getLong("subscription_count"),
					rs.getLong("artist_count"), rs.getLong("genre_count"));
		} catch (SQLException e) { e.printStackTrace(); }
		return DashboardStats.DEFAULT;
	}
}
