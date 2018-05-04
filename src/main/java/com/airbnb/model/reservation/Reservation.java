package com.airbnb.model.reservation;

import java.time.DateTimeException;
import java.time.LocalDate;

import com.airbnb.exceptions.InvalidReservationException;
import com.airbnb.exceptions.InvalidUserException;

public class Reservation {
	private static final int MIN_RATING = 0;
	private static final int MAX_RATING = 5;
	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private int placeId;
	private int userId;
	private int rating;

	public Reservation(int id, LocalDate startDate, LocalDate endDate, int placeId, int userId, int rating)
			throws InvalidReservationException {
		this.id = id;
		setStartDate(startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear());
		setEndDate(endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear());
		this.placeId = placeId;
		this.userId = userId;
		setRating(rating);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(int day, int month, int year) throws InvalidReservationException {
		try {
			LocalDate startDate = LocalDate.of(year, month, day);
			this.startDate = startDate;
		} catch (DateTimeException e) {
			throw new InvalidReservationException("Invalid date.");
		}
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(int day, int month, int year) throws InvalidReservationException {
		try {
			LocalDate endDate = LocalDate.of(year, month, day);
			if (endDate.isAfter(this.startDate)) {
				this.endDate = endDate;
			} else {
				throw new InvalidReservationException("End date must be after start date.");
			}
		} catch (DateTimeException e) {
			throw new InvalidReservationException("Invalid date.");
		}

	}

	public int getPlaceId() {
		return placeId;
	}

	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setRating(int rating) throws InvalidReservationException {
		if (rating >= MIN_RATING && rating <= MAX_RATING) {
			this.rating = rating;
		} else {
			throw new InvalidReservationException("Invalid rating.");
		}
	}

	public int getRating() {
		return this.rating;
	}

}
