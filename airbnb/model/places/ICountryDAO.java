package places;

import exceptions.InvalidCountryException;

public interface ICountryDAO {
	
	public int addCountry(Country country) throws InvalidCountryException;
	public int giveCountryId(String country)throws InvalidCountryException;
	public Country countryFromId(int country_id)throws InvalidCountryException;
}
