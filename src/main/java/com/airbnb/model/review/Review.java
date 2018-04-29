package com.airbnb.model.review;

import com.airbnb.model.place.Place;

public class Review {
	private int id;
	private String title;
	private String text;
	private int placeId;
	
	public Review(int id, String title, String text, int placeId) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.placeId = placeId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPlace() {
		return placeId;
	}

	public void setPlace(int placeId) {
		
		this.placeId = placeId;
	}
	
	
}
