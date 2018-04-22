package address;

import exceptions.InvalidAddressException;

public class Address {
	private static final int POSITIVE = 0;
	private int id;
	private int country_id;
	private int city_id;
	private String street;
	private int streetNumber;

	public Address(int id, int country_id, int city_id, String street, int streetNumber)
			throws InvalidAddressException {
		setId(id);
		setCountry(country_id);
		setCity(city_id);
		setStreet(street);
		setStreetNumber(streetNumber);
	}

	private void setId(int id) throws InvalidAddressException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidAddressException("Invalid id for address.");
		}
	}

	private void setCountry(int country_id) throws InvalidAddressException {
		if (country_id  <=0) {
			throw new InvalidAddressException("Empty country.");
		} else {
			this.country_id = country_id;
		}
	}

	private void setCity(int city_id) throws InvalidAddressException {
		if (city_id <=0) {
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

	public int getId() {
		return this.id;
	}

	public int getCountry() {
		return this.country_id;
	}

	public int getCity() {
		return this.city_id;
	}

	public String getStreet() {
		return this.street;
	}

	public int getStreetNumber() {
		return this.streetNumber;
	}
}
