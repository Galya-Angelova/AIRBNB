package com.airbnb.model.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidCountryException;
import com.airbnb.model.db.DBConnection;

@Component
public class CountryDAO implements ICountryDAO{
	private static final String ADD_COUNTRY_SQL = "INSERT INTO countries VALUES (null, ?)";
	private static final String GIVE_COUNTRY_SQL = "SELECT * FROM countries WHERE name= ?";
	private static final String COUNTRY_FROM_ID_SQL = "SELECT * FROM countries WHERE id=?";

	
	@Autowired
	private  DBConnection dbConnection;
	private  Connection connection;
	
	@Autowired
	public CountryDAO(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}


	
	@Override
	public int addCountry(Country country) throws InvalidCountryException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_COUNTRY_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, country.getName());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCountryException("Invalid statement", e);
		}
	}
	@Override
	public int giveCountryId(String country) throws InvalidCountryException {
		try (PreparedStatement ps = connection.prepareStatement(GIVE_COUNTRY_SQL)) {
			ps.setString(1, country);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new InvalidCountryException("There is no country with that name in the database");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCountryException("Invalid statement", e);
		}
	}

	@Override
	public Country countryFromId(int country_id) throws InvalidCountryException {
		try (PreparedStatement ps = connection.prepareStatement(COUNTRY_FROM_ID_SQL)) {
			ps.setInt(1, country_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");

				return new Country(id, name);
			}
			throw new InvalidCountryException("There is no country with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCountryException("Invalid statement", e);
		}
	}
}
