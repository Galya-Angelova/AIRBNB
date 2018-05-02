package com.airbnb.controller.place;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.exceptions.InvalidCountryException;
import com.airbnb.model.address.Address;
import com.airbnb.model.address.AddressDAO;
import com.airbnb.model.address.City;
import com.airbnb.model.address.CityDAO;
import com.airbnb.model.address.Country;
import com.airbnb.model.address.CountryDAO;
import com.airbnb.model.place.Place;
import com.airbnb.model.place.Place.PlaceType;
import com.airbnb.model.place.PlaceDAO;
import com.airbnb.model.user.User;

@Controller
public class PlaceController {

	@Autowired
	private PlaceDAO placeDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountryDAO countryDAO;
	@Autowired
	private AddressDAO addressDAO;

	@RequestMapping(value = "/createPlace", method = RequestMethod.GET)
	public String createPlace(Model model) {
		try {

			List<PlaceType> placeTypes = placeDAO.getAllPlaceTypes();

			model.addAttribute("placeTypes", placeTypes);

			return "addNewPlace";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

//	@RequestMapping(value = "/createPlace", method = RequestMethod.POST)
//	public String create(Model model, @RequestParam String name, @RequestParam String placeType,
//			@RequestParam String street, @RequestParam int streetNumber, @RequestParam String city,
//			@RequestParam String country, HttpSession session) throws ServletException, IOException {
//		try {
//			int addressId = addressDAO.addAddress(street, streetNumber, country, city);
//
//			User user = (User) session.getAttribute("user");
//
//			Place place = new Place(0, name, false, addressId, placeType, user.getId());
//
//			placeDAO.createPlace(name, street, streetNumber, city, country, placeType, user.getEmail());
//			List<Place> places = new ArrayList<Place>();
//			places.add(place);
//			session.setAttribute("myPlaces", places);
//
//			return "home";// or more-places
//		} catch (Exception e) {
//			e.printStackTrace();
//			model.addAttribute("exception", e);
//			return "error";
//		}
//	}
//	
	
	@RequestMapping(value = "/createPlace", method = RequestMethod.POST)
	public String create(Model model, @RequestParam String name, @RequestParam String placeTypeName,
			@RequestParam String street, @RequestParam int streetNumber, @RequestParam String city,
			@RequestParam String country, HttpSession session) throws ServletException, IOException {
		try {
//			purvo namirame neobhodimoto ni country_id i city_id ako gi ima a ako gi nqma v bazata gi suzdavame
//			i v dvata sluchaq poluchavame id-tata
			int country_id=0;
			try{
				country_id=countryDAO.giveCountryId(country);
			}catch (InvalidCountryException e){
				country_id=countryDAO.addCountry(new Country(0, country));
			}
			int city_id=0;
			try{
				city_id=cityDAO.giveCityId(city);
			}catch (InvalidCityException e){
				city_id=cityDAO.addCity(new City(0, city));
			}
//			s tezi id-ta suzdavame addresa i go dobavqme v bazata i mu vzimame id-to i s nego suzdavame place-a
			Address address= new Address(0, country_id, city_id, street, streetNumber);
			int addressId = addressDAO.addAddress(address);
			
			User user = (User) session.getAttribute("user");
			Place place = new Place(0, name, false, addressId, placeTypeName, user.getId());
			placeDAO.addPlace(place);

			return "home";// or more-places
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}
}
