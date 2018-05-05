package com.airbnb.controller.place;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
import com.airbnb.model.place.PlaceDAO;
import com.airbnb.model.place.PlaceDTO;
import com.airbnb.model.place.PlaceSearchInfo;
import com.airbnb.model.user.User;
import com.airbnb.model.user.UserDAO;

@Controller
@MultipartConfig
public class PlaceController {
	private static final String ALLOWED_FILE_EXTENSIONS = ".png .jpeg .jpg .bmp";
	@Autowired
	private PlaceDAO placeDAO;
	@Autowired
	private CityDAO cityDAO;
	@Autowired
	private CountryDAO countryDAO;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private UserDAO userDAO;

	@RequestMapping(value = "/createPlace", method = RequestMethod.GET)
	public String createPlace(Model model) {
		try {

			List<String> placeTypes = placeDAO.getAllPlaceTypes();

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
	public String create(Model model, HttpServletRequest request, @RequestParam String name,
			@RequestParam String country, @RequestParam String city, @RequestParam String placeTypeName,
			@RequestParam String street, @RequestParam int streetNumber, @RequestParam double price,
			@RequestParam("files") MultipartFile[] files) // @RequestParam("files") MultipartFile[] files,
			throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");

			if (user == null) {
				return "index";
			}
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
			Address address = new Address(0, country_id, city_id, street, Integer.valueOf(streetNumber));
			address.setCity(cityObject);
			address.setCountry(countryObject);
			int addressId = addressDAO.addAddress(address);

			if(!this.isValid(files)) {
				return "createPlace";
			}
			
			Place place = new Place(0, name, false, addressId, placeTypeName, user.getId(), Double.valueOf(price));
			place.setAddress(address);

			// String imageUrl = this.placeDAO.saveImageURL(files, place.getId());

			UUID uuid = UUID.randomUUID();
			String randomUUIDString = uuid.toString();
			for (MultipartFile f : files) {
				if (f.isEmpty()) {
					continue;
				}
				try {
					this.placeDAO.saveFileToDisk(place, f, randomUUIDString);
				} catch (IOException e) {
					e.printStackTrace();
					return "redirect:error";
				}
	}

			int placeID = placeDAO.addPlace(place);
			userDAO.addUserPlaceToUser(placeID, user);

			
			request.setAttribute("place", place);
			model.addAttribute("place", place);
			return "redirect:./myPlaces";// or more-places
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	private boolean isValid(MultipartFile[] files) {
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue;
			}

			String[] fileParts = file.getOriginalFilename().split("\\.");
			String fileExtension = fileParts[fileParts.length - 1];

			if (!ALLOWED_FILE_EXTENSIONS.contains(fileExtension)) {
				return false;
			}
		}
		
		return true;
	}

	@RequestMapping(value = "/myPlaces", method = RequestMethod.GET)
	public String getUserPlaces(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect ./index";
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

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String placeSearch(Model model) {
		try {
			PlaceSearchInfo filter = placeDAO.getDefaultFilter();
			PlaceSearchInfo editedFilter = placeDAO.getDefaultFilter();

			List<Place> places = placeDAO.getAllPlacesForSearch();
			List<String> placeTypes = placeDAO.getAllPlaceTypes();
			Set<String> cities = cityDAO.getCities();
			System.out.println(editedFilter);
			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("cities", cities);
			model.addAttribute("filter", filter);
			model.addAttribute("editedFilter", editedFilter);
			model.addAttribute("places", places);

			return "placeSearch";
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

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String filteredPlaceSearch(Model model, @ModelAttribute("editedFilter") PlaceSearchInfo editedFilter,
			BindingResult result) {
		try {
			if (result.hasErrors()) {
				throw new InvalidPlaceException("Invalid filter data");
			}

			List<String> placeTypes = placeDAO.getAllPlaceTypes();
			Set<String> cities = cityDAO.getCities();
			PlaceSearchInfo filter = placeDAO.getDefaultFilter();
			List<Place> places = placeDAO.getFilteredPlaces(editedFilter);

			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("cities", cities);
			model.addAttribute("filter", filter);
			model.addAttribute("editedFilter", editedFilter);
			model.addAttribute("places", places);

			return "placeSearch";
		} catch (InvalidPlaceException e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/showPhoto", method = RequestMethod.GET)
	public String showPhoto(Model model, HttpServletRequest req, HttpServletResponse resp,
			@RequestParam() String path) {

		try {
			if (path != null) {
				File file = new File(path);
				try (InputStream bytesFromFile = new FileInputStream(file); OutputStream out = resp.getOutputStream()) {

					byte[] bytes = new byte[1024];

					while ((bytesFromFile.read(bytes)) != -1) {
						out.write(bytes);
					}
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}
}
