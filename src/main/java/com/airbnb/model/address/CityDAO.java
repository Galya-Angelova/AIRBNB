package com.airbnb.model.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class CityDAO implements ICityDAO{
	private static final String ADD_CITY_SQL = "INSERT INTO cities VALUES (null, ?)";
	private static final String GIVE_COUNTRY_SQL = "SELECT * FROM cities WHERE name= ?";
	private static final String CITY_FROM_ID_SQL = "SELECT * FROM cities WHERE id=?";

	@Autowired
	private static DBConnectionTest dbConnection;

	private static Connection connection = dbConnection.getConnection();

	@Override
	public int addCity(City city) throws InvalidCityException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_CITY_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, city.getName());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCityException("Invalid statement", e);
		}
	}

	@Override
	public int giveCityId(String city) throws InvalidCityException {
		try (PreparedStatement ps = connection.prepareStatement(GIVE_COUNTRY_SQL)) {
			ps.setString(1, city);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new InvalidCityException("There is no city with that name in the database");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCityException("Invalid statement", e);
		}
	}

	@Override
	public City cityFromId(int city_id) throws InvalidCityException {
		try (PreparedStatement ps = connection.prepareStatement(CITY_FROM_ID_SQL)) {
			ps.setInt(1, city_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");

				return new City(id, name);
			}
			throw new InvalidCityException("There is no city with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidCityException("Invalid statement", e);
		}
	}
}
