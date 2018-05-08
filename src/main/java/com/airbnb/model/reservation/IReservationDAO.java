package com.airbnb.model.reservation;

import java.time.LocalDate;
import java.util.List;

import com.airbnb.exceptions.InvalidReservationException;
import com.airbnb.model.place.Place;
import com.airbnb.model.place.PlaceDTO;
import com.airbnb.model.user.User;

public interface IReservationDAO {
	public int makeReservation(Reservation reservation) throws InvalidReservationException;
	public Reservation reservationFromId(int reservationId) throws InvalidReservationException;
	public void giveRating(int rating,int reservationId) throws InvalidReservationException;
	public void deleteReservation(int reservationId) throws InvalidReservationException;
	public List<Reservation> getAllReservationsByHostID(int owner_id) throws InvalidReservationException;
	public List<Reservation> getAllVisitedPlacesByID(int guest_id) throws InvalidReservationException;
	
}
