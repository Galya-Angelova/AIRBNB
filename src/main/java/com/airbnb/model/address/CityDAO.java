package com.airbnb.model.address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class CityDAO implements ICityDAO{
	private static final String ADD_CITY_SQL = "INSERT INTO cities VALUES (null, ?)";
	private static final String GIVE_CITY_SQL = "SELECT * FROM cities WHERE name= ?";
	private static final String CITY_FROM_ID_SQL = "SELECT * FROM cities WHERE id=?";
	private static final String ALL_CITIES_SQL = "SELECT name FROM cities;";
	
	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public CityDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}

//	@Override
//	public int addCity(String city) throws InvalidCityException {
//		try (PreparedStatement ps = connection.prepareStatement(ADD_CITY_SQL, Statement.RETURN_GENERATED_KEYS)) {
//			ps.setString(1, city);
//			ps.executeUpdate();
//
//			ResultSet rs = ps.getGeneratedKeys();
//			rs.next();
//
//			return rs.getInt(1);
//		} catch (SQLException e) {
//			// e.printStackTrace();
//			throw new InvalidCityException("Invalid statement", e);
//		}
//	}
	
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
		try (PreparedStatement ps = connection.prepareStatement(GIVE_CITY_SQL)) {
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
	
	@Override
	public Set<String> getCities() throws InvalidPlaceException {
		Set<String> result = new TreeSet<String>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_CITIES_SQL);
			while (set.next()) {
				String name = set.getString("name");
				result.add(name);
			}
			if (result.isEmpty()) {
				throw new InvalidPlaceException("There are no cities.");
			}
				return result;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: " + e.getMessage());
		}
	}
	
	
}
