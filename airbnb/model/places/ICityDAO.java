package places;

import exceptions.InvalidCityException;

public interface ICityDAO {

	public int addCity(City city) throws InvalidCityException;
	public int giveCityId(String city)throws InvalidCityException;
	public City cityFromId(int city_id)throws InvalidCityException;
}
