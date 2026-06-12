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
	private static final String SELECT_ALL_SUBS = PostgreSubscriptionDAO.resourcer.getString("sql.subscriptions.select.all");
	private static final String ADD_SUB = PostgreSubscriptionDAO.resourcer.getString("sql.subscription.add");
	private static final String DELETE_SUB = PostgreSubscriptionDAO.resourcer.getString("sql.subscription.delete");

	public PostgreSubscriptionDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Subscription> getAllSubscriptions() {
		List<Subscription> subscriptions = new ArrayList<>();

		try (Statement statement = this.connection.createStatement();
			 ResultSet rs = statement.executeQuery(PostgreSubscriptionDAO.SELECT_ALL_SUBS)) {
			while (rs.next()) {
				Subscription subscription = new Subscription(rs.getString("name"),
						rs.getDouble("price"), rs.getInt("duration_days"));
				subscriptions.add(subscription);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			subscriptions.add(Subscription.DEFAULT);
		}
		return subscriptions;
	}

	@Override
	public void addNewSubscription(String name, Double price, int durationDays) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreSubscriptionDAO.ADD_SUB)) {
			statement.setString(1, name);
			statement.setDouble(2, price);
			statement.setInt(3, durationDays);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteSubscriptionByName(String name) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreSubscriptionDAO.DELETE_SUB)) {
			statement.setString(1, name);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
