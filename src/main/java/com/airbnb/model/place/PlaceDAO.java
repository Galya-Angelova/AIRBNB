package com.airbnb.model.place;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import com.airbnb.model.user.UserDAO;

@Component
public class PlaceDAO implements IPlaceDAO {
	private static final double DEFAULT_MIN_PLACE_PRICE = 20.0;
	private static final double DEFAULT_MAX_PLACE_PRICE = 500.0;
	private static final String DEFAULT_FILTER_ORDER = "name";
	public static final String IMAGE_PATH = "D:\\uploaded";
	private static final String EXTENTION = ".jpg";

	private static final String MIN_AND_MAX_PLACE_PRICES_SQL = "SELECT MIN(price) AS minPrice, MAX(price) maxPrice FROM place";
	private static final String FILTERED_PLACES_SQL = "SELECT pl.id, pl.name AS name, pl.price AS price, c.name AS city,  pt.name AS placeType FROM place AS pl INNER JOIN addresses AS adr ON(pl.address_id = adr.id) INNER JOIN cities AS c ON(c.id = adr.city_id) INNER JOIN placetype AS pt ON(pl.placeType_id = pt.id) WHERE (pl.name IS NULL OR pl.name LIKE ?) AND (pl.price IS NULL OR (pl.price >= ? AND pl.price <= ?)) AND (c.name IS NULL OR c.name LIKE ?) AND";
	private static final String ALL_PLACE_TYPES = "SELECT * FROM placetype;";
	// private static final String ALL_PLACES = "SELECT * FROM place order by
	// name ;";
	private static final String GET_ALL_PLACES = "SELECT * FROM place;";
	// private static final String PLACE_FROM_ID = "SELECT * FROM place WHERE
	// id=?";
	private static final String GET_PLACE_FOR_USER = "SELECT * FROM place WHERE user_id = ? order by name;";
	private static final String ADD_PLACE_SQL = "INSERT INTO place VALUES (null, ?,false,?,?,?,?,?)";
	private static final String ADD_PLACETYPE_SQL = "INSERT INTO placetype VALUES (null, ?)";
	private static final String GIVE_PLACETYPE_SQL = "SELECT * FROM placetype WHERE name= ?";
	private static final String PLACETYPE_FROM_ID_SQL = "SELECT * FROM placetype WHERE id=?";
	private static final String ADD_PICTURES = "INSERT INTO pictures VALUES(null,?,?);";
	private static final String GET_PICTURES_FOR_PLACE = "SELECT pictures.path FROM pictures WHERE pictures.place_id = ?;";

	// private static int COUNT = 0;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private DBConnectionTest dbConnection;
	private Connection connection;
	//placeId -> Place object
	private Map<Integer, Place> allPlaces = new TreeMap<>((i1, i2) -> {
		return i1.intValue() - i2.intValue();
	});

	@Autowired
	public PlaceDAO(DBConnectionTest dbConnection) {
		this.dbConnection = dbConnection;
		connection = this.dbConnection.getConnection();
		Thread t = new Thread(() -> {
			try {
				Thread.sleep(500);
				getAllPlacesFromDB();
			} catch (InvalidPlaceException | InterruptedException e) {
				e.printStackTrace();
			}
		});
		t.start();
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
			ps.setDate(6, Date.valueOf(place.getDateOfPosting()));
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				place.setId(rs.getInt(1));
			}
			for (String imagePath : place.getPhotosURLs()) {
				ps = connection.prepareStatement(ADD_PICTURES);
				ps.setString(1, imagePath);
				ps.setInt(2, place.getId());
				ps.executeUpdate();
			}

