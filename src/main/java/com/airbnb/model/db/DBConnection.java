package com.airbnb.model.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.airbnb.exceptions.DBException;
@Component
public class DBConnection {
	private static final String DB_NAME;
	private static final String HOST;
	private static final String PORT;
	private static final String DB_USERNAME;
	private static final String DB_PASSWORD;
	private Connection connection;

	static {
ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		
		Properties prop = new Properties();
		try (InputStream input = classLoader.getResourceAsStream("airbnbDB.properties");) {
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

	public DBConnection() throws DBException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DBException("Driver not loaded.",e);
		}
		try {
			
			connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + DB_NAME, DB_USERNAME,
					DB_PASSWORD);
		} catch (SQLException e) {
			throw new DBException("Wrong credentials",e);
		}
		
	}
	public Connection getConnection() {
		return connection;
	}
}
