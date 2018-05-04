package com.airbnb.model.reservation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidReservationException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class ReservationDAO implements IReservationDAO {
	private static final String ADD_RESERVATION = "INSERT INTO reservation VALUES(null,?,?,?,?,0)";
	private static final String ADD_RATING = "UPDATE reservation SET rating = ? WHERE id = ?;";
	private static final String RESERVATION_FROM_ID = "SELECT * FROM reservations WHERE id = ?;";
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
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(ADD_RESERVATION, Statement.RETURN_GENERATED_KEYS);
			ps.setDate(1, Date.valueOf(reservation.getStartDate()));
			ps.setDate(2, Date.valueOf(reservation.getEndDate()));
			ps.setInt(3, reservation.getPlaceId());
			ps.setInt(4, reservation.getUserId());

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			reservation.setId(rs.getInt(1));

			return rs.getInt(1);
		} catch (SQLException e) {
			throw new InvalidReservationException("Problems in DB",e);
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				throw new InvalidReservationException("Problems in DB",e);
			}
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

				return new Reservation(id, startDate, endDate, placeId, userId, rating);
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

}
