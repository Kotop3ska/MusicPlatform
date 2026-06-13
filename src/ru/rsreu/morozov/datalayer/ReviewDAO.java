package ru.rsreu.morozov.datalayer;

import ru.rsreu.morozov.datalayer.data.Review;
import java.util.List;

public interface ReviewDAO {
	List<Review> getAllReviews();
	void deleteReview(long id);
}
