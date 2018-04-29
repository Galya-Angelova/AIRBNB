package com.airbnb.tests;

import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.airbnb.config.SpringWebConfig;
import com.airbnb.model.db.DBConnectionTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringWebConfig.class)
public class DBTest {
	@Autowired
	private static DBConnectionTest db;

	private Connection con = db.getConnection();

	@Test
	public void test() {
		try (PreparedStatement ps = con.prepareStatement(
				"INSERT INTO users VALUES (null, ?, ? ,?, ?, ?, ?, false, false, sha1(?))",
				Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, "k@safa.das");
			ps.setBoolean(2, true);
			ps.setString(3, "HAHA");
			ps.setString(4, "PAPA");
			LocalDate lc = LocalDate.now();
			ps.setDate(5, Date.valueOf(lc));
			ps.setString(6, "0878421934");
			ps.setString(7, "Kp+91253641");
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			assertTrue(rs.getInt(1) > 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
