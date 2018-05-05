package com.airbnb.model.address;

import com.airbnb.exceptions.InvalidAddressException;

public class Address {
	private static final int POSITIVE = 0;
	
	private int id;
	private int country_id;
	private int city_id;
	private String street;
	private int streetNumber;
	private City city;
	private Country country;
	
	public Address(int id, int country_id, int city_id, String street, int streetNumber,City city,Country country)
			throws InvalidAddressException {
		setId(id);
		setCountryID(country_id);
		setCityID(city_id);
		setStreet(street);
		setStreetNumber(streetNumber);
		setCity(city);
		setCountry(country);
	}
	
//	Setters
	private void setId(int id) throws InvalidAddressException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidAddressException("Invalid id for address.");
		}
	}

	private void setCountryID(int country_id) throws InvalidAddressException {
		if (country_id  <=POSITIVE) {
			throw new InvalidAddressException("Empty country.");
		} else {
			this.country_id = country_id;
		}
	}

	private void setCityID(int city_id) throws InvalidAddressException {
		if (city_id <=POSITIVE) {
			throw new InvalidAddressException("Empty city.");
		} else {
			this.city_id = city_id;
		}
	}

	private void setStreet(String street) throws InvalidAddressException {
		if (street == null || (street.trim().isEmpty())) {
			throw new InvalidAddressException("Empty street.");
		} else {
			this.street = street;
		}
	}

	private void setStreetNumber(int streetNumber) throws InvalidAddressException {
		if (streetNumber > POSITIVE) {
			this.streetNumber = streetNumber;
		} else {
			throw new InvalidAddressException("Invalid street number.");
		}
	}

	private void setCity(City city) {
		this.city = city;
	}
	private void setCountry(Country country) {
		this.country = country;
	}
	
//	Getters
	public int getId() {
		return this.id;
	}

	public int getCountryId() {
		return this.country_id;
	}

	public int getCityId() {
		return this.city_id;
	}

	public String getStreet() {
		return this.street;
	}

	public int getStreetNumber() {
		return this.streetNumber;
	}
	public City getCity() {
		return this.city;
	}
	public Country getCountry() {
		return this.country;
	}
	
}
