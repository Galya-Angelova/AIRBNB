package com.airbnb.model.user;

import java.sql.SQLException;
import java.util.List;


import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.place.Place;

public interface IUserDAO {
	public int login(String email, String password) throws InvalidUserException;

	public int register(User user) throws InvalidUserException;

	public User userFromId(int user_id) throws InvalidUserException;

	public List<Integer> getAllUsersByID() throws SQLException ;
	
//	public User getUserFromEmailAndPassword(String email, String password) throws InvalidUserException;

//	public void saveUser(User user) throws SQLException;
	public void becomeAHost(User user) throws InvalidUserException;
	
	//public void addNewPlace(String streetName, String countryName, String placeTypeName, int userId) throws InvalidUserException;
//	public void changePassword(String newPass,String newPassConfirm, int userId) throws InvalidUserException;
	public boolean comparePasswords(int userId, String password) throws InvalidUserException;

	public void updateProfile(User user) throws InvalidUserException;

	public List<Integer> getVisitedPlacesByUserId(int user_id) throws InvalidUserException;

	public List<Integer> getUserPlacesByUserId(int user_id) throws InvalidUserException;

	public void addUserPlaceToUser(int placeID, User user) throws InvalidUserException;
}
