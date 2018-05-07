package com.airbnb.controller.reservation;

import java.sql.Date;
import java.time.LocalDate;

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
	public String makeReservation(Model model,@PathVariable("id") int id, HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return "redirect: ./logout";
			}
			PlaceDTO view = placeDAO.getDtoById(id);
			session.setAttribute("place", view);

			Reservation reservation = new Reservation();
			session.setAttribute("reservation", reservation);
			session.setAttribute("wrongDates", false);
			return "reservation";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public String makeReservation(Model model, @RequestParam Date startDate, @RequestParam Date endDate, HttpServletRequest request) {
		try {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "index";
		}
		LocalDate start = startDate.toLocalDate();
		LocalDate end = endDate.toLocalDate();
		if ((end.isBefore(start)) ||( start.isBefore(LocalDate.now()))) {
			session.setAttribute("wrongDates", true);
			return "reservation";
		}
		
		PlaceDTO place= (PlaceDTO)session.getAttribute("place");
		reservationDAO.makeReservation(new Reservation(0,start,end,place.getId(),user.getId(),0));
		//	TODO send email to user
		// TODO send email to owner
		return "redirect: ./search";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("exception", e);
			return "error";
		}
	}
}
