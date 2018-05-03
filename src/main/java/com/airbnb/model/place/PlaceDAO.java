package com.airbnb.model.place;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.model.db.DBConnectionTest;
import com.airbnb.model.place.Place.PlaceType;

@Component
public class PlaceDAO implements IPlaceDAO {
	private static final String ALL_PLACE_TYPES = "SELECT * FROM placetype;";
	// private static final String GET_ADDRESS = "SELECT * FROM locations JOIN
	// countries ON locations.country_id = countries.id JOIN cities ON
	// locations.city_id = cities.id WHERE cities.name = ? and countries.name = ?;";
	private static final String PLACE_FROM_ID = "SELECT * FROM users WHERE id=?";

	// private static final String CHECK_PLACE_TYPE = "SELECT count(*) FROM
	// placetype WHERE placetype.name = ?;";
	// private static final String CHECK_EMAIL = "SELECT count(*) FROM users WHERE
	// users.email = ?;";
	// private static final String ADD_PLACE = "INSERT INTO place
	// VALUES(null,?,false,?,(SELECT placetype.id FROM placetype where
	// placetype.name = ?),(SELECT users.id FROM users WHERE users.email = ?));";

	// private static final int EMPTY_NAME = 0;

	/*
	 * @Autowired private static DBConnectionTest dbConnection; private static
	 * Connection connection;
	 * 
	 * static { try { dbConnection = new DBConnectionTest(); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 * 
	 * public PlaceDAO() { connection = PlaceDAO.dbConnection.getConnection(); }
	 */
	private static final String ADD_PLACE_SQL = "INSERT INTO place VALUES (null, ?,false,?,?,?)";
	private static final String ADD_PLACETYPE_SQL = "INSERT INTO placetype VALUES (null, ?)";
	private static final String GIVE_PLACETYPE_SQL = "SELECT * FROM placetype WHERE name= ?";
	private static final String PLACETYPE_FROM_ID_SQL = "SELECT * FROM placetype WHERE id=?";
	private static final String ADD_PICTURES = "INSERT INTO pictures VALUES(null,?,?);";
	public static final String IMAGE_PATH = "D:\\uploaded\\dir";
	private static final String EXTENTION = ".jpg";
	private static int COUNT = 0;
	// @Autowired
	// private AddressDAO addressDAO;
	@Autowired
	private DBConnectionTest dbConnection;
	private Connection connection;

	@Autowired
	public PlaceDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
	}

	@Override
	public int addPlace(Place place) throws InvalidPlaceException {
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(ADD_PLACE_SQL, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, place.getName());
			ps.setInt(2, place.getAddressID());
			int placeTypeId = 0;
			try {
				placeTypeId = givePlaceTypeId(place.getPlaceTypeName());
			} catch (InvalidPlaceException e) {
				placeTypeId = addPlaceType(place.getPlaceType());
			}
			ps.setInt(3, placeTypeId);
			ps.setInt(4, place.getOwnerId());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			place.setId(rs.getInt(1));
			
			for (String imagePath : place.getPhotosUrls()) {
				ps = connection.prepareStatement(ADD_PICTURES);
				ps.setString(1, imagePath);
				ps.setInt(2, place.getId());
				ps.executeUpdate();
			}

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement in DB", e);
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new InvalidPlaceException("Invalid statement in DB", e);
			}
		}
	}

	@Override
	public int addPlaceType(PlaceType placeType) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(ADD_PLACETYPE_SQL, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, placeType.getName());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();

			return rs.getInt(1);
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

	@Override
	public int givePlaceTypeId(String placeType) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(GIVE_PLACETYPE_SQL)) {
			ps.setString(1, placeType);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new InvalidPlaceException("There is no place type with that name in the database");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

	@Override
	public String placeTypeFromId(int placeType_id) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(PLACETYPE_FROM_ID_SQL)) {
			ps.setInt(1, placeType_id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");

				return name;
			}
			throw new InvalidPlaceException("There is no place type with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

	@Override
	public Place placeFromId(int placeId) throws InvalidPlaceException {
		try (PreparedStatement ps = connection.prepareStatement(PLACE_FROM_ID)) {
			ps.setInt(1, placeId);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				int addressId = rs.getInt("address_id");
				boolean busied = rs.getBoolean("busied");
				int placeTypeId = rs.getInt("placeType_id");
				String placeTypeName = placeTypeFromId(placeTypeId);
				int userId = rs.getInt("user_id");
				// int address_id = rs.getInt("locations_id");

				return new Place(id, name, busied, addressId, placeTypeName, userId);
			}

			throw new InvalidPlaceException("There is no place with that id!");
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}
	}

	@Override
	public List<PlaceType> getAllPlaceTypes() throws InvalidPlaceException {
		List<PlaceType> result = new ArrayList<>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_PLACE_TYPES);
			while (set.next()) {
				// int id = set.getInt("id");
				String name = set.getString("name");

				PlaceType placeType = PlaceType.fromString(name);
				result.add(placeType);
			}
			if (result.isEmpty()) {
				throw new InvalidPlaceException("No such place types.");
			} else {
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: " + e.getMessage());
		}
	}

	public String saveImageURL(MultipartFile file, int placeId) throws InvalidPlaceException {
		// D://uploaded//dir5//5.jpg
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			String photoURL = IMAGE_PATH + placeId + File.separator + placeId + COUNT + EXTENTION;
			COUNT++;
			if (!file.isEmpty()) {
				File dir = new File("" + placeId);
				if (!dir.exists()) {
					dir.mkdir();
				}
				File f = new File(photoURL);
				if (!f.exists()) {
					f.createNewFile();
				}

				bis = new BufferedInputStream(file.getInputStream());
				bos = new BufferedOutputStream(new FileOutputStream(f));

				byte[] buffer = new byte[1024];

				while (bis.read(buffer) != -1) {
					bos.write(buffer);
				}

				return photoURL;
			}
			throw new InvalidPlaceException("Invalid photo.");
		} catch (IOException e) {
			throw new InvalidPlaceException("Can't create a file.", e);
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				throw new InvalidPlaceException("Can't create a file.", e);
			}

		}
	}
}
