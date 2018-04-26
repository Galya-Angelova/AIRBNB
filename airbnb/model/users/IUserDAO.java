package users;

import java.sql.SQLException;

import exceptions.InvalidUserException;

public interface IUserDAO {

	public int login(String email,String password) throws InvalidUserException ;

	public int register(User user)throws InvalidUserException;
	
	public User userFromId(int user_id)throws InvalidUserException;
	
	public User getUserFromEmailAndPassword(String email,String password) throws InvalidUserException;
	
	public void saveUser(User user) throws SQLException;
	
}
