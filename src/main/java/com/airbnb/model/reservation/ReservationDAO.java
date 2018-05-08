package com.airbnb.model.reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidReservationException;
import com.airbnb.model.db.DBConnectionTest;
import com.airbnb.model.place.Place;
import com.airbnb.model.place.PlaceDTO;
import com.airbnb.model.user.User;

@Component
public class ReservationDAO implements IReservationDAO {
	private static final String ADD_RESERVATION = "INSERT INTO reservation VALUES(null,?,?,?,?,0,?,0)";
	private static final String ADD_RATING = "UPDATE reservation SET rating = ? WHERE id = ?;";
	private static final String DELETE_RESERVATION = "UPDATE reservation SET deleted = 1 WHERE id = ?;";
	private static final String RESERVATION_FROM_ID = "SELECT * FROM reservation WHERE id = ?;";
	private static final String RESERVATIONS_FOR_BY_HOST_ID = "SELECT r.id,r.startDate,r.endDate,r.place_id,r.user_id AS guest_id,r.reservationDate,p.user_id AS host_id FROM reservation AS r INNER JOIN place AS p ON(r.place_id = p.id) WHERE r.deleted=0 AND p.user_id=? ORDER BY id DESC;";
	private static final String RESERVATIONS_FOR_BY_GUEST_ID = "SELECT * FROM reservation where deleted = 0 AND user_id=? ORDER BY id DESC;";
	
	@Autowired
	private DBConnectionTest dbConnection;
	private Connection connection;

	@Autowired
	public ReservationDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}
	@Override
	public int makeReservation(Reservation reservation) throws InvalidReservationException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_RESERVATION, Statement.RETURN_GENERATED_KEYS)){
			ps.setDate(1, Date.valueOf(reservation.getStartDate()));
			ps.setDate(2, Date.valueOf(reservation.getEndDate()));
			ps.setInt(3, reservation.getPlaceId());
			ps.setInt(4, reservation.getUserId());
			ps.setDate(5, Date.valueOf(LocalDate.now()));
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			reservation.setId(rs.getInt(1));
			
			return rs.getInt(1);
		} catch (SQLException e) {
			throw new InvalidReservationException("Problems in DB",e);
		}
	}

	@Override
	public Reservation reservationFromId(int reservationId) throws InvalidReservationException {
		try (PreparedStatement ps = connection.prepareStatement(RESERVATION_FROM_ID)) {
			ps.setInt(1, reservationId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				LocalDate startDate = rs.getDate("startDate").toLocalDate();
				LocalDate endDate = rs.getDate("endDate").toLocalDate();
				int placeId = rs.getInt("place_id");
				int userId = rs.getInt("user_id");
				int rating = rs.getInt("rating");
				LocalDate reservationDate=rs.getDate("reservationDate").toLocalDate();
				return new Reservation(id, startDate, endDate, placeId, userId, rating,reservationDate);
			}

			throw new InvalidReservationException("There is no reservation with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidReservationException("Invalid statement", e);
		}
	}

	@Override
	public void giveRating(int rating, int reservationId) throws InvalidReservationException {
		try(PreparedStatement ps = connection.prepareStatement(ADD_RATING)){
			ps.setInt(1, rating);
			ps.setInt(2, reservationId);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new InvalidReservationException("Invalid statement in DB",e);
		}
	}
	
	@Override
	public void deleteReservation(int reservationId) throws InvalidReservationException {
		try(PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION)){
			ps.setInt(1, reservationId);
			ps.executeUpdate();
		}catch(SQLException e) {
			throw new InvalidReservationException("Invalid statement in DB",e);
		}
	}
	
	@Override
	public List<Reservation> getAllReservationsByHostID(int owner_id)throws InvalidReservationException{
		List<Reservation> reservations = new ArrayList<Reservation>();
		try (PreparedStatement ps = connection.prepareStatement(RESERVATIONS_FOR_BY_HOST_ID)) {
			ps.setInt(1, owner_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				LocalDate startDate = rs.getDate("startDate").toLocalDate();
				LocalDate endDate = rs.getDate("endDate").toLocalDate();
				int placeId = rs.getInt("place_id");
				int userId = rs.getInt("guest_id");

				LocalDate reservationDate=rs.getDate("reservationDate").toLocalDate();
				reservations.add(new Reservation(id, startDate, endDate, placeId, userId, 0,reservationDate));
			}
			return reservations;
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidReservationException("Invalid statement", e);
		}
	}
	
	@Override
	public List<Reservation> getAllVisitedPlacesByID(int guest_id) throws InvalidReservationException {
		List<Reservation> reservations = new ArrayList<Reservation>();
		try (PreparedStatement ps = connection.prepareStatement(RESERVATIONS_FOR_BY_GUEST_ID)) {
			ps.setInt(1, guest_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				LocalDate startDate = rs.getDate("startDate").toLocalDate();
				LocalDate endDate = rs.getDate("endDate").toLocalDate();
				int placeId = rs.getInt("place_id");
				int userId = rs.getInt("user_id");
				LocalDate reservationDate=rs.getDate("reservationDate").toLocalDate();
				reservations.add(new Reservation(id, startDate, endDate, placeId, userId, 0,reservationDate));
			}
			return reservations;
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidReservationException("Invalid statement", e);
		}
	}
}
