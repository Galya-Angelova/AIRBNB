package com.airbnb.model.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidAddressException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class AddressDAO implements IAddressDAO{
//	private static final int EMPTY = 0;
//	private static final String ADD_CITY = "INSERT INTO cities VALUES(null,?)";
//	private static final String ADD_COUNTRY = "INSERT INTO countries VALUES(null,?);";
//	private static final String CHECK_COUNTRY = "SELECT count(*) FROM countries WHERE countries.name = ?;";
//	private static final String CHECK_CITY = "SELECT count(*) FROM cities WHERE cities.name = ?;";
	private static final String ADDRESS_FROM_ID_SQL = "SELECT * FROM addresses WHERE id=?";
	private static final String ADD_ADDRESS_SQL = "INSERT INTO addresses VALUES (null, ?, ?, ?, ?)";
//	private static final String GET_LOCATION_ID_FROM_CITY_AND_COUNTRY = "SELECT * FROM locations JOIN countries ON locations.country_id = countries.id JOIN cities ON locations.city_id = cities.id WHERE cities.name = ? and countries.name = ?;";
//	@Autowired
//	private CityDAO cityDAO;
//	
//	@Autowired
//	private CountryDAO countryDAO;
	
	/*@Autowired
	private static DBConnectionTest dbConnection;

	private static Connection connection = dbConnection.getConnection();*/
	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public AddressDAO(DBConnectionTest dbConnection) {
		this.dbConnection=dbConnection;
		connection = this.dbConnection.getConnection();
	}
	
//	@Override
//	public int addAddress(String street, int streetNumber, String country, String city) throws InvalidAddressException {
//		PreparedStatement ps = null;
//		try {
//			ps = connection.prepareStatement(CHECK_COUNTRY);
//			ps.setString(1, country);
//			ResultSet set = ps.executeQuery();
//			int count = 0;
//			if (set.next()) {
//				count = set.getInt(1);
//			}
//			// ps.close();
//
//			if (count == EMPTY) {
//				ps = connection.prepareStatement(ADD_COUNTRY);
//				ps.setString(1, country);
//			}
//			
//			ps = connection.prepareStatement(CHECK_CITY);
//			ps.setString(1, city);
//			int countCity = ps.executeQuery().getInt(1);
//			if(countCity == EMPTY) {
//				ps = connection.prepareStatement(ADD_CITY);
//				ps.setString(1, city);
//			}
//			
//			
//			ps = connection.prepareStatement(ADD_ADDRESS_SQL, Statement.RETURN_GENERATED_KEYS);
//			
//			ps.setString(1, street);
//			ps.setInt(2, streetNumber);
//			ps.setInt(3, countryDAO.giveCountryId(country));
//			ps.setInt(4,cityDAO.giveCityId(city));
//			ps.executeUpdate();
//
//			ResultSet rs = ps.getGeneratedKeys();
//			rs.next();
//
//			return rs.getInt(1);
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			throw new InvalidAddressException("Invalid statement", e);
//		} catch (InvalidCountryException e) {
//			e.printStackTrace();
//			throw new InvalidAddressException("Invalid country.");
//		} catch (InvalidCityException e) {
//			e.printStackTrace();
//			throw new InvalidAddressException("Invalid city.");
//		}
//	}
	
	@Override
	public int addAddress(Address address) throws InvalidAddressException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_ADDRESS_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, address.getStreet());
			ps.setInt(2, address.getStreetNumber());
			ps.setInt(3, address.getCountry());
			ps.setInt(4, address.getCity());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidAddressException("Invalid statement", e);
		}
	}
	@Override
	public Address addressFromId(int address_id)throws InvalidAddressException {
		try (PreparedStatement ps = connection.prepareStatement(ADDRESS_FROM_ID_SQL)) {
			ps.setInt(1, address_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				int country_id = rs.getInt("country_id");
				int city_id = rs.getInt("city_id");
				String street = rs.getString("street");
				int streetNumber = rs.getInt("streetNumber");

				return new Address(id, country_id, city_id, street, streetNumber);
			}
			throw new InvalidAddressException("There is no address with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidAddressException("Invalid statement", e);
		}
	}
}
