package places;

import exceptions.InvalidCityException;

public class City {
	private static final int POSITIVE = 0;
	private int id;
	private String name;

	public City(int id, String name) throws InvalidCityException {
		setId(id);
		setName(name);
	}

	private void setId(int id) throws InvalidCityException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidCityException("Invalid id for country.");
		}
	}

	private void setName(String name) throws InvalidCityException {
		if (name == null || (name.trim().isEmpty())) {
			throw new InvalidCityException("Empty name.");
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
