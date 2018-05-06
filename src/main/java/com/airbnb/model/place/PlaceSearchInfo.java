package com.airbnb.model.place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.airbnb.exceptions.InvalidPlaceException;

public class PlaceSearchInfo {
	private static final String[] ORDERED_OPTIONS = {
			"name", "price", "city", "placeType" };

	private String placeName;
	private String city;
	private List<String> placeTypes = new ArrayList<>();
	private double minPriceForNight;
	private double maxPriceForNight;
	private boolean isAscending;
	private String orderedBy;

	public PlaceSearchInfo() {
	}

	public PlaceSearchInfo(String placeName, String city, List<String> placeTypes, double minPriceForNight,
			double maxPriceForNight, boolean isAscending, String orderedBy) throws InvalidPlaceException {
		setPlaceName(placeName);
		setCity(city);
		setPlaceTypes(placeTypes);
		setMinPriceForNight(minPriceForNight);
		setMaxPriceForNight(maxPriceForNight);
		setIsAscending(isAscending);
	}

	// Setters
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public void setCity(String city) throws InvalidPlaceException {
		this.city = city;
	}

	public void setPlaceTypes(List<String> placeTypes) throws InvalidPlaceException {
		if (placeTypes == null) {
			this.placeTypes = new ArrayList<String>();
		} else {
			this.placeTypes = placeTypes;
		}
	}

	public void setMinPriceForNight(double minPriceForNight) throws InvalidPlaceException {
		if (minPriceForNight >= 0.0) {
			this.minPriceForNight = minPriceForNight;
		} else {
			throw new InvalidPlaceException("Invalid min price");
		}
	}

	public void setMaxPriceForNight(double maxPriceForNight) throws InvalidPlaceException {
		if (maxPriceForNight >= 0.0) {
			this.maxPriceForNight = maxPriceForNight;
		} else {
			throw new InvalidPlaceException("Invalid max price");
		}
	}

	public void setIsAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

	public void setOrderedBy(String orderedBy) throws InvalidPlaceException {
		if ((orderedBy != null && (!orderedBy.trim().isEmpty()))
				&& (Arrays.asList(ORDERED_OPTIONS)).contains(orderedBy)) {
			this.orderedBy = orderedBy;
		} else {
			throw new InvalidPlaceException("Invalid order parameter");
		}
	}

	// Getters
	public String getPlaceName() {
		return this.placeName;
	}

	public String getCity() {
		return this.city;
	}

	public List<String> getPlaceTypes() {
		return Collections.unmodifiableList(this.placeTypes);
	}

	public double getMinPriceForNight() {
		return this.minPriceForNight;
	}

	public double getMaxPriceForNight() {
		return this.maxPriceForNight;
	}

	public boolean getIsAscending() {
		return this.isAscending;
	}

	public String getOrderedBy() {
		return this.orderedBy;
	}

}
