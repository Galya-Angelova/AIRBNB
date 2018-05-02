package com.airbnb.model.place;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.db.DBConnectionTest;
import com.airbnb.model.place.Place.PlaceType;

@Component
public class PlaceDAO implements IPlaceDAO {
	private static final String ALL_PLACE_TYPES = "SELECT * FROM placetype;";
//	private static final String GET_ADDRESS = "SELECT * FROM locations JOIN countries ON locations.country_id = countries.id JOIN cities ON locations.city_id = cities.id WHERE cities.name = ? and countries.name = ?;";
	private static final String PLACE_FROM_ID = "SELECT * FROM users WHERE id=?";
	
//	private static final String CHECK_PLACE_TYPE = "SELECT count(*) FROM placetype WHERE placetype.name = ?;";
//	private static final String CHECK_EMAIL = "SELECT count(*) FROM users WHERE users.email = ?;";
//	private static final String ADD_PLACE = "INSERT INTO place VALUES(null,?,false,?,(SELECT placetype.id FROM placetype where placetype.name = ?),(SELECT users.id FROM users WHERE users.email = ?));";
	
	// private static final String INSERT_REVIEW = "INSERT INTO place
	// VALUES(null,'Street',1,(select countries.id from countries where
	// countries.name = 'Bulgaria'),(select placetype.id from placetype where
	// placetype.name = 'House'),(select users.id from users where users.email =
	// 'mail@bg.bg'));";
	
//	private static final int EMPTY_NAME = 0;
	
//	private static final String ADD_PLACE_TYPE = "INSERT INTO placetype VALUES(null,?);";
	
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
	private static final String ADD_PLACE_SQL = "INSERT INTO place VALUES (null, ?,false,?,?,?)";
	private static final String ADD_PLACETYPE_SQL = "INSERT INTO placetype VALUES (null, ?)";
	private static final String GIVE_PLACETYPE_SQL = "SELECT * FROM placetype WHERE name= ?";
	private static final String PLACETYPE_FROM_ID_SQL = "SELECT * FROM placetype WHERE id=?";

	
//	@Autowired
//	private AddressDAO addressDAO;
	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public PlaceDAO(DBConnectionTest dbConnection) {
		this.dbConnection=dbConnection;
		connection = this.dbConnection.getConnection();
	}
	
//	@Override
//	public int createPlace(String name,String streetName,int streetNumber,String city, String countryName, String placeTypeName, String userEmail)
//			throws InvalidPlaceException {
//		if (name != null && streetName != null && countryName != null && city != null && placeTypeName != null && userEmail != null
//				&& !(name.isEmpty() || streetName.isEmpty() || countryName.isEmpty() || city.isEmpty() || placeTypeName.isEmpty() || userEmail.isEmpty())) {
//			
//			// PreparedStatement statement = connection.prepareStatement(CHECK_COUNTRY);
//			PreparedStatement ps = null;
//			PreparedStatement ps2 = null;
//			try {
//				int locationId = addressDAO.addAddress(streetName, streetNumber, countryName, city);
//				
//				// ps2.close();
//				ps = connection.prepareStatement(CHECK_PLACE_TYPE);
//				ps.setString(1, placeTypeName);
//				int countPlaceType = ps.executeQuery().getInt(1);// if there is a place type with this name,
//																	// countPlaceType = 1 else 0
//				if (countPlaceType == EMPTY_NAME) {
//					ps = connection.prepareStatement(ADD_PLACE_TYPE);
//					ps.setString(1, placeTypeName);
//				}
//
//				ps = connection.prepareStatement(CHECK_EMAIL);
//				ps.setString(1, userEmail);
//				int countUserEmail = ps.executeQuery().getInt(1);
//				if (countUserEmail == EMPTY_NAME) {
//					throw new InvalidUserException("There is no user with this email and you can't add new place.");
//				}
//
//				connection.setAutoCommit(false);
//				ps.setString(1, name);
//				ps.setInt(2, locationId);
//				ps.setString(3, placeTypeName);
//				ps.setString(3, userEmail);
//
//				ResultSet rs = ps.getGeneratedKeys();
//				rs.next();
//
//				connection.commit();
//				ps.close();
//
//				return rs.getInt(1);
//
//			} catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					throw new InvalidPlaceException("Oops something went wrong.", e1);
//				}
//				throw new InvalidPlaceException("Invalid statement", e);
//			} catch (InvalidUserException e) {
//				throw new InvalidPlaceException("Invalid user", e);
//			} catch (InvalidAddressException e) {
//				throw new InvalidPlaceException("Invalid address", e);
//			} finally {
//
//				try {
//					ps.close();
//					ps2.close();
//					connection.setAutoCommit(false);
//				} catch (SQLException e) {
//					throw new InvalidPlaceException("Set auto commit false error", e);
//				}
//			}
//		} else {
//			throw new InvalidPlaceException("Please insert street,street number, city, country, place type and email.");
//		}
//	}
	
	@Override
	public int addPlace(Place place)
			throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_PLACE_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, place.getName());
			ps.setInt(2, place.getAddressID());
			int placeTypeId=0;
			try{
				placeTypeId=givePlaceTypeId(place.getPlaceTypeName());
			}catch (InvalidPlaceException e){
				placeTypeId=addPlaceType(place.getPlaceType());
			}
			ps.setInt(3,placeTypeId);
			ps.setInt(4, place.getOwnerId());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}
	
	@Override
	public int addPlaceType(PlaceType placeType) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_PLACETYPE_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, placeType.getName());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}
	
	@Override
	public int givePlaceTypeId(String placeType) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(GIVE_PLACETYPE_SQL)) {
			ps.setString(1, placeType);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new InvalidPlaceException("There is no place type with that name in the database");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

	@Override
	public String placeTypeFromId(int placeType_id) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(PLACETYPE_FROM_ID_SQL)) {
			ps.setInt(1, placeType_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");

				return name;
			}
			throw new InvalidPlaceException("There is no place type with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
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
				int addressId = rs.getInt("address_id");
				boolean busied = rs.getBoolean("busied");
				int placeTypeId = rs.getInt("placeType_id");
				String placeTypeName = placeTypeFromId(placeTypeId);
				int userId = rs.getInt("user_id");
				// int address_id = rs.getInt("locations_id");

				return new Place(id, name, busied, addressId, placeTypeName, userId);
			}

			throw new InvalidPlaceException("There is no place with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}
	
	@Override
	public List<PlaceType> getAllPlaceTypes() throws InvalidPlaceException {
		List<PlaceType> result = new ArrayList<>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_PLACE_TYPES);
			while(set.next()) {
				//int id = set.getInt("id");
				String name = set.getString("name");
				
				PlaceType placeType = PlaceType.fromString(name);
				result.add(placeType);
			}
			if(result.isEmpty()) {
				throw new InvalidPlaceException("No such place types.");
			}else {
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: "  +e.getMessage());
		}
	}
}
