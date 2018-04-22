package places;

import exceptions.InvalidCountryException;

public class Country {
	private static final int POSITIVE = 0;
	private int id;
	private String name;

	public Country(int id, String name) throws InvalidCountryException {
		setId(id);
		setName(name);
	}

	private void setId(int id) throws InvalidCountryException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidCountryException("Invalid id for country.");
		}
	}

	private void setName(String name) throws InvalidCountryException {
		if (name == null || (name.trim().isEmpty())) {
			throw new InvalidCountryException("Empty name.");
		} else {
			this.name = name;
		}
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
}
