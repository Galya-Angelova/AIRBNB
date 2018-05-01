package com.airbnb.model.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.stereotype.Component;

import com.airbnb.exceptions.DBException;

@Component
public class DBConnectionTest {

	private static final String DB_NAME = "/airbnbtestdb";
	private static final String HOST = "localhost";
	private static final String PORT = "3306";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "Kp.9205056346";
	private Connection connection;

	

/*
	static {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream("config.properties")) {
			prop.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HOST = prop.getProperty("HOST");
		DB_NAME = prop.getProperty("DB_NAME");
		DB_USERNAME = prop.getProperty("DB_USERNAME");
		DB_PASSWORD = prop.getProperty("DB_PASSWORD");
		PORT = prop.getProperty("PORT");
	}*/

	public DBConnectionTest() throws DBException {
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
