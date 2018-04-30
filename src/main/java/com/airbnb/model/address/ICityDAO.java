package com.airbnb.model.address;

import com.airbnb.exceptions.InvalidCityException;

public interface ICityDAO {
	public int addCity(String city) throws InvalidCityException;
	public int giveCityId(String city)throws InvalidCityException;
	public City cityFromId(int city_id)throws InvalidCityException;
}
