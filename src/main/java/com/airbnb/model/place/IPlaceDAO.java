package com.airbnb.model.place;

import java.util.List;

import com.airbnb.exceptions.InvalidPlaceException;

public interface IPlaceDAO {

	public int createPlace(String name,String streetName,int streetNumber,String city, String countryName, String placeTypeName, String userEmail) throws InvalidPlaceException;
	public Place placeFromId(int placeId) throws InvalidPlaceException;
	public List<String> getAllPlaceTypes() throws InvalidPlaceException;
}
