package com.airbnb.model.place;

import com.airbnb.exceptions.InvalidPlaceException;

public interface IPlaceDAO {

	public int createPlace(String streetName, String countryName, String placeTypeName, String userEmail) throws InvalidPlaceException;
	public Place placeFromId(int placeId) throws InvalidPlaceException;

}
