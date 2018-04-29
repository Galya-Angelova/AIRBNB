package com.airbnb.model.place;

public class Place {
	 enum PlaceType {
		HOUSE(1,"house"), ONE_ROOM(2,"one room"), TWO_ROOMS(3,"two rooms"), STUDIO(4,"studio");
			
			private String name;
			private int typeId;
			
			PlaceType(int id,String name) {
				this.typeId = id;
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
			public int getId() {
				return this.typeId;
			}
			
		}
	
	private int id;
	private String name;
	private boolean busied;
	private int locationID;
	private PlaceType placeType;
	private int ownerId;
	
	public Place(int id, String name, boolean busied, int locationID,String placeTypeName, int ownerId) {
		this.id = id;
		this.name = name;
		this.busied = busied;
		this.locationID = locationID;
		setPlaceType(placeTypeName);
		this.ownerId = ownerId;
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

	public boolean isBusied() {
		return busied;
	}

	public void setBusied(boolean busied) {
		this.busied = busied;
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	
	public void setPlaceType(String name) {
		this.placeType = PlaceType.fromString(name);
	}
	
	public String getPlaceTypeName(){
		return this.placeType.name;
	}
	public int getPlaceTypeID() {
		return this.placeType.typeId;
	}
	
	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
}
