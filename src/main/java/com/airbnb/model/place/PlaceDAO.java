package com.airbnb.model.place;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.exceptions.InvalidReviewException;
import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.db.DBConnectionTest;
import com.airbnb.model.review.ReviewDAO;
import com.airbnb.model.user.User;

public class PlaceDAO implements IPlaceDAO {
	private static final String PLACE_FROM_ID = "SELECT * FROM users WHERE id=?";
	private static final String CHECK_COUNTRY = "SELECT count(*) FROM countries WHERE countries.name = ?;";
	private static final String CHECK_PLACE_TYPE = "SELECT count(*) FROM placetype WHERE placetype.name = ?;";
	private static final String CHECK_EMAIL = "SELECT count(*) FROM users WHERE users.email = ?;";
	private static final String ADD_PLACE = "INSERT INTO place VALUES(null,?,?,(SELECT countries.id FROM countries WHERE countries.name = ?),(SELECT placetype.id FROM placetype where placetype.name = ?),(SELECT users.id FROM users WHERE users.email = ?));";
	// private static final String INSERT_REVIEW = "INSERT INTO place
	// VALUES(null,'Street',1,(select countries.id from countries where
	// countries.name = 'Bulgaria'),(select placetype.id from placetype where
	// placetype.name = 'House'),(select users.id from users where users.email =
	// 'mail@bg.bg'));";
	private static final int EMPTY_NAME = 0;

	private static final String ADD_COUNTRY = "INSERT INTO countries VALUES(null,?);";
	private static final String ADD_PLACE_TYPE = "INSERT INTO placetype VALUES(null,?);";
	/*@Autowired
	private static DBConnectionTest dbConnection;
	private static Connection connection;

	static {
		try {
			dbConnection = new DBConnectionTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PlaceDAO() {
		connection = PlaceDAO.dbConnection.getConnection();
	}*/

	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public PlaceDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}
	@Override
	public int createPlace(String streetName, String countryName, String placeTypeName, String userEmail)
			throws InvalidPlaceException {
		if (streetName != null && countryName != null && placeTypeName != null && userEmail != null
				&& !(streetName.isEmpty() || countryName.isEmpty() || placeTypeName.isEmpty() || userEmail.isEmpty())) {
			// PreparedStatement statement = connection.prepareStatement(CHECK_COUNTRY);
			PreparedStatement ps = null;
			PreparedStatement ps2 = null;
			try {
				ps = connection.prepareStatement(CHECK_COUNTRY);
				ps.setString(1, countryName);
				ResultSet set = ps.executeQuery();
				int count = 0;
				if (set.next()) {
					count = set.getInt(1);
				}
				// ps.close();

				if (count == EMPTY_NAME) {
					ps = connection.prepareStatement(ADD_COUNTRY);
					ps.setString(1, countryName);
				}
				// ps2.close();
				ps = connection.prepareStatement(CHECK_PLACE_TYPE);
				ps.setString(1, placeTypeName);
				int countPlaceType = ps.executeQuery().getInt(1);// if there is a place type with this name,
																	// countPlaceType = 1 else 0
				if (countPlaceType == EMPTY_NAME) {
					ps = connection.prepareStatement(ADD_PLACE_TYPE);
					ps.setString(1, placeTypeName);
				}

				ps = connection.prepareStatement(CHECK_EMAIL);
				ps.setString(1, userEmail);
				int countUserEmail = ps.executeQuery().getInt(1);
				if (countUserEmail == EMPTY_NAME) {
					throw new InvalidUserException("There is no user with this email and you can't add new place.");
				}

				connection.setAutoCommit(false);
				ps.setString(1, streetName);
				ps.setString(2, countryName);
				ps.setString(3, placeTypeName);
				ps.setString(3, userEmail);

				ResultSet rs = ps.getGeneratedKeys();
				rs.next();

				connection.commit();
				ps.close();

				return rs.getInt(1);

			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new InvalidPlaceException("Oops something went wrong.", e1);
				}
				throw new InvalidPlaceException("Invalid statement", e);
			} catch (InvalidUserException e) {
				throw new InvalidPlaceException("Invalid user", e);
			} finally {

				try {
					ps.close();
					ps2.close();
					connection.setAutoCommit(false);
				} catch (SQLException e) {
					throw new InvalidPlaceException("Set auto commit false error", e);
				}
			}
		} else {
			throw new InvalidPlaceException("Please insert street, country, place type and email.");
		}
	}

	@Override
	public Place placeFromId(int placeId) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(PLACE_FROM_ID)) {
			ps.setInt(1, placeId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				int locationId = rs.getInt("location_id");
				boolean busied = rs.getBoolean("busied");
				int placeTypeId = rs.getInt("placeType_id");
				int userId = rs.getInt("user_id");
				// int address_id = rs.getInt("locations_id");

				//return new Place(id, name, busied, locationId, placeTypeName, userId); //TODO get placeType name from placeType table
			}

			throw new InvalidPlaceException("There is no place with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

}