			this.allPlaces.put(place.getId(), place);
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
	public void addPhotoToPlace(PlaceDTO place) throws InvalidPlaceException {
		if (place == null) {
			throw new InvalidPlaceException("Empty place.");
		}
		try (PreparedStatement ps = connection.prepareStatement(GET_PICTURES_FOR_PLACE);) {

			ps.setInt(2, place.getId());
			ResultSet rs = ps.executeQuery();
			List<String> imageURLs = new ArrayList<>();
			while (rs.next()) {
				String imagePath = rs.getString("path");
				String base64Encoded = this.convertFromLocalPathToBase64String(imagePath);
				if (!base64Encoded.isEmpty()) {
					imageURLs.add(base64Encoded);
				}
			}

			place.setPhotosURLs(imageURLs);
		} catch (SQLException e) {
			throw new InvalidPlaceException("Invalid photo.");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid photo url.");
		}
	}

	private String convertFromLocalPathToBase64String(String imagePath)
			throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File(imagePath);

		if (!file.exists()) {
			return "";
		}

		FileInputStream fis = new FileInputStream(file);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		try {
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		byte[] bytes = bos.toByteArray();

		byte[] encodeBase64 = Base64.getEncoder().encode(bytes);
		String base64Encoded = new String(encodeBase64, "UTF-8");

		return base64Encoded;
	}

	/*
	 * public String getPhotoPath(int placeId) throws InvalidPlaceException { String
	 * sql = "SELECT path FROM prictures WHERE place_id =? LIMIT 1;";
	 * 
	 * try (PreparedStatement ps = connection.prepareStatement(sql)) { ps.setInt(1,
	 * placeId); ResultSet resultSet = ps.executeQuery(); if (resultSet.next()) {
	 * return resultSet.getString("path"); } return null; } catch (SQLException e) {
	 * throw new InvalidPlaceException("Invalid photo."); } }
	 */

