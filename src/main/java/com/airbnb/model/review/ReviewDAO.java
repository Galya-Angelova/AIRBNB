package com.airbnb.model.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.airbnb.exceptions.InvalidReviewException;
import com.airbnb.model.db.DBConnection;

@Component
public class ReviewDAO implements IReviewDAO {

	private static final String GET_ALL_REVIEWS_FOR_PLACE = "SELECT * FROM review WHERE place_id=? ORDER BY id DESC;";
	private static final String INSERT_REVIEW = "INSERT INTO review values(null,?,?,?,?);";
	
	@Autowired
	private  DBConnection dbConnection;
	private  Connection connection;
	
	@Autowired
	public ReviewDAO(DBConnection dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}
	@Override
	public int createReview(Review review) throws InvalidReviewException {
			try (PreparedStatement ps = connection.prepareStatement(INSERT_REVIEW, Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, review.getTitle());
				ps.setString(2, review.getText());
				ps.setInt(3, review.getPlaceId());
				ps.setInt(4, review.getUserId());
				
				ps.executeUpdate();
				
				ResultSet rs = ps.getGeneratedKeys();
				rs.next();

				return rs.getInt(1);

			}catch (SQLException e) {
				e.printStackTrace();
				throw new InvalidReviewException("Invalid statement", e);
			}
	}
	
	@Override
	public List<Review> getAllReviewsForPlace(int place_id) throws InvalidReviewException{
		List<Review> reviews = new ArrayList<Review>();
		try (PreparedStatement ps = connection.prepareStatement(GET_ALL_REVIEWS_FOR_PLACE)) {
			ps.setInt(1, place_id);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				int id = rs.getInt("id");
				String title= rs.getString("title");
				String text= rs.getString("text");
				int placeId = rs.getInt("place_id");
				int userId = rs.getInt("user_id");
				
				reviews.add(new Review(id, title, text, placeId, userId));
			}
			return reviews;
		}catch (SQLException e) {
			throw new InvalidReviewException("Invalid statement", e);
		}
	}
	
}
