package userTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import exceptions.DBException;
import exceptions.InvalidUserException;
import users.UserDAO;

public class UserTests {

	@Test(expected = InvalidUserException.class)
	public void testBadPassword() throws  InvalidUserException, DBException {
		UserDAO dao = UserDAO.getInstance();
		dao.login("User", "pasWo=1");
	}
	
	@Test(expected = InvalidUserException.class)
	public void testBadUsername() throws  InvalidUserException, DBException {
		UserDAO dao = UserDAO.getInstance();
		dao.login("Admin","");
	}

	@Test
	public void testSuccess() throws InvalidUserException {
		UserDAO dao;
		try {
			dao = UserDAO.getInstance();
			int id = dao.login("user@asr.com","pass");
			assertEquals(id, 1);
		} catch (DBException e) {
			e.printStackTrace();
		}
		
		
	}
}