	public int editPlace(Place place) throws InvalidPlaceException {
		String sql = "UPDATE POSTS SET  name=?, busied=?,address_id = ? , placetype_id = ?, user_id = ?, price=?  WHERE id = ?;";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, place.getName());
			ps.setBoolean(2, place.isBusied());
			ps.setInt(3, place.getAddressID());
			int placeTypeId = givePlaceTypeId(place.getPlaceTypeName());
			ps.setInt(4, placeTypeId);
			ps.setInt(5, place.getOwnerId());
			ps.setDouble(6, place.getPrice());
			ps.setInt(7, place.getId());
			return ps.executeUpdate();// return 0 if there is no change, 1 if there are changes
		} catch (SQLException e) {
			throw new InvalidPlaceException("Something went wrong in DB", e);
		}
	}
	
	public PlaceDTO getDtoById(int id) throws InvalidPlaceException {
		try {
			Place place = this.placeFromId(id);
			Address address = this.addressDAO.addressFromId(place.getAddressID());
			PlaceDTO result = new PlaceDTO(id, place.getName(), place.getPlaceTypeName(), place.isBusied(),
					address.getCountry().getName(), address.getCity().getName(), address.getStreet(), address.getStreetNumber(),
					place.getPrice(), place.getDateOfPosting(),place.getOwnerId());
			return result;
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid address",e);
		}
		
	}

	/*
	 * public ArrayList<String> getAllPhotosForPlace(int placeId) throws
	 * InvalidPlaceException { ArrayList<String> result = new ArrayList<>(); String
	 * sql = "SELECT path FROM pictures WHERE place_id = ?; ";
	 * 
	 * try (PreparedStatement ps = connection.prepareStatement(sql)) { ps.setInt(1,
	 * placeId); ResultSet resultSet = ps.executeQuery(); while (resultSet.next()) {
	 * result.add(resultSet.getString("path")); }
	 * 
	 * return result; } catch (SQLException e) { throw new
	 * InvalidPlaceException("No photos for this place.", e); } }
	 */

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
	public List<String> getAllPlaceTypes() throws InvalidPlaceException {
		List<String> result = new ArrayList<>();
		Statement st;
		try {
			st = connection.createStatement();
			ResultSet set = st.executeQuery(ALL_PLACE_TYPES);
			while (set.next()) {
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

	/*
	 * @Override public String saveImageURL(MultipartFile file, int placeId) throws
	 * InvalidPlaceException { // D://uploaded//dir5//5.jpg BufferedInputStream bis
	 * = null; BufferedOutputStream bos = null; try { String photoURL = IMAGE_PATH +
	 * placeId + File.separator + placeId + COUNT + EXTENTION; COUNT++; if
	 * (!file.isEmpty()) { File dir = new File("" + placeId); if (!dir.exists()) {
	 * dir.mkdir(); } File f = new File(photoURL); if (!f.exists()) {
	 * f.createNewFile(); }
	 * 
	 * bis = new BufferedInputStream(file.getInputStream()); bos = new
	 * BufferedOutputStream(new FileOutputStream(f));
	 * 
	 * byte[] buffer = new byte[1024];
	 * 
	 * while (bis.read(buffer) != -1) { bos.write(buffer); }
	 * 
	 * return photoURL; } throw new InvalidPlaceException("Invalid photo."); } catch
	 * (IOException e) { throw new InvalidPlaceException("Can't create a file.", e);
	 * } finally { try { bis.close(); bos.close(); } catch (IOException e) { throw
	 * new InvalidPlaceException("Can't create a file.", e); } } }
	 */

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

	public List<PlaceDTO> getFilteredPlaces(PlaceSearchInfo filter) throws InvalidPlaceException {
		List<PlaceDTO> filteredPlaces = new ArrayList<PlaceDTO>();
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
			ps.setString(number++, filter.getCity().equalsIgnoreCase("All") ? "%%" : filter.getCity());
			for (String placeType : filter.getPlaceTypes()) {
				ps.setString(number++, placeType);
			}
			ResultSet set = ps.executeQuery();
			while (set.next()) {
				Place place = placeFromId(set.getInt("id"));
				Address address = place.getAddress();
				filteredPlaces.add(new PlaceDTO(place.getId(), place.getName(), place.getPlaceTypeName(),
						place.isBusied(), address.getCountry().getName(), address.getCity().getName(),
						address.getStreet(), address.getStreetNumber(), place.getPrice(), place.getDateOfPosting(),place.getOwnerId()));
			}
			return filteredPlaces;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Oops , something went wrong. Reason: " + e.getMessage());
		}
	}

	@Override
	public void getAllPlacesFromDB() throws InvalidPlaceException {
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
				int ownerId = set.getInt("user_id");
				double price = set.getDouble("price");
				Address address = addressDAO.addressFromId(addressId);
				LocalDate date =set.getDate("date_of_posting").toLocalDate();
				this.allPlaces.put(id,
						new Place(id, name, busied, addressId, placeTypeName, ownerId, price, address, date));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong in DB", e);
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid address", e);
		}
	}

	public Set<PlaceDTO> getAllPlaces() throws InvalidPlaceException {
		Set<PlaceDTO> result = new TreeSet<PlaceDTO>((p1, p2) -> {
			return p1.getName().compareToIgnoreCase(p2.getName());
		});
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
				int ownerId = set.getInt("user_id");
				double price = set.getDouble("price");
				Address address = addressDAO.addressFromId(addressId);
				LocalDate date = set.getDate("date_of_posting").toLocalDate();
				PlaceDTO dto = new PlaceDTO(id, name, placeTypeName, busied, address.getCountry().getName(),
						address.getCity().getName(), address.getStreet(), address.getStreetNumber(), price, date,ownerId);
				if (dto != null) {
					this.addPhotosToPlace(dto);
				}
				result.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong in DB", e);
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid address", e);
		}
		/*for (Place place : this.allPlaces.values()) {
			int id = place.getId();
			String name = place.getName();
			boolean busied = place.isBusied();
			String placeTypeName = place.getPlaceTypeName();
			double price = place.getPrice();
			Address address = place.getAddress();
			String country = address.getCountry().getName();
			String city = address.getCity().getName();
			String street = address.getStreet();
			int streetNumber = address.getStreetNumber();
			LocalDate date = place.getDateOfPosting();
			PlaceDTO view = new PlaceDTO(id, name, placeTypeName, busied, country, city, street, streetNumber, price,
					date);
			if (view != null) {
				this.addPhotosToPlace(view);
			}
			result.add(view);
		}*/
		
		return result;
	}

	@Override
	public Place placeFromId(int id) throws InvalidPlaceException {
		if (this.allPlaces.containsKey(id)) {
			return this.allPlaces.get(id);
		} else {
			throw new InvalidPlaceException("There is no place with that id!");
		}
	}

	@Override
	public List<PlaceDTO> gettAllPlacesForUser(int userId) throws InvalidPlaceException {
		List<PlaceDTO> result = new ArrayList<>();
		if (userId <= 0) {
			throw new InvalidPlaceException("Invalid user id.");
		}
		try {
			List<Integer> userPlaces= userDAO.getUserPlacesByUserId(userId);
			for(Integer id: userPlaces) {
				PlaceDTO dto = this.getDtoById(id);
//				PlaceDTO dto = new PlaceDTO(id, placeObject.getName(), placeObject.getPlaceTypeName(), placeObject.isBusied(), placeObject.getAddress().getCountry().getName(),  placeObject.getAddress().getCity().getName(),  placeObject.getAddress().getStreet(),  placeObject.getAddress().getStreetNumber(), placeObject.getPrice(), placeObject.getDateOfPosting());
				if (dto != null) {
					this.addPhotosToPlace(dto);
				}
				result.add(dto);
			}
		} catch (InvalidUserException e1) {
			e1.printStackTrace();
			throw new InvalidPlaceException("Invalid place for user",e1);
		}
		return result;

		/*
		 * for (Place place : this.allPlaces.values()) { if (place.getOwnerId() ==
		 * userId) { int id = place.getId(); String name = place.getName(); boolean
		 * busied = place.isBusied(); String placeTypeName = place.getPlaceTypeName();
		 * double price = place.getPrice(); Address address = place.getAddress(); String
		 * country = address.getCountry().getName(); String city =
		 * address.getCity().getName(); String street = address.getStreet(); int
		 * streetNumber = address.getStreetNumber(); LocalDate date =
		 * place.getDateOfPosting(); PlaceDTO view = new PlaceDTO(id, name,
		 * placeTypeName, busied, country, city, street, streetNumber, price, date); if
		 * (view != null) { this.addPhotosToPlace(view); } result.add(view); } } return
		 * result;
		 */

		/*try (PreparedStatement ps = connection.prepareStatement(GET_PLACE_FOR_USER)) {
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
				LocalDate date = rs.getDate("date_of_posting").toLocalDate();
				PlaceDTO view = new PlaceDTO(id, name, placeTypeName, busied, country, city, street, streetNumber,
						price, date);
				if (view != null) {
					this.addPhotosToPlace(view);
				}
				result.add(view);
			}

			return result;
		} catch (

		SQLException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong", e);
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong with address", e);
		}
*/
	}

	private void addPhotosToPlace(PlaceDTO place) throws InvalidPlaceException {
		if (place == null) {
			throw new InvalidPlaceException("Empty place.");
		}

		try (PreparedStatement pr = connection.prepareStatement(GET_PICTURES_FOR_PLACE)) {
			pr.setInt(1, place.getId());
			ResultSet rs = pr.executeQuery();
			List<String> imagePaths = new ArrayList<>();

			while (rs.next()) {
				String imagePath = rs.getString("path");

				String base64Encoded = this.convertFromLocalPathToBase64String(imagePath);
				// if image does not exist return empty string and continue;
				if (!base64Encoded.isEmpty()) {
					imagePaths.add(base64Encoded);
				}

			}
			// int id = place.getId();
			/*
			 * Place placeObject = this.placeFromId(id);
			 * placeObject.setPhotosURLs(imagePaths);
			 */
			place.setPhotosURLs(imagePaths);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong", e);
		}
	}

	public void saveFileToDisk(Place place, MultipartFile f, String randomUUIDString) throws IOException {
		String dirPath = IMAGE_PATH + "\\" + randomUUIDString + "\\";

		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		String fullPath = dirPath + f.getOriginalFilename();
		File convFile = new File(fullPath);
		convFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(f.getBytes());
		fos.close();

		place.addPhotoURL(fullPath);
	}

}
