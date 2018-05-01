package com.airbnb.model.place;

import java.util.List;

import com.airbnb.exceptions.InvalidPlaceException;

public class Place {
//	 enum PlaceType {
//		HOUSE(1,"house"), ONE_ROOM(2,"one room"), TWO_ROOMS(3,"two rooms"), STUDIO(4,"studio");
//			
//			private String name;
//			private int typeId;
//			
//			PlaceType(int id,String name) {
//				this.typeId = id;
//				if(name != null) {
//					this.name = name;
//				}
//			}
//			
//			public static PlaceType fromString(String text) {
//				for (PlaceType place : PlaceType.values()) {
//					if (place.name.equalsIgnoreCase(text)) {
//						return place;
//					}
//				}
//				return null;
//			}
//			public String getName() {
//				return this.name;
//			}
//			public int getId() {
//				return this.typeId;
//			}
//			
//		}
	 
	 enum PlaceType {
			HOUSE("house"), ONE_ROOM("one room"), TWO_ROOMS("two rooms"), STUDIO("studio");
				
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
	private List<String> photoes;
	
	public Place(int id, String name, boolean busied, int addressID ,String placeTypeName, int ownerId) throws InvalidPlaceException {
		setId(id);
		setName(name);
		setBusied(busied);
		setAddressID(addressID);
		setPlaceType(placeTypeName);
		this.ownerId = ownerId;
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
	
	public int getOwnerId() {
		return ownerId;
	}
	
}
