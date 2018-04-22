package address;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dbconnector.DBConnectionTest;
import exceptions.InvalidAddressException;

public class AddressDAO implements IAddressDAO {
	private static final String ADDRESS_FROM_ID_SQL = "SELECT * FROM locations WHERE id=?";
	private static final String ADD_ADDRESS_SQL = "INSERT INTO locations VALUES (null, ?, ?, ?, ?)";

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
	public int addAddress(Address address) throws InvalidAddressException {
		try (PreparedStatement ps = con.prepareStatement(ADD_ADDRESS_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
	public Address addressFromId(int address_id) throws InvalidAddressException {
		try (PreparedStatement ps = con.prepareStatement(ADDRESS_FROM_ID_SQL)) {
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