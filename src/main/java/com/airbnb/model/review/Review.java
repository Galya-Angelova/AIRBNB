package com.airbnb.model.review;

import com.airbnb.exceptions.InvalidReviewException;

public class Review {
	private static final int POSITIVE = 0;
	
	private int id;
	private String title;
	private String text;
	private int placeId;
	private int userId;
	
	public Review(){}
	
	public Review(int id, String title, String text, int placeId,int userId) throws InvalidReviewException {
		setId(id);
		setTitle(title);
		setText(text);
		setPlaceId(placeId);
		setUserId(userId);
	}

//	Setters
	public void setId(int id) throws InvalidReviewException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidReviewException("Invalid id for review.");
		}
	}

	public void setTitle(String title) throws InvalidReviewException {
		if (title == null || (title.trim().isEmpty())) {
			throw new InvalidReviewException("Empty title.");
		} else {
			this.title = title;
		}
	}

	public void setText(String text) throws InvalidReviewException {
		if (text == null || (text.trim().isEmpty())) {
			throw new InvalidReviewException("Empty text.");
		} else {
			this.text = text;
		}
	}

	public void setPlaceId(int placeId) throws InvalidReviewException {
		if (placeId >= POSITIVE) {
			this.placeId = placeId;
		} else {
			throw new InvalidReviewException("Invalid id for review's place.");
		}
	}
	
	public void setUserId(int userId) throws InvalidReviewException {
		if (userId >= POSITIVE) {
			this.userId = userId;
		} else {
			throw new InvalidReviewException("Invalid id for review's user.");
		}
	}
//	Getters
	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return this.title;
	}

	public String getText() {
		return this.text;
	}

	public int getPlaceId() {
		return this.placeId;
	}
	
	public int getUserId() {
		return this.userId;
	}
}
