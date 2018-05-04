package com.airbnb.model.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidReviewException;
import com.airbnb.model.db.DBConnectionTest;

@Component
public class ReviewDAO implements IReviewDAO {

	// private static final String INSERT_REVIEW = "INSERT INTO review
	// values(null,'Title','Text',1);";
	private static final String INSERT_REVIEW = "INSERT INTO review values(null,?,?,((SELECT place.id FROM place WHERE place.name = ?));";
	/*@Autowired
	private static DBConnectionTest dbConnection;
	private static Connection connection;

	static {
		try {
			dbConnection = new DBConnectionTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ReviewDAO() {
		connection = ReviewDAO.dbConnection.getConnection();
	}
*/
	@Autowired
	private  DBConnectionTest dbConnection;
	private  Connection connection;
	
	@Autowired
	public ReviewDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}
	@Override
	public int createReview(String title, String text, String placeName) throws InvalidReviewException {
		if (title != null && text != null && placeName != null
				&& !(title.isEmpty() || text.isEmpty() || placeName.isEmpty())) {
			try (PreparedStatement ps = connection.prepareStatement(INSERT_REVIEW, Statement.RETURN_GENERATED_KEYS)) {
//				connection.setAutoCommit(false);
				ps.setString(1, title);
				ps.setString(2, text);
				ps.setString(3, placeName);

				ResultSet rs = ps.getGeneratedKeys();
				rs.next();

//				connection.commit();
//				ps.close();

				return rs.getInt(1);

			}catch (SQLException e) {
//				try {
//					connection.rollback();
//				} catch (SQLException e1) {
//					throw new InvalidReviewException("Oops something went wrong.", e1);
//				}
				throw new InvalidReviewException("Invalid statement", e);
//			}finally {
//				try {
//					connection.setAutoCommit(false);
//				} catch (SQLException e) {
//					throw new InvalidReviewException("Set auto commit false error", e);
//				}
			}
		}else {
			throw new InvalidReviewException("Please insert title, text and place name.");
		}
	}

}
