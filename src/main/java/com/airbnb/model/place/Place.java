package com.airbnb.model.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.airbnb.exceptions.InvalidPlaceException;

public class Place {
	 
	 public enum PlaceType {
			HOUSE("House"), ONE_ROOM("One room"), TWO_ROOMS("Two rooms"), STUDIO("Studio");
				
				private String name;
				
				PlaceType(String name) {
					if(name != null) {
						this.name = name;
					}
				}
				
				public static PlaceType fromString(String text) {
					for (PlaceType place : PlaceType.values()) {
						if (place.name.equalsIgnoreCase(text)) {
							return place;
						}
					}
					return null;
				}
				public String getName() {
					return this.name;
				}
			}
	private static final int POSITIVE = 0;
	
	private int id;
	private String name;
	private boolean busied;
	private int addressID;
	private PlaceType placeType;
	private int ownerId;
	private List<String> photosURLs;
	
	
	public Place() {
		
	}
	public Place(int id, String name, boolean busied, int addressID ,String placeTypeName, int ownerId) throws InvalidPlaceException {
		setId(id);
		setName(name);
		setBusied(busied);
		setAddressID(addressID);
		setPlaceType(placeTypeName);
		this.ownerId = ownerId;
		this.photosURLs = new ArrayList<>();
	}

//	Setters
	public void setId(int id) throws InvalidPlaceException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidPlaceException("Invalid id for place.");
		}
	}
	
	public void setName(String name) throws InvalidPlaceException {
		if (name == null || (name.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty name.");
		} else {
			this.name = name;
		}
	}
	
	public void setBusied(boolean busied) {
		this.busied = busied;
	}
	
	public void setAddressID(int addressID) throws InvalidPlaceException {
		if (addressID >= POSITIVE) {
			this.addressID = addressID;
		} else {
			throw new InvalidPlaceException("Invalid id for place's address.");
		}
	}
	
	public void setPlaceType(String name) throws InvalidPlaceException {
		if(name!=null){
		this.placeType = PlaceType.fromString(name);
		}else{
			throw new InvalidPlaceException("Invalid place type name.");
		}
	}
	
	public void setOwnerId(int ownerId) throws InvalidPlaceException {
		if (ownerId >= POSITIVE) {
			this.ownerId = ownerId;
		} else {
			throw new InvalidPlaceException("Invalid id for place' owner.");
		}
	}
	
	
//	Getters
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isBusied() {
		return busied;
	}
	
	public int getAddressID() {
		return addressID;
	}
	
	public PlaceType getPlaceType(){
		return this.placeType;
	}
	
	public String getPlaceTypeName(){
		return this.placeType.name;
	}
	
//	public int getPlaceTypeID() {
//	return this.placeType.typeId;
//}
	public List<String> getPhotosUrls() {
		return Collections.unmodifiableList(this.photosURLs);
	}

	public void setPhotosUrls(List<String> photosURLs) {
		this.photosURLs = new ArrayList<String> (photosURLs);
	}
	
	public int getOwnerId() {
		return ownerId;
	}
	
}
