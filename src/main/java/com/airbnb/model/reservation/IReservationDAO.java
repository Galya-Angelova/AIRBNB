package com.airbnb.model.reservation;

import com.airbnb.exceptions.InvalidReservationException;

public interface IReservationDAO {
	public int makeReservation(Reservation reservation) throws InvalidReservationException;
	public Reservation reservationFromId(int reservationId) throws InvalidReservationException;
	public void giveRating(int rating,int reservationId) throws InvalidReservationException;
}
