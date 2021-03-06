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
import com.airbnb.model.db.DBConnection;
import com.airbnb.model.place.Place.PlaceType;
import com.airbnb.model.user.UserDAO;

@Component
public class PlaceDAO implements IPlaceDAO {
	private static final double DEFAULT_MIN_PLACE_PRICE = 20.0;
	private static final double DEFAULT_MAX_PLACE_PRICE = 500.0;
	private static final String DEFAULT_FILTER_ORDER = "name";
	public static final String IMAGE_PATH = "D:\\uploaded";

	private static final String MIN_AND_MAX_PLACE_PRICES_SQL = "SELECT MIN(price) AS minPrice, MAX(price) maxPrice FROM place";
	private static final String FILTERED_PLACES_SQL = "SELECT pl.id, pl.name AS name, pl.price AS price, c.name AS city,  pt.name AS placeType FROM place AS pl INNER JOIN addresses AS adr ON(pl.address_id = adr.id) INNER JOIN cities AS c ON(c.id = adr.city_id) INNER JOIN placetype AS pt ON(pl.placeType_id = pt.id) INNER JOIN users AS u ON(u.id = pl.user_id) WHERE (pl.name IS NULL OR pl.name LIKE ?) AND (pl.price IS NULL OR (pl.price >= ? AND pl.price <= ?)) AND (c.name IS NULL OR c.name LIKE ?) AND (u.deleted = '0') AND";
	private static final String ALL_PLACE_TYPES = "SELECT * FROM placetype;";
	private static final String GET_ALL_PLACES = "SELECT * FROM place AS p INNER JOIN users AS u ON (p.user_id = u.id) WHERE u.deleted = '0';";
	private static final String GET_ALL_PICTURES = "SELECT path FROM pictures;";
	private static final String ADD_PLACE_SQL = "INSERT INTO place VALUES (null, ?,false,?,?,?,?,?)";
	private static final String ADD_PLACETYPE_SQL = "INSERT INTO placetype VALUES (null, ?)";
	private static final String GIVE_PLACETYPE_SQL = "SELECT * FROM placetype WHERE name= ?";
	private static final String PLACETYPE_FROM_ID_SQL = "SELECT * FROM placetype WHERE id=?";
	private static final String ADD_PICTURES = "INSERT INTO pictures VALUES(null,?,?);";
	private static final String GET_PICTURES_FOR_PLACE = "SELECT pictures.path FROM pictures WHERE pictures.place_id = ?;";
	private static final String GET_AVG_RATING = "SELECT rating FROM reservation WHERE deleted =0 AND place_id=? AND rating !=0;";
	
	// private static int COUNT = 0;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AddressDAO addressDAO;
	@Autowired
	private DBConnection dbConnection;
	private Connection connection;
	//placeId -> Place object
	private Map<Integer, Place> allPlaces = new TreeMap<>((i1, i2) -> {
		return i1.intValue() - i2.intValue();
	});

	@Autowired
	public PlaceDAO(DBConnection dbConnection) {
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


	public boolean editPlace(Place place) throws InvalidPlaceException {
		String sql = "UPDATE place SET  name=?, busied=?,address_id = ? , placetype_id = ?, user_id = ?, price=?, user_id = ?  WHERE id = ?;";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, place.getName());
			ps.setBoolean(2, place.isBusied());
			ps.setInt(3, place.getAddressID());
			int placeTypeId = givePlaceTypeId(place.getPlaceTypeName());
			ps.setInt(4, placeTypeId);
			ps.setInt(5, place.getOwnerId());
			ps.setDouble(6, place.getPrice());
			ps.setInt(7, place.getOwnerId());
			ps.setInt(8, place.getId());
			
			/*for (String imagePath : place.getPhotosURLs()) {
				PreparedStatement ps2 = connection.prepareStatement(ADD_PICTURES);
				ps2.setString(1, imagePath);
				ps2.setInt(2, place.getId());
				ps2.executeUpdate();
			}*/
			
			boolean isEdited = ( ps.executeUpdate() > 0) ? true : false;// return 0 if there is no change, 1 if there are changes
			if(isEdited) {
				this.updatePlaceInCache(place);
			}
			return isEdited;
		} catch (SQLException e) {
			throw new InvalidPlaceException("Something went wrong in DB", e);
		}
	}
	
	private void updatePlaceInCache(Place place) {
		if(place != null) {
			this.allPlaces.put(place.getId(), place);
		}
	}
	

	public PlaceDTO getDtoById(int id) throws InvalidPlaceException {
		try {
			Place place = this.placeFromId(id);
			Address address = this.addressDAO.addressFromId(place.getAddressID());
			PlaceDTO result = new PlaceDTO(id, place.getName(), place.getPlaceTypeName(), place.isBusied(),
					address.getCountry().getName(), address.getCity().getName(), address.getStreet(), address.getStreetNumber(),
					place.getPrice(), place.getDateOfPosting(),place.getOwnerId());
		
			if (result != null) {
				this.addPhotosToPlace(result);
			}

			return result;
		} catch (InvalidAddressException e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Invalid address",e);
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
		
		for (Place place : this.allPlaces.values()) {
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
			int ownerId = place.getOwnerId();
			PlaceDTO view = new PlaceDTO(id, name, placeTypeName, busied, country, city, street, streetNumber, price,
					date,ownerId);
			if (view != null) {
				this.addPhotosToPlace(view);
			}
			result.add(view);
		}
		
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
			
			place.setPhotosURLs(imagePaths);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong", e);
		}
	}
	@Override
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
	@Override
	public double getAvgRating(int id)throws InvalidPlaceException {
		try (PreparedStatement pr = connection.prepareStatement(GET_AVG_RATING)) {
			pr.setInt(1, id);
			ResultSet rs = pr.executeQuery();
		double rating=0;
		double count =0;
			while (rs.next()) {
				count++;
				rating+=rs.getDouble("rating");
			}
			if(rating>0){
				return (rating/count);
			}else{
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidPlaceException("Something went wrong", e);
		}
	}

	public List<String> getAllPhotos() throws InvalidPlaceException{
		try {
			List<String> photos = new ArrayList<>();
			Statement st = this.connection.createStatement();
			ResultSet rs = st.executeQuery(GET_ALL_PICTURES);
			while(rs.next()) {
				String path = rs.getString("path");
				String base64Encoded = this.convertFromLocalPathToBase64String(path);
				if (!base64Encoded.isEmpty()) {
					photos.add(base64Encoded);
				}
			}
			return photos;
		} catch (SQLException | FileNotFoundException | UnsupportedEncodingException e) {
			throw new InvalidPlaceException("Something went wrong with images...",e);
		} 
	}
}
