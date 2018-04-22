package users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import dbconnector.DBConnectionTest;
import exceptions.InvalidUserException;

public class UserDAO implements IUserDAO {
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE email=? and password = sha1(?)";
	private static final String USER_FROM_ID_SQL = "SELECT * FROM users WHERE idUser=?";
	private static final String REGISTER_USER_SQL = "INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false, sha1(?), ?)";

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
	public int login(String email, String password) throws InvalidUserException {
		try (PreparedStatement ps = con.prepareStatement(LOGIN_USER_SQL)) {
			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("idUser");
			}

			throw new InvalidUserException("Wrong email or password, try again!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	@Override
	public int register(User user) throws InvalidUserException {
		try (PreparedStatement ps = con.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, user.getEmail());
			ps.setBoolean(2, user.isMale());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setDate(5, java.sql.Date.valueOf(user.getBirthdate()));
			ps.setString(6, user.getPhoneNumber());
			ps.setString(7, user.getPassword());
			ps.setInt(8, user.getAddress_id());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	@Override
	public User userFromId(int user_id) throws InvalidUserException {
		try (PreparedStatement ps = con.prepareStatement(USER_FROM_ID_SQL)) {
			ps.setInt(1, user_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("idUser");
				String email = rs.getString("email");
				boolean isMale = rs.getBoolean("isMale");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String password = rs.getString("password");
				LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
				int day = birthdate.getDayOfMonth();
				int month = birthdate.getMonthValue();
				int year = birthdate.getYear();
				String phoneNumber = rs.getString("phone");
				int address_id = rs.getInt("locations_id");

				return new User(id, email, password, isMale, firstName, lastName, day, month, year, phoneNumber,
						address_id);
			}

			throw new InvalidUserException("There is no user with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

}
