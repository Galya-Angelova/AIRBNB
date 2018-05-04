package com.airbnb.model.review;

import com.airbnb.exceptions.InvalidReviewException;

public interface IReviewDAO {

	public int createReview(String title,String text,String placeName)throws InvalidReviewException;
}
