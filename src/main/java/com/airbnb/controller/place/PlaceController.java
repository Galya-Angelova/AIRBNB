package com.airbnb.controller.place;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.airbnb.example.controller.Product;
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
			 model.addAttribute("place", new Place());
			return "addNewPlace";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/createPlace", method = RequestMethod.POST)
	public String create(Model model, @RequestParam String name, @RequestParam String placeTypeName,
			@RequestParam String street, @RequestParam int streetNumber, @RequestParam String city,
			@RequestParam String country, @RequestParam("files") MultipartFile files, HttpSession session) throws ServletException, IOException {
		try {
			
			int country_id = 0;
			try {
				country_id = countryDAO.giveCountryId(country);
			} catch (InvalidCountryException e) {
				country_id = countryDAO.addCountry(new Country(0, country));
			}
			int city_id = 0;
			try {
				city_id = cityDAO.giveCityId(city);
			} catch (InvalidCityException e) {
				city_id = cityDAO.addCity(new City(0, city));
			}
			// s tezi id-ta suzdavame addresa i go dobavqme v bazata i mu vzimame id-to i s
			// nego suzdavame place-a
			Address address = new Address(0, country_id, city_id, street, streetNumber);
			int addressId = addressDAO.addAddress(address);

			User user = (User) session.getAttribute("user");
			Place place = new Place(0, name, false, addressId, placeTypeName, user.getId());
			placeDAO.addPlace(place);
			
			String imageUrl = this.placeDAO.saveImageURL(files, place.getId());

			return "viewPlace";// or more-places
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}
	

/*	 @RequestMapping("/save-place")
	    public String uploadResources( HttpServletRequest servletRequest,
	                                 @ModelAttribute Place place,
	                                 Model model)
	    {
	        //Get the uploaded files and store them
	        List<MultipartFile> files = place.getPhotoes();
	        List<String> fileNames = new ArrayList<String>();
	        if (null != files && files.size() > 0)
	        {
	            for (MultipartFile multipartFile : files) {
	 
	                String fileName = multipartFile.getOriginalFilename();
	                fileNames.add(fileName);
	 
	                File imageFile = new File(servletRequest.getServletContext().getRealPath("/image"), fileName);
	                try
	                {
	                    multipartFile.transferTo(imageFile);
	                } catch (IOException e)
	                {
	                    e.printStackTrace();
	                }
	            }
	        }
	 
	        // Here, you can save the product details in database
	         
	        model.addAttribute("place", place);
	        return "viewPlace";
	    }*/
}
