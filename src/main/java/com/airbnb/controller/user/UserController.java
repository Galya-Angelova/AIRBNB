package com.airbnb.controller.user;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.user.User;
import com.airbnb.model.user.UserDAO;

@Controller
public class UserController {
	private static final int MAX_TIME = 1800;// seconds

	@Autowired
	private UserDAO dao;

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public String userLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String email = (String) request.getAttribute("email");
			String password = (String) request.getAttribute("password");
			int userId = dao.login(email, password);
			User user = null;
			if (userId > 0) {
				user = dao.userFromId(userId);
			}
			if (user != null) {
				request.getSession().setAttribute("user", user);
				request.getSession().setMaxInactiveInterval(MAX_TIME);
				return "home";
			} else {
				throw new InvalidUserException("invalid username or password");
			}
		} catch (InvalidUserException e) {
			request.setAttribute("exception", e);
			return "error";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	protected String userRegister(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String email = (String) request.getParameter("email");
			String password = (String) request.getParameter("password");
			String passwordConfirm = (String) request.getParameter("confirm password");
			boolean isMale = Boolean.valueOf(String.valueOf(request.getAttribute("gender")));
			String firstName = (String) request.getParameter("first name");
			String lastName = (String) request.getParameter("last name");

			String birthdate = request.getParameter("bday");
			Date date = Date.valueOf(birthdate);
			LocalDate localDate = date.toLocalDate();
			int month = localDate.getMonthValue();
			int day = localDate.getDayOfMonth();
			int year = localDate.getYear();

			System.out.println(localDate.toString());
			if (!password.equals(passwordConfirm)) {
				throw new InvalidUserException("Password mismatch");
			}
			if (!User.validateEmail(email)) {
				throw new InvalidUserException("You should try with valid email.");
			}
			if (!(User.validateStringText(firstName) || User.validateStringText(lastName))) {
				throw new InvalidUserException("Invalid name, your name should contains only characters.");
			}

			User user = new User(0, email, password, isMale, firstName, lastName, day, month, year, "");

			dao.register(user);

			return "index";

		} catch (InvalidUserException e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			return "error";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			return "error";
		}
	}
}
