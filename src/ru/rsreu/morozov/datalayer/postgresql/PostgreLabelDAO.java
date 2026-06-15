package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.LabelDAO;
import ru.rsreu.morozov.datalayer.data.Label;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreLabelDAO implements LabelDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.label.select.all");
	private static final String ADD = resourcer.getString("sql.label.add");
	private static final String UPDATE = resourcer.getString("sql.label.update");
	private static final String DELETE = resourcer.getString("sql.label.delete");

	public PostgreLabelDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Label> getAllLabels() {
		List<Label> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) list.add(new Label(rs.getLong("label_id"), rs.getString("name"), rs.getInt("foundation_year")));
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public void addNewLabel(String name, int foundationYear) {
		try (PreparedStatement ps = connection.prepareStatement(ADD)) {
			ps.setString(1, name); ps.setInt(2, foundationYear); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void updateLabel(long id, String name, int foundationYear) {
		try (PreparedStatement ps = connection.prepareStatement(UPDATE)) {
			ps.setLong(1, id); ps.setString(2, name); ps.setInt(3, foundationYear); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}

	@Override
	public void deleteLabel(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}
}
