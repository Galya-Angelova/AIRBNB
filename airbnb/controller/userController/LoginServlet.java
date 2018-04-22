package userController;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exceptions.DBException;
import exceptions.InvalidUserException;
import users.User;
import users.UserDAO;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String email = (String) request.getAttribute("email");
			String password = (String) request.getAttribute("password");
			User u = UserDAO.getInstance().getUserFromEmailAndPassword(email, password);
			if(u != null) {
				request.getSession().setAttribute("user",u);
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}else {
				throw new InvalidUserException("invalid username or password");
			}
		}catch (InvalidUserException | DBException e) {
			request.setAttribute("exception", e);
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}

}
