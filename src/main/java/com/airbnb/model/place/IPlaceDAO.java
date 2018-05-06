package com.airbnb.model.place;

import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.place.Place.PlaceType;

public interface IPlaceDAO {

//	public int createPlace(String name,String streetName,int streetNumber,String city, String countryName, String placeTypeName, String userEmail) throws InvalidPlaceException;
	public int addPlace(Place placel) throws InvalidPlaceException;
	public int addPlaceType(PlaceType placeType) throws InvalidPlaceException;
	public int givePlaceTypeId(String placeType) throws InvalidPlaceException;
	public String placeTypeFromId(int placeType_id) throws InvalidPlaceException;
	
	
	public Place placeFromId(int placeId) throws InvalidPlaceException;
	public List<String> getAllPlaceTypes() throws InvalidPlaceException;
	public PlaceSearchInfo getDefaultFilter() throws InvalidPlaceException;

	public Set<PlaceDTO> getAllPlaces() throws InvalidPlaceException;
	public List<PlaceDTO> getFilteredPlaces(PlaceSearchInfo filter) throws InvalidPlaceException;
//	public List<Place> getAllPlacesForSearch() throws InvalidPlaceException;
	public void getAllPlacesFromDB() throws InvalidPlaceException;
	//public String saveImageURL(MultipartFile file, int placeId) throws InvalidPlaceException;
	public List<PlaceDTO> gettAllPlacesForUser(int userId) throws InvalidPlaceException;
	public void addPhotoToPlace(PlaceDTO place) throws InvalidPlaceException;
	
}
