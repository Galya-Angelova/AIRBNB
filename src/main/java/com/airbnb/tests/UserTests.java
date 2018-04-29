package com.airbnb.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.airbnb.config.SpringWebConfig;
import com.airbnb.exceptions.DBException;
import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.user.UserDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringWebConfig.class)
public class UserTests {
	
	@Autowired
	private UserDAO dao;
	
	@Test(expected = InvalidUserException.class)
	public void testBadPassword() throws  InvalidUserException, DBException {
		dao.login("User", "pasWo=1");
	}
	
	@Test(expected = InvalidUserException.class)
	public void testBadUsername() throws  InvalidUserException, DBException {
		dao.login("Admin","");
	}

	@Test
	public void testSuccess() throws InvalidUserException {
		int id = dao.login("user@asr.com","pass");
		assertEquals(id, 1);
	}
	
}
