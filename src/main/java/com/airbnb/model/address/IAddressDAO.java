package com.airbnb.model.address;

import com.airbnb.exceptions.InvalidAddressException;

public interface IAddressDAO {
	public int addAddress(String street, int streetNumber, String country, String city)throws InvalidAddressException;
	public Address addressFromId(int address_id)throws InvalidAddressException;
}
