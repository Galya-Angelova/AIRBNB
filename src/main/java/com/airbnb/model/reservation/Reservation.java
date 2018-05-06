package com.airbnb.model.reservation;

import java.time.DateTimeException;
import java.time.LocalDate;

import com.airbnb.exceptions.InvalidReservationException;

public class Reservation {
	private static final int POSITIVE = 0;
	private static final int MIN_RATING = 0;
	private static final int MAX_RATING = 5;
	
	private int id;
	private LocalDate startDate;
	private LocalDate endDate;
	private int placeId;
	private int userId;
	private int rating;

	public Reservation() {};
	public Reservation(int id, LocalDate startDate, LocalDate endDate, int placeId, int userId, int rating)
			throws InvalidReservationException {
		setId(id);
		setStartDate(startDate.getDayOfMonth(), startDate.getMonthValue(), startDate.getYear());
		setEndDate(endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear());
		setPlaceId(placeId);
		setUserId(userId);
		setRating(rating);
	}

//	Setters
	public void setId(int id) throws InvalidReservationException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidReservationException("Invalid id for reservation.");
		}
	}

	public void setStartDate(int day, int month, int year) throws InvalidReservationException {
		try {
			LocalDate startDate = LocalDate.of(year, month, day);
			this.startDate = startDate;
		} catch (DateTimeException e) {
			throw new InvalidReservationException("Invalid date.");
		}
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

	public void setPlaceId(int placeId) throws InvalidReservationException {
		if (placeId >= POSITIVE) {
			this.placeId = placeId;
		} else {
			throw new InvalidReservationException("Invalid id for reservations's place.");
		}
	}

	public void setUserId(int userId) throws InvalidReservationException {
		if (userId >= POSITIVE) {
			this.userId = userId;
		} else {
			throw new InvalidReservationException("Invalid id for reservations's user.");
		}
	}

	public void setRating(int rating) throws InvalidReservationException {
		if (rating >= MIN_RATING && rating <= MAX_RATING) {
			this.rating = rating;
		} else {
			throw new InvalidReservationException("Invalid rating.");
		}
	}

//	Getters
	public int getId() {
		return this.id;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public int getPlaceId() {
		return this.placeId;
	}

	public int getUserId() {
		return this.userId;
	}

	public int getRating() {
		return this.rating;
	}

}
