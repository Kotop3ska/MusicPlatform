package ru.rsreu.morozov.datalayer.postgresql;

import ru.rsreu.morozov.datalayer.ReviewDAO;
import ru.rsreu.morozov.datalayer.data.Review;
import ru.rsreu.morozov.resourcer.ProjectResourcer;
import ru.rsreu.morozov.resourcer.Resourcer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgreReviewDAO implements ReviewDAO {
	private final Connection connection;
	private static Resourcer resourcer = ProjectResourcer.getInstance("resources.queries");
	private static final String SELECT_ALL = resourcer.getString("sql.review.select.all");
	private static final String DELETE = resourcer.getString("sql.review.delete");

	public PostgreReviewDAO(Connection connection) { this.connection = connection; }

	@Override
	public List<Review> getAllReviews() {
		List<Review> list = new ArrayList<>();
		try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL)) {
			while (rs.next()) {
				Timestamp ts = rs.getTimestamp("review_date");
				list.add(new Review(rs.getLong("review_id"), rs.getString("username"), rs.getString("album_title"),
						rs.getInt("rating"), ts != null ? ts.toLocalDateTime().toLocalDate() : null));
			}
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
		return list;
	}

	@Override
	public void deleteReview(long id) {
		try (PreparedStatement ps = connection.prepareStatement(DELETE)) {
			ps.setLong(1, id); ps.execute();
		} catch (SQLException e) { throw new RuntimeException(e.getMessage(), e); }
	}
}
