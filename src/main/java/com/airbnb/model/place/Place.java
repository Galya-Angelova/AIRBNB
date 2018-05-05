package com.airbnb.model.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.address.Address;
import org.springframework.web.multipart.MultipartFile;
public class Place {

	public enum PlaceType {
		HOUSE("House"), ONE_ROOM("One room"), TWO_ROOMS("Two rooms"), STUDIO("Studio");

		private String name;

		PlaceType(String name) {
			if (name != null) {
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
	private Address address;
	private PlaceType placeType;
	private int ownerId;
	private double price;
	private List<String> photosURLs;
	private String photoSrc;
	private MultipartFile photo;
	
	public Place() {

	}

	public Place(int id, String name, boolean busied, int addressID, String placeTypeName, int ownerId, double price,Address address)
			throws InvalidPlaceException {
		setId(id);
		setName(name);
		setBusied(busied);
		setAddressID(addressID);
		setPlaceType(placeTypeName);
		setPrice(price);
		setOwnerId(ownerId);
		setAddress(address);
		this.photosURLs = new ArrayList<>();
	}

	// Setters
	public void setId(int id) throws InvalidPlaceException {
		if (id >= POSITIVE) {
			this.id = id;
		} else {
			throw new InvalidPlaceException("Invalid id for place.");
		}
	}

	private void setName(String name) throws InvalidPlaceException {
		if (name == null || (name.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty name.");
		} else {
			this.name = name;
		}
	}

	private void setBusied(boolean busied) {
		this.busied = busied;
	}

	private void setAddressID(int addressID) throws InvalidPlaceException {
		if (addressID >= POSITIVE) {
			this.addressID = addressID;
		} else {
			throw new InvalidPlaceException("Invalid id for place's address.");
		}
	}

	private void setPlaceType(String name) throws InvalidPlaceException {
		if (name != null) {
			this.placeType = PlaceType.fromString(name);
		} else {
			throw new InvalidPlaceException("Invalid place type name.");
		}
	}

	private void setOwnerId(int ownerId) throws InvalidPlaceException {
		if (ownerId >= POSITIVE) {
			this.ownerId = ownerId;
		} else {
			throw new InvalidPlaceException("Invalid id for place' owner.");
		}
	}

	/*public void setPhotoSrc(String photoSrc) {
		if(photoSrc != null) {
			this.photosURLs.add(photoSrc);
		}
	}	*/
	private void setPhotosUrls(List<String> photosURLs) {
		this.photosURLs = new ArrayList<String>(photosURLs);
	}

	private void setPrice(double price) throws InvalidPlaceException {
		if (price >= POSITIVE) {
			this.price = price;
		} else {
			throw new InvalidPlaceException("Invalid price for place.");
		}
	}

	private void setAddress(Address address) throws InvalidPlaceException {
		if(address != null) {
			this.address = address;
		}else {
			throw new InvalidPlaceException("Invalid address.");
		}
	}
	
	public void setPhoto(MultipartFile file) {
		this.photo = file;
	}
	
	// Getters
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

	public PlaceType getPlaceType() {
		return this.placeType;
	}

	public String getPlaceTypeName() {
		return this.placeType.name;
	}

	// public int getPlaceTypeID() {
	// return this.placeType.typeId;
	// }
	public String getPhotoSrc() {
		return photoSrc;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public Address getAddress() {
		return this.address;
	}
	public double getPrice() {
		return this.price;
	}

	public List<String> getPhotosURLs() {
		return photosURLs;
	}

	public void setPhotosURLs(List<String> photosURLs) {
		this.photosURLs = photosURLs;
	}
}
