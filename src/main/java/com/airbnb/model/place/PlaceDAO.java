package com.airbnb.model.place;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.airbnb.exceptions.InvalidAddressException;
import com.airbnb.exceptions.InvalidPlaceException;
import com.airbnb.exceptions.InvalidUserException;
import com.airbnb.model.address.Address;
import com.airbnb.model.address.AddressDAO;
import com.airbnb.model.db.DBConnectionTest;
import com.airbnb.model.place.Place.PlaceType;
import com.airbnb.model.user.User;

@Component
public class PlaceDAO implements IPlaceDAO {
	private static final double DEFAULT_MIN_PLACE_PRICE = 20.0;
	private static final double DEFAULT_MAX_PLACE_PRICE = 500.0;
	private static final String DEFAULT_FILTER_ORDER = "name";
	public static final String IMAGE_PATH = "D:\\uploaded\\dir";
	private static final String EXTENTION = ".jpg";
	
	private static final String MIN_AND_MAX_PLACE_PRICES_SQL = "SELECT MIN(price) AS minPrice, MAX(price) maxPrice from place";
	private static final String FILTERED_PLACES_SQL = "SELECT pl.id, pl.name AS name, pl.price AS price, c.name AS city,  pt.name AS placeType FROM place AS pl INNER JOIN addresses AS adr ON(pl.address_id = adr.id) INNER JOIN cities AS c ON(c.id = adr.city_id) INNER JOIN placetype AS pt ON(pl.placeType_id = pt.id) WHERE (pl.name IS NULL OR pl.name LIKE ?) AND (pl.price IS NULL OR (pl.price >= ? AND pl.price <= ?)) AND (c.name IS NULL OR c.name LIKE ?) AND";
	private static final String ALL_PLACE_TYPES = "SELECT * FROM placetype;";
	private static final String ALL_PLACES = "SELECT * FROM place order by name ;";
	private static final String GET_ALL_PLACES = "SELECT * FROM place;";
	private static final String PLACE_FROM_ID = "SELECT * FROM place WHERE id=?";
	private static final String GET_PLACE_FOR_USER = "SELECT * FROM place WHERE user_id = ?;";
	private static final String ADD_PLACE_SQL = "INSERT INTO place VALUES (null, ?,false,?,?,?,?)";
	private static final String ADD_PLACETYPE_SQL = "INSERT INTO placetype VALUES (null, ?)";
	private static final String GIVE_PLACETYPE_SQL = "SELECT * FROM placetype WHERE name= ?";
	private static final String PLACETYPE_FROM_ID_SQL = "SELECT * FROM placetype WHERE id=?";
	private static final String ADD_PICTURES = "INSERT INTO pictures VALUES(null,?,?);";
	
	
	
	private static int COUNT = 0;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private DBConnectionTest dbConnection;
	private Connection connection;
	private Map<Integer, Place> allPlaces = new HashMap<>();

