package com.airbnb.model.review;

import java.util.List;

import com.airbnb.exceptions.InvalidReviewException;

public interface IReviewDAO {

	public int createReview(Review review) throws InvalidReviewException;

	public List<Review> getAllReviewsForPlace(int place_id) throws InvalidReviewException;

	
}
