package com.airbnb.model.address;

import java.util.Set;

import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.exceptions.InvalidPlaceException;

public interface ICityDAO {
//	public int addCity(String city) throws InvalidCityException;
	public int addCity(City city) throws InvalidCityException;
	public int giveCityId(String city)throws InvalidCityException;
	public City cityFromId(int city_id)throws InvalidCityException;
	Set<String> getCities() throws InvalidPlaceException;
}
