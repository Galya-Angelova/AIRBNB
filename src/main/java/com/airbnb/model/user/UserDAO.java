package com.airbnb.model.user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class UserDAO implements IUserDAO {
//	private static final String CHANGE_PHONE = "UPDATE users SET phone= ? WHERE id =? ";
//	private static final String GET_PASSWORD = "SELECT password FROM users WHERE id = ?";
//	private static final String CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE id = ?";
	private static final String GET_USERS_ID = "SELECT id FROM users;";
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE email=?";
	private static final String USER_FROM_ID_SQL = "SELECT * FROM users WHERE id=?";
	private static final String REGISTER_USER_SQL = "INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false,?)";

	private static final String BECAME_A_HOST = "UPDATE users SET isHost = 1 WHERE id = ?;";
	private static final String UPDATE_USER_PROFIL_SQL = "UPDATE users SET email = ? , firstName = ? , lastName = ? , phone = ? , password = ? WHERE id = ?;";

	// TODO change with DBConnection
	@Autowired
	private DBConnectionTest dbConnection;

	private Connection connection;

	/*
	 * static { try { dbConnection = new DBConnectionTest(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */

	@Autowired
	public UserDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}

	@Override
	public int login(String email, String password) throws InvalidUserException {
		try (PreparedStatement ps = connection.prepareStatement(LOGIN_USER_SQL)) {

			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int id = rs.getInt("id");
				User user = userFromId(id);

				if (BCrypt.checkpw(password, user.getPassword())) {
					return id;
				}
			}

			throw new InvalidUserException("Wrong email or password, try again!");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidUserException("Invalid statement" + e.getMessage(), e);
		}
	}

	@Override
	public int register(User user) throws InvalidUserException {
		PreparedStatement ps = null;
		try {
//			connection.setAutoCommit(false);
			ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getEmail());
			ps.setBoolean(2, user.isMale());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setDate(5, Date.valueOf(user.getBirthdate()));
			ps.setString(6, user.getPhoneNumber());
			String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			ps.setString(7, hashed);
			// ps.setInt(8, user.getAddress_id());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

//			connection.commit();

			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
//			try {
//				connection.rollback();
//			} catch (SQLException e1) {
//				e.printStackTrace();
				throw new InvalidUserException("Invalid statement" + e.getMessage(), e);
//			}
//			System.out.println(e.getMessage());
//			throw new InvalidUserException("Invalid statement, try with another credentials.Reason:" + e.getMessage(),
//					e);
//		} finally {
//			try {
//				connection.setAutoCommit(false);
//				ps.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				throw new InvalidUserException("Invalid statement" + e.getMessage(), e);
//			}
		}
	}

	@Override
	public User userFromId(int user_id) throws InvalidUserException {
		try (PreparedStatement ps = connection.prepareStatement(USER_FROM_ID_SQL)) {
			ps.setInt(1, user_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
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
				// int address_id = rs.getInt("locations_id");

				return new User(id, email, password, isMale, firstName, lastName, day, month, year, phoneNumber);
			}

			throw new InvalidUserException("There is no user with that id!");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	@Override
	public List<Integer> getAllUsersByID() throws SQLException {
		Statement s = connection.createStatement();
		ResultSet set = s.executeQuery(GET_USERS_ID);
		List<Integer> allId = new ArrayList<Integer>();
		while (set.next()) {
			allId.add(set.getInt("id"));
		}
		return allId;
	}

	@Override
	public void becameAHost(int userId) throws InvalidUserException {
		User user = userFromId(userId);
		if (user.isHost()) {
			throw new InvalidUserException("Already a host.");
		}
		user.becameAHost();
		try (PreparedStatement ps = connection.prepareStatement(BECAME_A_HOST)) {
			ps.setInt(1, userId);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidUserException("Invalid statement.", e);
		}
	}

//	@Override
//	public void changePassword(String newPass, String newPassConfirm, int userId) throws InvalidUserException {
//
//		if (User.validatePassword(newPass)) {
//			String hashedPassword = BCrypt.hashpw(newPass, BCrypt.gensalt());
//			if (!BCrypt.checkpw(newPassConfirm, hashedPassword)) {
//				throw new InvalidUserException("Passwords mismatch.");
//			}
//
//			try {
//				this.connection.setAutoCommit(false);
//				PreparedStatement ps = this.connection.prepareStatement(CHANGE_PASSWORD);
//				ps.setString(1, hashedPassword);
//				ps.setInt(2, userId);
//				ps.executeUpdate();
//				this.connection.commit();
//				ps.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				try {
//					this.connection.rollback();
//				} catch (SQLException e1) {
//					throw new InvalidUserException("Invalid credentials," + e1.getMessage());
//				}
//				throw new InvalidUserException(e);
//			} finally {
//				try {
//					this.connection.setAutoCommit(true);
//				} catch (SQLException e) {
//					e.printStackTrace();
//					throw new InvalidUserException("Something went wrong...," + e.getMessage());
//				}
//			}
//		} else {
//			throw new InvalidUserException(
//					"Your password should be at least 8 characters and must contains at least: one diggit, one upper case letter,one lower case letter and one special character(@#$%^&+=).");
//		}
//
//	}


	@Override
	public boolean comparePasswords(int userId, String password) throws InvalidUserException {
		User user = userFromId(userId);
		String passwordInDB = user.getPassword();	
		return BCrypt.checkpw(password, passwordInDB);
	}
	
	@Override
	public void updateProfil(User user) throws InvalidUserException {
		try (PreparedStatement ps =  connection.prepareStatement(UPDATE_USER_PROFIL_SQL)){
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getFirstName());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getPhoneNumber());
			String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
			ps.setString(5, hashed);
			ps.setInt(6, user.getId());
			ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidUserException("Something went wrong...," + e.getMessage());
		}
	}
//
//	public void changePhoneNumber(int userId, String phoneNumber) throws InvalidUserException {
//
//		try (PreparedStatement ps = connection.prepareStatement(CHANGE_PHONE)) {
//			if (!User.validatePhoneNumber(phoneNumber)) {
//				throw new InvalidUserException("Invalid phone number");
//			}
//			ps.setString(1, phoneNumber);
//			ps.setInt(2, userId);
//			ps.executeUpdate();
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//				throw new InvalidUserException("Invalid statement" + e.getMessage(), e);
//		}
//	}

}
