package com.airbnb.model.email;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Properties;

public class MailProperties {
	public static void main(String[] args) {

		Properties prop = new Properties();

		try (OutputStream output = new FileOutputStream("src/main/resources/mail.properties")){

			// set the properties value
			prop.setProperty("sender", "kaloianpavlov92@gmail.com");
			prop.setProperty("password", "9205056346");
			
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} 			

	}
}
