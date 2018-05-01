package com.airbnb.model.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class DBProperties {
	public static void main(String[] args) {

		Properties prop = new Properties();

		try (OutputStream output = new FileOutputStream("src/main/resources/config.properties")){

			// set the properties value
			prop.setProperty("DB_NAME", "/airbnbTestDB");
			prop.setProperty("HOST", "localhost");
			prop.setProperty("PORT", "3306");
			prop.setProperty("DB_USERNAME", "root");
			prop.setProperty("DB_PASSWORD", "1234t");

			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} 			

		
	}
}
