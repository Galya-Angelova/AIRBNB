package userController;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.InvalidUserException;
import users.User;
import users.UserDAO;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
			if(!(validateStringText(firstName) || validateStringText(lastName))){
				throw new InvalidUserException("Invalid name, your name should contains only characters.");
			}

			User user = new User(0, email, password, isMale, firstName, lastName, day, month, year, "");
			//User user = new User(email, password, isMale);
			UserDAO.getInstance().saveUser(user);

			request.getSession().setAttribute("user", user);

			request.getRequestDispatcher("/index.jsp").forward(request, response);

		} catch (InvalidUserException e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
	private boolean validateStringText(String text) {
		String str = text.trim();
		return ((str != null) && !(str.isEmpty()) && (str.matches("[A-Za-z ]*") && (str.equals(text))));
	}
}
