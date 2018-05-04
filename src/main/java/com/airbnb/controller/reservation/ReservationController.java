package com.airbnb.controller.reservation;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@RequestMapping(value = "/reservation", method = RequestMethod.GET)
	public String makeReservation(HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			
			return "redirect ./../logout";
		}
		Reservation reservation = new Reservation();
		session.setAttribute("reservation", reservation);
	
		//reservationDAO.makeReservation(new Reservation(0, startDate, endDate, placeId, userId, rating))
		return "reservation";
	}
	@RequestMapping(value = "/reservation", method = RequestMethod.POST)
	public String makeReservation(Model model, @RequestParam Date startDate, @RequestParam Date endDate,HttpServletRequest request) {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return "index";
		}
		//TODO startDate after now
		if(endDate.toLocalDate().isBefore(startDate.toLocalDate())) {
			return "reservation";
		}
	
		//reservationDAO.makeReservation(new Reservation(0, startDate, endDate, placeId, userId, rating))
		return "home";
	}
}
