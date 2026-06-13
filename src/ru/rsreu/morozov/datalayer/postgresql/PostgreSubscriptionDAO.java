package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.SubscriptionDAO;
import ru.rsreu.morozov.datalayer.data.Subscription;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreSubscriptionDAO implements SubscriptionDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.subscriptions.select.all");
	private static final String ADD = resourcer.getString("sql.subscription.add");
	private static final String UPDATE = resourcer.getString("sql.subscription.update");
	private static final String DELETE = resourcer.getString("sql.subscription.delete");

	public PostgreSubscriptionDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Subscription> getAllSubscriptions() {
		List<Subscription> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Subscription(rs.getLong("subscription_id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("duration_days")));
		} catch (SQLException e) { e.printStackTrace(); }
		return list;
	}

	@Override
	public void addNewSubscription(String name, Double price, int durationDays) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, name); ps.setDouble(2, price); ps.setInt(3, durationDays); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void updateSubscription(long id, String name, Double price, int durationDays) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, name); ps.setDouble(3, price); ps.setInt(4, durationDays); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void deleteSubscription(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { e.printStackTrace(); }
	}
}
