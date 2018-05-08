
package com.airbnb.controller.reservation;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.email.MailSender;
import com.airbnb.model.place.PlaceDAO;
import com.airbnb.model.place.PlaceDTO;
import com.airbnb.model.reservation.Reservation;
import com.airbnb.model.reservation.ReservationDAO;
import com.airbnb.model.user.User;
import com.airbnb.model.user.UserDAO;

@Controller
public class ReservationController {

	@Autowired
	private ReservationDAO reservationDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private PlaceDAO placeDAO;

	@RequestMapping(value = "/reservation/{id}", method = RequestMethod.GET)
	public String makeReservation(Model model, @PathVariable("id") int id, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "redirect:/logout";
			}
			PlaceDTO view = placeDAO.getDtoById(id);
			session.setAttribute("place", view);

			Reservation reservation = new Reservation();
			session.setAttribute("reservation", reservation);
			session.setAttribute("wrongDates", false);
			session.setAttribute("sameUser", false);
			return "reservation";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public String makeReservation(Model model, @RequestParam Date startDate, @RequestParam Date endDate,
			HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "index";
			}
			LocalDate start = startDate.toLocalDate();
			LocalDate end = endDate.toLocalDate();
			if ((end.isBefore(start)) || (start.isBefore(LocalDate.now()))) {
				session.setAttribute("wrongDates", true);
				return "reservation";
			}
			PlaceDTO place = (PlaceDTO) session.getAttribute("place");
			if (user.getId() == place.getOwnerId()) {
				session.setAttribute("sameUser", true);
				return "reservation";
			}

			int reservationId = reservationDAO
					.makeReservation(new Reservation(0, start, end, place.getId(), user.getId(), 0, LocalDate.now()));
			Thread t = new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(24 * 60 * 60 * 1000);
						if (end.isBefore(LocalDate.now())) {
							reservationDAO.deleteReservation(reservationId);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
			short days = (short) start.until(end, ChronoUnit.DAYS);
			double fullPrice = place.getPrice() * days;

			String userContent = String.format(
					"You've successfully made reservation for: %s, place type - %s, Address: Country - %s, City - %s, Street - %s, StreetNumber - %d , for %d days and it will cost you %.2f. The owner of the place have 7 days from now to reject your reservation! You can check in your \"Visited places\" section, if he rejects your reservation the place will no longer be there. If the place is there after the 7 days period that means your reservation is completed!",
					place.getName(), place.getPlaceTypeName(), place.getCountry(), place.getCity(), place.getStreet(),
					place.getStreetNumber(), days, fullPrice);
			MailSender.sendEmail(user.getEmail(), userContent);
			String ownerContent = String.format(
					"%s %s with e-mail: %s, has made a reservation for: %s, place type - %s, Address: Country - %s, City - %s, Street - %s, StreetNumber - %d , for %d days and it will cost %.2f. If you want to reject the reservation you have to log in our site and go to \"Awaiting reservations\" section and then click on the \"Reject  reservation\" button on the reservation! You have 7 days to reject it or it will be automaticly completed",
					user.getFirstName(), user.getLastName(), user.getEmail(), place.getName(), place.getPlaceTypeName(),
					place.getCountry(), place.getCity(), place.getStreet(), place.getStreetNumber(), days, fullPrice);
			MailSender.sendEmail(userDAO.userFromId(place.getOwnerId()).getEmail(), ownerContent);

			return "redirect: ./search";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/reservations", method = RequestMethod.GET)
	public String getReservations(Model model, HttpSession session) {
		try {
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "redirect: ./logout";
			}
			Map<Reservation, PlaceDTO> reservations = new TreeMap<Reservation, PlaceDTO>((r1, r2) -> {
				return r2.getId() - r1.getId();
			});
			List<Reservation> reservationsList = reservationDAO.getAllReservationsByHostID(user.getId());
			List<User> guests=new ArrayList<User>();
			for (Reservation reservation : reservationsList) {
				guests.add(userDAO.userFromId(reservation.getUserId()));
				PlaceDTO place = placeDAO.getDtoById(reservation.getPlaceId());
				reservations.put(reservation, place);
			}

			model.addAttribute("reservations", reservations);
			model.addAttribute("guests",guests);
			return "reservations";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/reservations/{id}", method = RequestMethod.GET)
	public String deleteReservations(Model model, @PathVariable("id") int id, HttpSession session) {
		try {
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "redirect: ./logout";
			}
			Reservation reservation = reservationDAO.reservationFromId(id);
			LocalDate sevenDaysPeriod = reservation.getReservationDate().plusDays(7);
			if (sevenDaysPeriod.isBefore(LocalDate.now())) {
				return "redirect:/reservations";
			}
			reservationDAO.deleteReservation(id);
			short days = (short) reservation.getStartDate().until(reservation.getEndDate(), ChronoUnit.DAYS);
			PlaceDTO place = placeDAO.getDtoById(reservation.getPlaceId());
			String userContent = String.format(
					"REJCETED! Your reservation for: %s, place type - %s, Address: Country - %s, City - %s, Street - %s, StreetNumber - %d , for %d days has been rejected by the owner of the place.",
					place.getName(), place.getPlaceTypeName(), place.getCountry(), place.getCity(), place.getStreet(),
					place.getStreetNumber(), days);
			User u = userDAO.userFromId(reservation.getUserId());
			MailSender.sendEmail(u.getEmail(), userContent);
			return "redirect:/reservations";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

}
