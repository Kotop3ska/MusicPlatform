package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.UserDAO;
import ru.rsreu.morozov.datalayer.data.User;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreUserDAO implements UserDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.user.select.all");
	private static final String DETAIL = resourcer.getString("sql.user.detail");

	public PostgreUserDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<User> getAllUsers() {
		List<User> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) {
				Timestamp ts = rs.getTimestamp("created_at");
				list.add(new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("email"),
						rs.getString("subscription_name"), ts != null ? ts.toLocalDateTime().toLocalDate() : null));
			}
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public User getUserDetail(long userId) {
		try (PreparedStatement ps = connection.prepareStatement(DETAIL)) {
			ps.setLong(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Timestamp ts = rs.getTimestamp("created_at");
					return new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("email"),
							rs.getString("subscription_name"), ts != null ? ts.toLocalDateTime().toLocalDate() : null);
				}
			}
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return User.DEFAULT;
	}
}
