package com.airbnb.controller.place;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
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

import com.airbnb.exceptions.InvalidAddressException;
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
import com.airbnb.model.reservation.Reservation;
import com.airbnb.model.review.Review;
import com.airbnb.model.review.ReviewDAO;
import com.airbnb.model.user.User;
import com.airbnb.model.user.UserDAO;

@Controller
@MultipartConfig
public class PlaceController {
	private static final String IMAGE_EXTENSIONS = ".png .jpeg .jpg .bmp";
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
	@Autowired
	private ReviewDAO reviewDAO;
	
	@RequestMapping(value = "/createPlace", method = RequestMethod.GET)
	public String createPlace(Model model, HttpSession session) {
		if (session.getAttribute("user") == null) {
			return "redirect: ./logout";
		}
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
	public String create(Model model, HttpSession session, HttpServletRequest request, @RequestParam String name,
			@RequestParam String country, @RequestParam String city, @RequestParam String placeTypeName,
			@RequestParam String street, @RequestParam int streetNumber, @RequestParam double price,
			@RequestParam("files") MultipartFile[] files) throws ServletException {

		try {
			User user = (User) session.getAttribute("user");

			if (user == null) {
				return "index";
			}

			int addressId = this.getAddressId(country, city, street, streetNumber);
			Address address = this.addressDAO.addressFromId(addressId);
			Place place = new Place(0, name, false, addressId, placeTypeName, user.getId(), Double.valueOf(price),
					address, LocalDate.now());

			savePhotos(files, place,request);

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

	private void savePhotos(MultipartFile[] files, Place place,HttpServletRequest request) throws IOException {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		if(isValid(files, request)) {
		for (MultipartFile f : files) {
			if (f.isEmpty()) {
				continue;
			}
			this.placeDAO.saveFileToDisk(place, f, randomUUIDString);
		}
		}
	}

	private boolean isValid(MultipartFile[] files,HttpServletRequest request) {
		for (MultipartFile file : files) {
			if (file.isEmpty()) {
				continue;
			}
			ServletContext context = request.getServletContext();
			String fileName = file.getOriginalFilename();
			String mimeType = context.getMimeType(fileName);
			if (!mimeType.startsWith("image/")) {
			    // It's not  an image.
				return false;
			}
			

			String[] fileParts = file.getOriginalFilename().split("\\.");
			String fileExtension = fileParts[fileParts.length - 1];

			if (!IMAGE_EXTENSIONS.contains(fileExtension)) {
				return false;
			}
		}

		return true;
	}

	private int getAddressId(String country, String city, String street, int streetNumber)
			throws InvalidCountryException, InvalidCityException, InvalidAddressException {
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

		Address address = new Address(0, country_id, city_id, street, streetNumber, cityObject, countryObject);
		int addressId = addressDAO.addAddress(address);
		return addressId;

	}

	@RequestMapping(value = "/myPlaces", method = RequestMethod.GET)
	public String getUserPlaces(Model model, HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "redirect: ./logout";
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
	


	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String placeSearch(Model model) {
		try {
			PlaceSearchInfo filter = placeDAO.getDefaultFilter();
			PlaceSearchInfo editedFilter = placeDAO.getDefaultFilter();

			Set<PlaceDTO> allPlaces = placeDAO.getAllPlaces();
			List<String> placeTypes = placeDAO.getAllPlaceTypes();
			Set<String> cities = cityDAO.getCities();
			List<String> imgUrls = placeDAO.getAllPhotos();
			
			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("cities", cities);
			model.addAttribute("filter", filter);
			model.addAttribute("editedFilter", editedFilter);
			model.addAttribute("allPlaces", allPlaces);
			model.addAttribute("allPhotosURLs", imgUrls);
			
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
			List<PlaceDTO> allPlaces = placeDAO.getFilteredPlaces(editedFilter);

			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("cities", cities);
			model.addAttribute("filter", filter);
			model.addAttribute("editedFilter", editedFilter);
			model.addAttribute("allPlaces", allPlaces);

			return "placeSearch";
		} catch (InvalidPlaceException e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/editPlace", method = RequestMethod.GET)
	public String getEditPage(@RequestParam("id") int id, Model model) {

		try {
			PlaceDTO view = this.placeDAO.getDtoById(id);
			List<String> placeTypes = this.placeDAO.getAllPlaceTypes();
			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("place", view);
			return "editPlace";
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

	@RequestMapping(value = "/editPlace", method = RequestMethod.POST)
	public String editPlace(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam("id") int placeId, @RequestParam String name, @RequestParam String country,
			@RequestParam String city, @RequestParam String placeTypeName, // @RequestParam boolean isBusied,
			@RequestParam String street, @RequestParam int streetNumber, @RequestParam double price,
			@RequestParam String dateOfPosting ) {//, @RequestParam("files") MultipartFile[] files) {

		try {
			User user = (User) session.getAttribute("user");

			if (user == null) {
				return "index";
			}

			int addressId = this.getAddressId(country, city, street, streetNumber);
			Address address = this.addressDAO.addressFromId(addressId);
			LocalDate date = PlaceDTO.convertFromStringToLocalDate(dateOfPosting);
			Place place = new Place(placeId, name, false, addressId, placeTypeName, user.getId(), price, address, date);
			boolean isEdited = placeDAO.editPlace(place);
			
			
			model.addAttribute("place", place);
			if (isEdited) {
				return "redirect:/myPlaces";
			} else {
				return "editPlace";
			}
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
	
	@RequestMapping(value = "/placeDetails", method = RequestMethod.GET)
	public String getPlaceDetails(@RequestParam("id") int id, Model model, HttpSession session) {
		try {
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "redirect:/logout";
			}
			
			PlaceDTO view = this.placeDAO.getDtoById(id);
			List<String> placeTypes = this.placeDAO.getAllPlaceTypes();
			double rating =this.placeDAO.getAvgRating(id);
			double avgRating = Math.round(rating * 100.0) / 100.0;
			Map<Review, User> reviews = new TreeMap<Review, User>((r1, r2) -> {
				return r2.getId() - r1.getId();
			});
			List<Review> reviewsList= reviewDAO.getAllReviewsForPlace(id);
			for (Review review : reviewsList) {
				reviews.put(review, userDAO.userFromId(review.getUserId()));
			}
			
			Reservation reservation = new Reservation();
			session.setAttribute("reservation", reservation);
			session.setAttribute("wrongDates", false);
			session.setAttribute("sameUser", false);
			
			model.addAttribute("placeTypes", placeTypes);
			model.addAttribute("place", view);
			model.addAttribute("avgRating",avgRating);
			model.addAttribute("reviews",reviews);
			model.addAttribute("newReview",new Review());
			return "placeDetails";
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
	
	@RequestMapping(value = "/placeDetails", method = RequestMethod.POST)
	public String addReviewToPlaceDetail(Model model, @ModelAttribute("newReview") Review review,
			BindingResult result,HttpSession session) {
		try {
			if (result.hasErrors()) {
				throw new InvalidPlaceException("Invalid review data");
			}
			User user= (User) session.getAttribute("user");
			if (user == null) {
				return "redirect:/placeDetails?id="+review.getPlaceId();
			}
			reviewDAO.createReview(review);
			return "redirect: ./placeDetails?id="+review.getPlaceId();
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
	
	@ModelAttribute("newReview")
	public Review createStubReview() {
		return new Review();
	}
}
