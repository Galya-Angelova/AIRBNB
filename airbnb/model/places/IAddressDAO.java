package places;

import exceptions.InvalidAddressException;

public interface IAddressDAO {
	
	public int addAddress(Address address)throws InvalidAddressException;
	public Address addressFromId(int address_id)throws InvalidAddressException;
}
