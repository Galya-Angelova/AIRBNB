package com.airbnb.model.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidAddressException;
import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.exceptions.InvalidCountryException;
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
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountryDAO countryDAO;
	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public AddressDAO(DBConnectionTest dbConnection) {
		this.dbConnection=dbConnection;
		connection = this.dbConnection.getConnection();
	}
	
	@Override
	public int addAddress(Address address) throws InvalidAddressException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_ADDRESS_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, address.getStreet());
			ps.setInt(2, address.getStreetNumber());
			ps.setInt(3, address.getCountryId());
			ps.setInt(4, address.getCityId());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			 e.printStackTrace();
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
				Address result = new Address(id, country_id, city_id, street, streetNumber);
				result.setCity(cityDAO.cityFromId(city_id));
				result.setCountry(countryDAO.countryFromId(country_id));
				return result;
			}
			throw new InvalidAddressException("There is no address with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidAddressException("Invalid statement", e);
		} catch (InvalidCityException e) {
			e.printStackTrace();
			throw new InvalidAddressException("Invalid city", e);
		} catch (InvalidCountryException e) {
			e.printStackTrace();
			throw new InvalidAddressException("Invalid country", e);
		}
	}
}
