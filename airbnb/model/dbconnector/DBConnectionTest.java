package dbconnector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionTest {
	private static final String DB_NAME = "/airbnbtestdb";
	private static final String HOST = "localhost";
	private static final String PORT = "3306";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "Kp.9205056346";
	private static DBConnectionTest instance = null;
	private Connection connection;

	private DBConnectionTest() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + DB_NAME, DB_USERNAME,
				DB_PASSWORD);
	}

	public static synchronized DBConnectionTest getInstance() throws ClassNotFoundException, SQLException {
		if (instance == null) {
			instance = new DBConnectionTest();
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}
}
