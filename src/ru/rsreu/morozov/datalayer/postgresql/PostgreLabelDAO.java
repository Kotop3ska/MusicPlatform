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
	private static final String SELECT_ALL_LABELS = PostgreLabelDAO.resourcer.getString("sql.label.select.all");
	private static final String ADD_LABEL = PostgreLabelDAO.resourcer.getString("sql.label.add");
	private static final String DELETE_LABEL = PostgreLabelDAO.resourcer.getString("sql.label.delete");
	private static final String UPDATE_LABEL = PostgreLabelDAO.resourcer.getString("sql.label.update");

	public PostgreLabelDAO(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Label> getAllLabels() {
		List<Label> labeles = new ArrayList<>();

		try (Statement statement = this.connection.createStatement();
			 ResultSet rs = statement.executeQuery(PostgreLabelDAO.SELECT_ALL_LABELS)) {
			while (rs.next()) {
				Label label = new Label(rs.getString("name"),
						rs.getInt("foundation_year"));
				labeles.add(label);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			labeles.add(Label.DEFAULT);
		}
		return labeles;
	}

	@Override
	public void addNewLabel(String name, int foundationYear) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreLabelDAO.ADD_LABEL)) {
			statement.setString(1, name);
			statement.setInt(2, foundationYear);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteLabelByName(String name) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreLabelDAO.DELETE_LABEL)) {
			statement.setString(1, name);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateLabelByName(String oldName, String newName, int foundationYear) {
		try (CallableStatement statement = this.connection.prepareCall(PostgreLabelDAO.UPDATE_LABEL)) {
			statement.setString(1, oldName);
			statement.setString(2, newName);
			statement.setInt(3, foundationYear);

			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
