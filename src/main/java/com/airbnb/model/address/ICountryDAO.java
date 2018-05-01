package com.airbnb.model.address;

import com.airbnb.exceptions.InvalidCountryException;

public interface ICountryDAO {
//	public int addCountry(String country) throws InvalidCountryException;
	public int addCountry(Country country) throws InvalidCountryException;
	public int giveCountryId(String country)throws InvalidCountryException;
	public Country countryFromId(int country_id)throws InvalidCountryException;
}
