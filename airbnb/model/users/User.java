package users;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.InvalidArgumentException;
import places.Address;
import places.Place;

public class User {
	private static final int POSITIVE = 0;
	private long id;
	private String email;
	private boolean isMale;
	private String firstName;
	private String lastName;
	private String password;
	private LocalDate birthdate;
	private String phoneNumber;
	private Address address;
	private boolean isHost;
	private boolean deleted;
	private List<Place> visitedPlaces;
	private List<Place> myPlaces;

	public User(long id, String email,String password, boolean isMale, String firstName, String lastName, int day, int month, int year,
			String phoneNumber, String country, String city, String street, int streetNumber)
			throws InvalidArgumentException {
		setId(id);
		setEmail(email);
		createPassword(password);
		setIsMale(isMale);
		setFirstName(firstName);
		setLastName(lastName);
		setBirthdate(day, month, year);
		changePhoneNumber(phoneNumber);
		setAddress(country, city, street, streetNumber);
		this.visitedPlaces = new ArrayList<>();
		this.myPlaces = new ArrayList<>();
	}

	public void changePhoneNumber(String phoneNumber) throws InvalidArgumentException {
		if (phoneNumber == null || phoneNumber.trim().length() == POSITIVE) {
			throw new InvalidArgumentException("Empty phone number.");
		} else {
			if (validatePhoneNumber(phoneNumber)) {
				this.phoneNumber = phoneNumber;
			} else {
				throw new InvalidArgumentException("Invalid phone number, please enter correct phone number.");
			}
		}
	}

	private void createPassword(String password) throws InvalidArgumentException {
		if (password.isEmpty() || password == null) {
			throw new InvalidArgumentException("Empty password");
		} else {
			if (validatePassword(password)) {
				this.password = password;
			} else {
				throw new InvalidArgumentException(
						"Your password should be at least 8 characters, one Upper case letter and one Lower case and contains at least one character.");
			}
		}
	}

	private void setId(long id) throws InvalidArgumentException {
		if (id > POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidArgumentException("Invalid id for address.");
		}
	}

	private void setEmail(String email) throws InvalidArgumentException {
		if (validateEmail(email)) {
			this.email = email;
		} else {
			throw new InvalidArgumentException("Invalid email.");
		}
	}

	private void setIsMale(boolean isMale) {
		this.isMale = isMale;
	}

	private void setFirstName(String firstName) throws InvalidArgumentException {
		if (validateStringText(firstName)) {
			this.firstName = firstName;
		} else {
			throw new InvalidArgumentException("Invalid name.");
		}
	}

	private void setLastName(String lastName) throws InvalidArgumentException {
		if (validateStringText(lastName)) {
			this.firstName = lastName;
		} else {
			throw new InvalidArgumentException("Invalid name.");
		}
	}

	private void setBirthdate(int day, int month, int year) throws InvalidArgumentException {
		try {
			LocalDate date = LocalDate.of(year, month, day);
			this.birthdate = date;
		} catch (DateTimeException e) {
			throw new InvalidArgumentException("Invalid birthdate.");
		}
	}

	private void setAddress(String country, String city, String street, int streetNumber)
			throws InvalidArgumentException {
		this.address = new Address(0, country, city, street, streetNumber);
	}

	public void deleteAccount() {
		this.deleted = true;
	}

	// validations
	private boolean validateStringText(String text) {
		String str = text.trim();
		return (!(str.isEmpty()) && (str != null) && (str.matches("[A-Za-z ]*") && (str.equals(text))));
	}

	private boolean validateEmail(String email) {
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
}
