package com.airbnb.model.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import com.airbnb.exceptions.DBException;

public class Test {
	public static void main(String[] args) {
		DBConnectionTest db=null;
		try {
			db = new DBConnectionTest();
		} catch (DBException e) {
			System.out.println(e.getMessage());
		}
		Connection con = db.getConnection();
			try (PreparedStatement ps = con.prepareStatement("INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false, sha1(?))", Statement.RETURN_GENERATED_KEYS)) {
				ps.setString(1, "k@safa.das");
				ps.setBoolean(2, true);
				ps.setString(3, "HAHA");
				ps.setString(4, "PAPA");
				LocalDate lc= LocalDate.now();
				ps.setDate(5, Date.valueOf(lc));
				ps.setString(6, "0899499934");
				ps.setString(7, "Kp+920505");
				ps.executeUpdate();
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