	@Autowired
	public PlaceDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
		try {
			fillFromDB();
		} catch (InvalidPlaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			ps.setDouble(5, place.getPrice());
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
		} finally {
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
	/*
	 * @Override public Place placeFromId(int placeId) throws InvalidPlaceException
	 * { try (PreparedStatement ps = connection.prepareStatement(PLACE_FROM_ID)) {
	 * ps.setInt(1, placeId);
	 * 
	 * ResultSet rs = ps.executeQuery();
	 * 
	 * if (rs.next()) {
	 * 
	 * int id = rs.getInt("id"); String name = rs.getString("name"); int addressId =
	 * rs.getInt("address_id"); boolean busied = rs.getBoolean("busied"); int
	 * placeTypeId = rs.getInt("placeType_id"); String placeTypeName =
	 * placeTypeFromId(placeTypeId); int userId = rs.getInt("user_id"); double price
	 * = rs.getDouble("price"); // int address_id = rs.getInt("locations_id");
	 * 
	 * return new Place(id, name, busied, addressId, placeTypeName, userId,price); }
	 * 
	 * throw new InvalidPlaceException("There is no place with that id!"); } catch
	 * (SQLException e) { // e.printStackTrace(); throw new
	 * InvalidPlaceException("Invalid statement", e); } }
	 */

	@Override
	public List<String> getAllPlaceTypes() throws InvalidPlaceException {
		List<String> result = new ArrayList<>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_PLACE_TYPES);
			while (set.next()) {
				// int id = set.getInt("id");
				String name = set.getString("name");
				result.add(name);
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
	@Override
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

	@Override
	public PlaceSearchInfo getDefaultFilter() throws InvalidPlaceException {
		String orderBy = DEFAULT_FILTER_ORDER;
		List<String> placeTypes = this.getAllPlaceTypes();
		try (Statement st = connection.createStatement()) {
			ResultSet set = st.executeQuery(MIN_AND_MAX_PLACE_PRICES_SQL);
			double minPrice = 0;
			double maxPrice = 0;
			if (set.next()) {
				minPrice = set.getDouble("minPrice");
				maxPrice = set.getDouble("maxPrice");
			}
			if (maxPrice < DEFAULT_MAX_PLACE_PRICE) {
				if (minPrice > DEFAULT_MIN_PLACE_PRICE) {
					return new PlaceSearchInfo(null, null, placeTypes, DEFAULT_MIN_PLACE_PRICE, DEFAULT_MAX_PLACE_PRICE,
							true, orderBy);
				} else {
					return new PlaceSearchInfo(null, null, placeTypes, set.getDouble("minPrice"),
							DEFAULT_MAX_PLACE_PRICE, true, orderBy);
				}
			} else {
				if (minPrice > DEFAULT_MIN_PLACE_PRICE) {
					return new PlaceSearchInfo(null, null, placeTypes, DEFAULT_MIN_PLACE_PRICE,
							set.getDouble("maxPrice"), true, orderBy);
				} else {
					return new PlaceSearchInfo(null, null, placeTypes, set.getDouble("minPrice"),
							set.getDouble("maxPrice"), true, orderBy);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid statement", e);
		}

	}

	@Override
	public List<Place> getAllPlacesForSearch() throws InvalidPlaceException {
		List<Place> places = new ArrayList<>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_PLACES);
			while (set.next()) {
				int placeTypeID = set.getInt("placeType_id");
				String placeTypeName = this.placeTypeFromId(placeTypeID);
				places.add(new Place(set.getInt("id"), set.getString("name"), set.getBoolean("busied"),
						set.getInt("address_id"), placeTypeName, set.getInt("user_id"),set.getDouble("price")));
			}
			if (places.isEmpty()) {
				throw new InvalidPlaceException("There are no cities.");
			}
			return places;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: " + e.getMessage());
		}
	}

	@Override
	public List<Place> getFilteredPlaces(PlaceSearchInfo filter) throws InvalidPlaceException {
		List<Place> filteredPlaces = new ArrayList<Place>();
		StringBuffer sql = new StringBuffer(FILTERED_PLACES_SQL);
		if (filter.getPlaceTypes().isEmpty()) {
			sql.append("(pt.name IS NULL or pt.name like \"%%\")");
		} else {
			sql.append("(pt.name IS NULL");
			for (String placeType : filter.getPlaceTypes()) {
				sql.append(" OR pt.name LIKE ?");
			}
			sql.append(")");
		}
		sql.append(" ORDER BY " + filter.getOrderedBy() + " " + (filter.getIsAscending() ? "ASC" : "DESC"));
		try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
			int number = 1;
			ps.setString(number++, filter.getPlaceName() != null ? "%" + filter.getPlaceName() + "%" : "%%");
			ps.setDouble(number++, filter.getMinPriceForNight());
			ps.setDouble(number++, filter.getMaxPriceForNight());
			ps.setString(number++, filter.getCity() != null ? "%" + filter.getCity() + "%" : "%%");
			for (String placeType : filter.getPlaceTypes()) {
				ps.setString(number++, placeType);
			}
			ResultSet set = ps.executeQuery();
			while (set.next()) {
				filteredPlaces.add(placeFromId(set.getInt("id")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: " + e.getMessage());
		}
		return null;
	}

	@Override
	public void fillFromDB() throws InvalidPlaceException {
		try {
			Statement statement = connection.createStatement();
			ResultSet set = statement.executeQuery(GET_ALL_PLACES);
			while (set.next()) {
				int id = set.getInt("id");
				String name = set.getString("name");
				boolean busied = set.getBoolean("busied");
				int addressId = set.getInt("address_id");
				int placeTypeId = set.getInt("placeType_id");
				String placeTypeName = placeTypeFromId(placeTypeId);
				int ownerId = set.getInt("price");
				double price = set.getDouble("price");

				this.allPlaces.put(id, new Place(id, name, busied, addressId, placeTypeName, ownerId, price));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong in DB", e);
		}
	}
	
	@Override
	public List<PlaceDTO> getAllPlaces() throws InvalidPlaceException {
		List<PlaceDTO> result = new ArrayList<>();
		this.allPlaces.clear();
		fillFromDB();
		try {
			for (Place place : this.allPlaces.values()) {

				int id = place.getId();
				String name = place.getName();
				int addressId = place.getAddressID();
				boolean busied = place.isBusied();
				String placeTypeName = place.getPlaceTypeName();
				double price = place.getPrice();
				// int address_id = rs.getInt("locations_id");
				Address address = this.addressDAO.addressFromId(addressId);
				String country = address.getCountry().getName();
				String city = address.getCity().getName();
				String street = address.getStreet();
				int streetNumber = address.getStreetNumber();
				result.add(new PlaceDTO(id, name, placeTypeName, busied, country, city, street, streetNumber, price));
			}
			return result;
		} catch (InvalidAddressException e) {
			throw new InvalidPlaceException("Invalid address", e);
		}
	}

	@Override
	public Place placeFromId(int id) throws InvalidPlaceException {
		this.allPlaces.clear();
		fillFromDB();
		if (this.allPlaces.containsKey(id)) {
			return this.allPlaces.get(id);
		} else {
			throw new InvalidPlaceException("There is no place with that id!");
		}
	}
	
	@Override
	public List<PlaceDTO> gettAllPlacesForUser(int userId) throws InvalidPlaceException {
		List<PlaceDTO> result = new ArrayList<>();
		try (PreparedStatement ps = connection.prepareStatement(GET_PLACE_FOR_USER)) {
			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				int id = rs.getInt("id");
				String name = rs.getString("name");
				int addressId = rs.getInt("address_id");
				boolean busied = rs.getBoolean("busied");
				int placeTypeId = rs.getInt("placeType_id");
				String placeTypeName = placeTypeFromId(placeTypeId);
				double price = rs.getDouble("price");
				Address address = this.addressDAO.addressFromId(addressId);
				String country = address.getCountry().getName();
				String city = address.getCity().getName();
				String street = address.getStreet();
				int streetNumber = address.getStreetNumber();
				result.add(new PlaceDTO(id, name, placeTypeName, busied, country, city, street, streetNumber, price));
			}
			return result;
		} catch (SQLException e) {
			throw new InvalidPlaceException("No places for this user.", e);
		} catch (InvalidAddressException e) {
			throw new InvalidPlaceException("Invalid address.", e);
		}
	}

}
