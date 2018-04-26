package users;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dbconnector.DBConnectionTest;
import exceptions.DBException;
import exceptions.InvalidUserException;

public class UserDAO implements IUserDAO {
	
	private static final String SAVE_USER = "INSERT INTO users(id,email,isMale,firstName,lastName,birthdate,phone,isHost,deleted,password) VALUES (null,?,?,?,?,?,?,?,?,sha(?);";
	private static final String GET_USERS = "SELECT id FROM users;";
	private static final String LOGIN_USER_SQL = "SELECT * FROM users WHERE email=? and password = sha1(?)";
	private static final String USER_FROM_ID_SQL = "SELECT * FROM users WHERE id=?";
	private static final String REGISTER_USER_SQL = "INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false, sha1(?))";
	private static UserDAO instance;
	private static Connection connection;

	private UserDAO() throws DBException {
		try {
			connection = DBConnectionTest.getInstance().getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			throw new DBException("Error! ", e);
		}
	}

	public static UserDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	@Override
	public int login(String email, String password) throws InvalidUserException {
		try (PreparedStatement ps = connection.prepareStatement(LOGIN_USER_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("id");
			}

			throw new InvalidUserException("Wrong email or password, try again!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	@Override
	public int register(User user) throws InvalidUserException {
		try (PreparedStatement ps = connection.prepareStatement(REGISTER_USER_SQL)) {
			ps.setString(1, user.getEmail());
			ps.setBoolean(2, user.isMale());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setDate(5, Date.valueOf(user.getBirthdate()));
			ps.setString(6, user.getPhoneNumber());
			ps.setString(7, user.getPassword());
			// ps.setInt(8, user.getAddress_id());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			saveUser(user);
			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	@Override
	public User userFromId(int user_id) throws InvalidUserException {
		try (PreparedStatement ps = connection.prepareStatement(USER_FROM_ID_SQL)) {
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
				// int address_id = rs.getInt("locations_id");

				return new User(id, email, password, isMale, firstName, lastName, day, month, year, phoneNumber);
			}

			throw new InvalidUserException("There is no user with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidUserException("Invalid statement", e);
		}
	}

	public List<User> getAll() throws SQLException {
		Statement s = connection.createStatement();
		ResultSet set = s.executeQuery(GET_USERS);
		List<User> all = new ArrayList<User>();
		while (set.next()) {
			try {
				all.add(userFromId(set.getInt("id")));
			} catch (InvalidUserException e) {
				e.printStackTrace();
			}
		}
		return all;
	}

	@Override
	public User getUserFromEmailAndPassword(String email, String password) throws InvalidUserException {
		String sql = "SELECT id,name,pass FROM users;";
		Statement s;
		try {
			s = connection.createStatement();
			ResultSet set = s.executeQuery(sql);

			if (set.next()) {
				User user = new User(set.getInt("id"), set.getString("email"), set.getString("password"),
						set.getBoolean("isMale"), set.getString("firstName"), set.getString("lastName"),
						set.getDate("birthdate").toLocalDate().getDayOfMonth(),
						set.getDate("birthdate").toLocalDate().getMonthValue(),
						set.getDate("birthdate").toLocalDate().getYear(), set.getString("phone"));
				return user;
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		throw new InvalidUserException("No user with these email or password!");
	}

	@Override
	public void saveUser(User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(SAVE_USER);
		ps.setString(1, user.getEmail());
		ps.setBoolean(2, user.isMale());
		ps.setString(3, user.getFirstName());
		ps.setString(4, user.getLastName());
		ps.setDate(5, Date.valueOf(user.getBirthdate()));
		ps.setString(6, user.getPhoneNumber());
		ps.setBoolean(7, user.isHost());
		ps.setBoolean(8, user.isDeleted());
		ps.setString(9, user.getPassword());
		ps.executeUpdate();
	}

	
}
