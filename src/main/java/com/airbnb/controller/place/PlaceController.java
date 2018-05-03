package com.airbnb.controller.place;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.airbnb.exceptions.InvalidCityException;
import com.airbnb.exceptions.InvalidCountryException;
import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.address.Address;
import com.airbnb.model.address.AddressDAO;
import com.airbnb.model.address.City;
import com.airbnb.model.address.CityDAO;
import com.airbnb.model.address.Country;
import com.airbnb.model.address.CountryDAO;
import com.airbnb.model.place.Place;
import com.airbnb.model.place.Place.PlaceType;
import com.airbnb.model.place.PlaceDAO;
import com.airbnb.model.place.PlaceDTO;
import com.airbnb.model.user.User;

@Controller
@MultipartConfig
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
			@RequestParam String country, @RequestParam double price, // @RequestParam("files") MultipartFile[] files,
																		// ModelMap modelMap,
			HttpServletRequest request) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			int country_id = 0;
			try {
				country_id = countryDAO.giveCountryId(country);
			} catch (InvalidCountryException e) {
				country_id = countryDAO.addCountry(new Country(0, country));
			}
			Country countryObject = countryDAO.countryFromId(country_id);

			int city_id = 0;
			try {
				city_id = cityDAO.giveCityId(city);
			} catch (InvalidCityException e) {
				city_id = cityDAO.addCity(new City(0, city));
			}
			City cityObject = cityDAO.cityFromId(city_id);
			Address address = new Address(0, country_id, city_id, street, streetNumber);
			address.setCity(cityObject);
			address.setCountry(countryObject);
			int addressId = addressDAO.addAddress(address);

			Place place = new Place(0, name, false, addressId, placeTypeName, user.getId(), price);
			place.setAddress(address);
			user.addToMyPlaces(place);
			// String imageUrl = this.placeDAO.saveImageURL(files, place.getId());

			/*
			 * for (MultipartFile f : files) { if (f.isEmpty()) { continue; } try {
			 * System.out.println(f.getInputStream().toString());
			 * System.out.println("IMA FAIL"); } catch (IOException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } }
			 */

			placeDAO.addPlace(place);
			/*
			 * StringJoiner sj = new StringJoiner(" , ");
			 * 
			 * for (MultipartFile file : files) {
			 * 
			 * if (file.isEmpty()) { continue; // next pls }
			 * 
			 * try {
			 * 
			 * byte[] bytes = file.getBytes(); Path path = Paths.get(placeDAO.IMAGE_PATH +
			 * file.getOriginalFilename()); Files.write(path, bytes);
			 * 
			 * sj.add(file.getOriginalFilename());
			 * 
			 * } catch (IOException e) { e.printStackTrace(); } }
			 * 
			 * String uploadedFileName = sj.toString(); if
			 * (StringUtils.isEmpty(uploadedFileName)) {
			 * redirectAttributes.addFlashAttribute("message",
			 * "Please select a file to upload"); } else {
			 * redirectAttributes.addFlashAttribute("message", "You successfully uploaded '"
			 * + uploadedFileName + "'"); }
			 */
			model.addAttribute("place", place);
			return "viewPlace";// or more-places
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/myPlaces", method = RequestMethod.GET)
	public String getUserPlaces(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "index";
		}
		try {
			List<PlaceDTO> placesForUser = this.placeDAO.gettAllPlacesForUser(user.getId());
			model.addAttribute("userPlaces", placesForUser);

			return "myPlaces";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/allPlaces", method = RequestMethod.GET)
	public String showAllPlaces(Model model, HttpSession session) {
		try {
			model.addAttribute("allPlaces", this.placeDAO.getAllPlaces());
			return "allPlaces";
		} catch (InvalidPlaceException e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}

	}

	

	/*
	 * @RequestMapping("/save-place") public String uploadResources(
	 * HttpServletRequest servletRequest,
	 * 
	 * @ModelAttribute Place place, Model model) { //Get the uploaded files and
	 * store them List<MultipartFile> files = place.getPhotoes(); List<String>
	 * fileNames = new ArrayList<String>(); if (null != files && files.size() > 0) {
	 * for (MultipartFile multipartFile : files) {
	 * 
	 * String fileName = multipartFile.getOriginalFilename();
	 * fileNames.add(fileName);
	 * 
	 * File imageFile = new
	 * File(servletRequest.getServletContext().getRealPath("/image"), fileName); try
	 * { multipartFile.transferTo(imageFile); } catch (IOException e) {
	 * e.printStackTrace(); } } }
	 * 
	 * // Here, you can save the product details in database
	 * 
	 * model.addAttribute("place", place); return "viewPlace"; }
	 */
}
