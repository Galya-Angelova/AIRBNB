package com.airbnb.model.place;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.airbnb.exceptions.InvalidPlaceException;

public class PlaceDTO {
	private static final int POSITIVE = 0;
	
	private int id;
	private String name;
	private String placeTypeName;
	private boolean busied;
	private String country;
	private String city;
	private String street;
	private int streetNumber;
	private double price;
	private LocalDate dateOfPosting;
	private List<String> photosURLs;
	
	public PlaceDTO(int id, String name, String placeTypeName, boolean busied, String country, String city,
			String street, int streetNumber,double price,LocalDate date) throws InvalidPlaceException {
		setId(id);
		setName(name);
		setPlaceTypeName(placeTypeName);
		setBusied(busied);
		setCountry(country);
		setCity(city);
		setStreet(street);
		setStreetNumber(streetNumber);
		setPrice(price);
		this.dateOfPosting =date;
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
	
	public void setPlaceTypeName(String placeTypeName) throws InvalidPlaceException {
		if (placeTypeName == null || (placeTypeName.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty place type.");
		} else {
			this.placeTypeName = placeTypeName;
		}
	}
	
	public void setBusied(boolean busied) {
		this.busied = busied;
	}
	
	public void setCountry(String country) throws InvalidPlaceException {
		if (country == null || (country.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty country.");
		} else {
			this.country = country;
		}
	}
	
	public void setCity(String city) throws InvalidPlaceException {
		if (city == null || (city.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty city.");
		} else {
			this.city = city;
		}
	}
	
	public void setStreet(String street) throws InvalidPlaceException {
		if (street == null || (street.trim().isEmpty())) {
			throw new InvalidPlaceException("Empty street.");
		} else {
			this.street = street;
		}
	}
	
	public void setStreetNumber(int streetNumber) throws InvalidPlaceException {
		if (streetNumber >= POSITIVE) {
			this.streetNumber = streetNumber;
		} else {
			throw new InvalidPlaceException("Invalid street number.");
		}
	}
	
	public void setPrice(double price) throws InvalidPlaceException {
		if (price >= POSITIVE) {
			this.price = price;
		} else {
			throw new InvalidPlaceException("Invalid id for place.");
		}
	}
	
//	Getters
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getPlaceTypeName() {
		return this.placeTypeName;
	}
	
	public boolean isBusied() {
		return this.busied;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getCity() {
		return this.city;
	}
	
	public String getStreet() {
		return this.street;
	}
	
	public int getStreetNumber() {
		return this.streetNumber;
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
	public String getDateOfPosting() {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
		 String result = formatter.format(this.dateOfPosting);
		 return result;
		/*String month = this.dateOfPosting.getMonth().name(); // will give the full name of the month
		int day = this.dateOfPosting.getDayOfMonth();
		int year = this.dateOfPosting.getYear();
		StringBuilder sb = new StringBuilder();
		sb.append(day);
		sb.append(" ");
		sb.append(month);
		sb.append(" ");
		sb.append(year);
		return sb.toString();*/
	}
	
	public static LocalDate convertFromStringToLocalDate(String date) {
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
		 LocalDate localDate = LocalDate.parse(date, formatter);//2016-08-16
		 String result = formatter.format(localDate);//16-Aug-2016
		 return localDate;
	}
}
