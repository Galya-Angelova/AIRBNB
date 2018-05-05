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
	private static final String GET_USERS_ID = "SELECT id FROM users;";
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE email=?";
	private static final String USER_FROM_ID_SQL = "SELECT * FROM users WHERE id=?";
	private static final String REGISTER_USER_SQL = "INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false,?)";

	private static final String BECOME_A_HOST = "UPDATE users SET isHost = 1 WHERE id = ?;";
	private static final String UPDATE_USER_PROFIL_SQL = "UPDATE users SET email = ? , firstName = ? , lastName = ? , phone = ? , password = ? WHERE id = ?;";
	private static final String USER_PLACES_SQL="SELECT id FROM place WHERE user_id=?;";
	private static final String USER_VISITED_PLACES_SQL="SELECT place_id FROM reservation WHERE user_id=?;";

	// TODO change with DBConnection
	@Autowired
	private DBConnectionTest dbConnection;

	private Connection connection;

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
		try (PreparedStatement ps = connection.prepareStatement(REGISTER_USER_SQL, Statement.RETURN_GENERATED_KEYS) ){
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

			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
				throw new InvalidUserException("Invalid statement" + e.getMessage(), e);
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
				boolean isHost=rs.getBoolean("isHost");
				return new User(id, email, password, isMale, firstName, lastName, day, month, year, phoneNumber,isHost,this.getVisitedPlacesByUserId(user_id),this.getUserPlacesByUserId(user_id));
			}

			throw new InvalidUserException("There is no user with that id!");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}
	@Override
	public List<Integer> getVisitedPlacesByUserId(int user_id) throws InvalidUserException  {
		List<Integer> resault=new ArrayList<Integer>();
		try (PreparedStatement ps = connection.prepareStatement(USER_VISITED_PLACES_SQL)) {
			ps.setInt(1, user_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				resault.add(rs.getInt("place_id"));
			}
			
			return resault;	
			} catch (SQLException e) {
				e.printStackTrace();
				throw new InvalidUserException("Invalid statement", e);
			}
	}
	
	@Override
	public List<Integer> getUserPlacesByUserId(int user_id) throws InvalidUserException  {
		List<Integer> resault=new ArrayList<Integer>();
		try (PreparedStatement ps = connection.prepareStatement(USER_PLACES_SQL)) {
			ps.setInt(1, user_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				resault.add(rs.getInt("id"));
			}
			
			return resault;	
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
	public void becomeAHost(User user) throws InvalidUserException {
		if (user.getIsHost()) {
			throw new InvalidUserException("Already a host.");
		}
		user.becomeAHost();
		try (PreparedStatement ps = connection.prepareStatement(BECOME_A_HOST)) {
			ps.setInt(1, user.getId());
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new InvalidUserException("Invalid statement.", e);
		}
	}

	@Override
	public boolean comparePasswords(int userId, String password) throws InvalidUserException {
		User user = userFromId(userId);
		String passwordInDB = user.getPassword();	
		return BCrypt.checkpw(password, passwordInDB);
	}
	
	@Override
	public void updateProfile(User user) throws InvalidUserException {
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
	
	@Override
	public void addUserPlaceToUser(int placeID, User user) throws InvalidUserException{
		user.addToMyPlaces(placeID);
		this.becomeAHost(user);
	}
}
