package com.airbnb.model.place;

import java.util.ArrayList;
import java.util.List;

public class PlaceDTO {
	private int id;
	private String name;
	private String placeTypeName;
	private boolean busied;
	private String country;
	private String city;
	private String street;
	private int streetNumber;
	private double price;
	private List<String> photosURLs;
	
	public PlaceDTO(int id, String name, String placeTypeName, boolean busied, String country, String city,
			String street, int streetNumber,double price) {
		this.id = id;
		this.name = name;
		this.placeTypeName = placeTypeName;
		this.busied = busied;
		this.country = country;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		this.setPhotosURLs(new ArrayList<>());
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlaceTypeName() {
		return placeTypeName;
	}
	public void setPlaceTypeName(String placeTypeName) {
		this.placeTypeName = placeTypeName;
	}
	public boolean isBusied() {
		return busied;
	}
	public void setBusied(boolean busied) {
		this.busied = busied;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return price;
	}
	public List<String> getPhotosURLs() {
		return photosURLs;
	}
	public void setPhotosURLs(List<String> photosURLs) {
		this.photosURLs = photosURLs;
	}
}
