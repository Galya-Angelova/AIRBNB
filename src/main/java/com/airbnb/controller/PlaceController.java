package com.airbnb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.airbnb.model.address.AddressDAO;
import com.airbnb.model.place.Place;
import com.airbnb.model.place.PlaceDAO;
import com.airbnb.model.user.User;

@Controller
public class PlaceController {

	@Autowired
	private PlaceDAO placeDAO;
	
	@Autowired
	private AddressDAO addressDAO;

	@RequestMapping(value = "/createPlace", method = RequestMethod.GET)
	public String createPlace(Model model) {
		try {

			List<String> placeTypes = placeDAO.getAllPlaceTypes();

			model.addAttribute("placeTypes", placeTypes);

			return "addNewPlace";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}
	
	@RequestMapping(value = "/createPlace", method = RequestMethod.POST)
	public String create(Model model, @RequestParam String name, @RequestParam String placeType,
			@RequestParam String street,@RequestParam int streetNumber, @RequestParam String city,@RequestParam String country,HttpSession session) throws ServletException, IOException {
		try {
			int addressId = addressDAO.addAddress(street, streetNumber, country, city);
			
			User user = (User) session.getAttribute("user");

			Place place = new Place(0, name, false, addressId, placeType, user.getId());

			placeDAO.createPlace(name, street, streetNumber, city, country, placeType, user.getEmail());
			List<Place> places = new ArrayList<Place>();
			places.add(place);
			session.setAttribute("myPlaces", places);
			
			return "home";//or more-places
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
}
}
