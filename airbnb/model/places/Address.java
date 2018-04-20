package places;

import exceptions.InvalidArgumentException;

public class Address {
	private static final int POSITIVE = 0;
	private long id;
	private String country;
	private String city;
	private String street;
	private int streetNumber;
	
	public Address(long id, String country, String city, String street, int streetNumber) throws InvalidArgumentException {
		setId(id);
		setCountry(country);
		setCity(city);
		setStreet(street);
		setStreetNumber(streetNumber);
	}

	
	private void setId(long id) throws InvalidArgumentException{
		if(id > POSITIVE) {
			this.id = id;
		}else {
			throw new InvalidArgumentException("Invalid id for address.");
		}
	}


	private void setCountry(String country) throws InvalidArgumentException{
		if((country.isEmpty()) || country == null) {
			throw new InvalidArgumentException("Empty country.");
		}else {
			this.country = country;
		}
	}


	private void setCity(String city) throws InvalidArgumentException{
		if((city.isEmpty()) || city == null) {
			throw new InvalidArgumentException("Empty city.");
		}else {
			this.city = city;
		}
	}


	private void setStreet(String street) throws InvalidArgumentException{
		if((street.isEmpty()) || street == null) {
			throw new InvalidArgumentException("Empty street.");
		}else {
			this.street = street;
		}
	}


	private void setStreetNumber(int streetNumber) throws InvalidArgumentException{
		if(streetNumber > POSITIVE) {
			this.streetNumber = streetNumber;
		}else {
			throw new InvalidArgumentException("Invalid street number.");
		}
	}


	public long getId() {
		return this.id;
	}

	public String getCountry() {
		return this.country;
	}

	public String getCity() {
		return this.city;
	}

	public String getStreet() {
		return this.street;
	}

	public int getStreetNumber() {
		return this.streetNumber;
	}
}
