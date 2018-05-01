package com.airbnb.model.user;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.place.Place;

public class User {
	private static final int POSITIVE = 0;
	
	private int id;
	private String email;
	private boolean isMale;
	private String firstName;
	private String lastName;
	private String password;
	private LocalDate birthdate;
	private String phoneNumber;
	//private int address_id;
	private boolean isHost;
	private boolean deleted;
	private List<Place> visitedPlaces;
	private List<Place> myPlaces;
	
	public User(String email, String password, boolean isMale) throws InvalidUserException {
		setEmail(email);
		createPassword(password);
		setIsMale(isMale);
	}
	public User(int id, String email, String password, boolean isMale, String firstName, String lastName, int day,
			int month, int year, String phoneNumber)
					throws InvalidUserException {
		this(email,password,isMale);
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setBirthdate(day, month, year);
		changePhoneNumber(phoneNumber);
		//setAddress(address_id);
		this.visitedPlaces = new ArrayList<>();
		this.myPlaces = new ArrayList<>();
	}

	public void addToMyPlaces(int idPlace) {
		if(idPlace > POSITIVE) {
			//TODO method to convert from idPlace to Place object
		}
	}
	
//	Setters
	public void changePhoneNumber(String phoneNumber) throws InvalidUserException {
		if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
			throw new InvalidUserException("Empty phone number.");
		} else {
			if (validatePhoneNumber(phoneNumber)) {
				this.phoneNumber = phoneNumber;
			} else {
				throw new InvalidUserException("Invalid phone number, please enter correct phone number.");
			}
		}
	}

	private void createPassword(String password) throws InvalidUserException {
		if (password == null || password.isEmpty()) {
			throw new InvalidUserException("Empty password");
		} else {
			if (validatePassword(password)) {
				this.password = password;
			} else {
				throw new InvalidUserException(
						"Your password should be at least 8 characters and must contains at least: one diggit, one upper case letter,one lower case letter and one special character(@#$%^&+=).");
			}
		}
	}

	private void setId(int id) throws InvalidUserException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidUserException("Invalid id.");
		}
	}

	private void setEmail(String email) throws InvalidUserException {
		if (validateEmail(email)) {
			this.email = email;
		} else {
			throw new InvalidUserException("Invalid email.");
		}
	}

	private void setIsMale(boolean isMale) {
		this.isMale = isMale;
	}

	private void setFirstName(String firstName) throws InvalidUserException {
		if (validateStringText(firstName)) {
			this.firstName = firstName;
		} else {
			throw new InvalidUserException("Invalid name.");
		}
	}

	private void setLastName(String lastName) throws InvalidUserException {
		if (validateStringText(lastName)) {
			this.lastName = lastName;
		} else {
			throw new InvalidUserException("Invalid name.");
		}
	}

	private void setBirthdate(int day, int month, int year) throws InvalidUserException {
		try {
			LocalDate date = LocalDate.of(year, month, day);
			this.birthdate = date;
		} catch (DateTimeException e) {
			throw new InvalidUserException("Invalid birthdate.");
		}
	}
	
	public void becameAHost() {
		this.isHost = true;
	}

	/*private void setAddress(int address_id)
			throws InvalidUserException {
		if (address_id >= POSITIVE) {
			this.address_id = address_id;
		} else {
			throw new InvalidUserException("Invalid id for address.");
		}
	}*/

	public void deleteAccount() {
		this.deleted = true;
	}

	public void restoreAccount() {
		this.deleted = false;
	}

	// validations

	public static boolean validateEmail(String email) {
		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}

	private boolean validatePhoneNumber(String number) {
		// validate phone numbers of format "1234567890"
		if (number.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (number.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (number.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (number.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;

	}

	private boolean validatePassword(String password) {
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!-_@#$%^&+=])(?=\\S+$).{8,}";
		return password.matches(pattern);
	}

	public static boolean validateStringText(String text) {
		String str = text.trim();
		return ((str != null) && !(str.isEmpty()) && (str.matches("[A-Za-z ]*") && (str.equals(text))));
	}
//	Getters

	public int getId() {
		return this.id;
	}

	public String getEmail() {
		return this.email;
	}

	public boolean isMale() {
		return this.isMale;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getPassword() {
		return this.password;
	}

	public LocalDate getBirthdate() {
		return this.birthdate;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/*public int getAddress_id() {
		return this.address_id;
	}*/

	public boolean isHost() {
		return this.isHost;
	}
	public boolean isDeleted() {
		return this.deleted;
	}
	
}
