package com.airbnb.model.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;
@Component
public class DBConnection {
	private static final String DB_NAME;
	private static final String HOST;
	private static final String PORT;
	private static final String DB_USERNAME;
	private static final String DB_PASSWORD;
	private Connection connection;

	static {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("src/main/resources/airbnbDB.properties")) {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DB_NAME = prop.getProperty("DB_NAME");
		HOST = prop.getProperty("HOST");
		PORT = prop.getProperty("PORT");
		DB_USERNAME = prop.getProperty("DB_USERNAME");
		DB_PASSWORD = prop.getProperty("DB_PASSWORD");
	}

	private DBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + DB_NAME, DB_USERNAME,
				DB_PASSWORD);
	}

	public Connection getConnection() {
		return connection;
	}
}
