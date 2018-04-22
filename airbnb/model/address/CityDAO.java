package address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dbconnector.DBConnectionTest;
import exceptions.InvalidCityException;

public class CityDAO implements ICityDAO {
	private static final String ADD_CITY_SQL = "INSERT INTO cities VALUES (null, ?)";
	private static final String GIVE_COUNTRY_SQL = "SELECT * FROM cities WHERE name= ?";
	private static final String CITY_FROM_ID_SQL = "SELECT * FROM cities WHERE id=?";

	private static Connection con;

	static {
		try {
			con = DBConnectionTest.getInstance().getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			// e.printStackTrace();
			System.out.println("Something went wrong with the database");
		}
	}

	@Override
	public int addCity(City city) throws InvalidCityException {
		try (PreparedStatement ps = con.prepareStatement(ADD_CITY_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
		try (PreparedStatement ps = con.prepareStatement(GIVE_COUNTRY_SQL)) {
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
		try (PreparedStatement ps = con.prepareStatement(CITY_FROM_ID_SQL)) {
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
