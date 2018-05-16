package com.as.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.as.app.beans.Car;
import com.as.app.beans.ListingStatus;
import com.as.app.beans.Model;
import com.as.app.beans.Part;
import com.as.app.beans.PartGroup;
import com.as.app.beans.Wholesaler;
import com.as.app.price.util.ActivePart;
import com.as.app.util.StringUtil;

public class CarPartsDao {
	
	private static final String SELECT_FROM_MAKE = "SELECT * FROM make";
	private static final String INSERT_INTO_MAKE = "INSERT INTO make(name) values(?)";
	private static final String INSERT_PHOTO_ID = "INSERT into tm_partphotoid values (?,?)";
	private static final String SELECT_PHOTO_IDS = "SELECT wholesalerpartno, photoid FROM tm_partphotoid";
	private static final String TRUNCATE_PART_TABLE = "TRUNCATE TABLE part";
	private static final String MODEL_YEAR = "MODEL-YEAR: ";
	private static final String INSERT_MODEL = "INSERT INTO model(makeid, name, subname, year, photoname) VALUES(?,?,?,?,?)";
	private static final String SELECT_MODEL_ID = "SELECT id FROM model WHERE makeid=? and TRIM(name)=? and TRIM(subname)=? and TRIM(year)=?";
	private static final String SELECT_MAKE_ID = "SELECT id from make WHERE name = ?";
	private static final String SELECT_WHOLESALER_PART_NO = "SELECT wholesalerpartno FROM part where wholesalerpartno = ?";
	private static final String INSERT_PART = "INSERT INTO part(modelid, wholesaleprice, retailprice, partname, source, stock, photoname, wholesalerpartno, wholesalerid, linkid, isactive, details) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE_PART = "UPDATE part SET modelid = ?, wholesaleprice = ?, retailprice = ?, partname = ?, source = ?, stock = ?, photoname = ?, wholesalerid = ?, linkid = ?, details=? where wholesalerpartno = ?";
	private static final String UPDATE_PART_WHOLESALEPRICE = "UPDATE part SET wholesaleprice = ? where wholesalerpartno = ?";
	private static final String SELECT_PARTS_NOT_READY_FOR_RELIST = "SELECT DISTINCT wholesalerpartno FROM tm_listeditems where status IN ('LISTED', 'SOLD')";
	private static final String SELECT_RELISTABLE_ACTIVE_PARTS = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details " +
			"FROM part p, make m, model o " +
			"WHERE p.isactive = 1 and p.modelid = o.id and o.makeid = m.id and p.stock > 2 " +
			"and p.wholesalerpartno NOT IN (" + SELECT_PARTS_NOT_READY_FOR_RELIST + ")"   +
//			" and p.wholesalerpartno not in (select temp from temp) " +
//			" and p.wholesalerpartno in ('COR1103', 	'COR1104', 'COR1102', 'COR1101') " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";
	private static final String SELECT_RELISTABLE_ACTIVE_PARTS2 = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details " +
			"FROM part p, make m, model o " +
			"WHERE p.isactive = 1 and p.modelid = o.id and o.makeid = m.id and p.stock > 2 " +
			"and p.wholesalerpartno NOT IN (" + SELECT_PARTS_NOT_READY_FOR_RELIST + ")"   +
			" and p.wholesalerpartno in (select temp from temp) " +
//			" and p.wholesalerpartno in ('HA0405R', 'HA0405L') " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";	
	private static final String SELECT_ACTIVE_PARTS = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details  " +
			"FROM part p, make m, model o " +
			"WHERE p.isactive = true and p.modelid = o.id and o.makeid = m.id " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";
	
	private static final String SELECT_PARTS_BY_PARTNUMBER = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details  " +
			"FROM part p, make m, model o " +
			"WHERE p.modelid = o.id and o.makeid = m.id and p.wholesalerpartno in (%s) " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";
	
	private static final String SELECT_ALL_PARTS = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details, g.groupname, g.groupid  " +
			"FROM part p, make m, model o, tm_partgroup pg, tm_group g " +
			"WHERE p.modelid = o.id and o.makeid = m.id " +
			"and p.wholesalerpartno = pg.partnumber and pg.groupid = g.groupid " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";
	
	private static final String SELECT_ALL_PARTS_BY_MAKE = "SELECT " +
			"p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive, " +
			"m.name, o.name, o.subname, o.year, p.details, w.name, w.code " +
			"FROM part p, make m, model o, wholesaler w " +
			"WHERE p.modelid = o.id and o.makeid = m.id and m.id = ? and w.id = p.wholesalerid " +
			"ORDER BY m.name, o.name, o.year, p.wholesalerpartno";	
	
	
	private static final String INSERT_LISTED_ITEM =  "INSERT into tm_listeditems(wholesalerpartno, listingid, status) VALUES(?,?,?)";
	private static final String UPDATE_LISTED_ITEMS_STATUS = "UPDATE tm_listeditems SET status = ? WHERE listingid in (%s) and status in (%s)";
	private static final String SELECT_MODEL_NAMES = "SELECT id, name from model order by makeid, name";
	
	private static final String SELECT_PARTGROUP_WITH_LOW_STOCK = "select pg.groupid " +
				" from part p, tm_partgroup pg " +
				" where p.isactive = true and p.stock <= 2 and p.wholesalerpartno = pg.partnumber";

	private DBConfig dbConfig;
	
	public CarPartsDao(String domain) {
		this.dbConfig = new DBConfig(domain);
	}
		
	/**
	 * The assumption is there is only one user connecting to the database. This can be extracted to a 
	 * different class i.e. ConnectionManager if needed.
	 * @return
	 */
	public Connection getConnection() {
		
		Connection con = null;
		try {

			Class.forName(this.dbConfig.getClassStr());
			con = DriverManager.getConnection(this.dbConfig.getUrl(), this.dbConfig.getUser(), this.dbConfig.getPwd());
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return con;

	}
	
	public int updateItemsStatusTo(ArrayList<Long> listingIds, ListingStatus toStatus, ArrayList<ListingStatus> fromStatus) {		
		Connection con = null;
		try {
			con = getConnection();
			String listingIdsStr = StringUtil.toDelimitedString(listingIds, ',');

			ArrayList<String> fromStatusStrArr = StringUtil.encloseStringsWith(fromStatus, "'");
			String fromStatusStr = StringUtil.toDelimitedString(fromStatusStrArr, ',');
		    String query = String.format(UPDATE_LISTED_ITEMS_STATUS, listingIdsStr, fromStatusStr);
		    System.out.println(query);
			PreparedStatement pstmt = con.prepareStatement(query);
		    pstmt.setString(1, toStatus.toString());
		    
		    int executeUpdate = pstmt.executeUpdate();
		    System.out.println("DAO updated: " + executeUpdate);
			return executeUpdate;
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return -1;
	}
	
	public HashMap<Car, ArrayList<Part>> getPartsByPartnumber(ArrayList<String> partNumbers) {		
		ArrayList<String> partNumbers2 = StringUtil.encloseStringsWith(partNumbers, "'");
		String partNumberStr = StringUtil.toDelimitedString(partNumbers2, ',');
	    String query = String.format(SELECT_PARTS_BY_PARTNUMBER, partNumberStr);
		return getParts(query);
	}
	
	public HashMap<Car, ArrayList<Part>> getPartsByPartnumberInTemp() {
		 String query = String.format(SELECT_PARTS_BY_PARTNUMBER, "SELECT temp from temp");
		 return getParts(query);
	}
	
	public int updateListedItem(Long listingId, int biddersWatchers, float maxBid, int viewCount) {		
		Connection con = null;
		try { 
			con = getConnection();
			String query = "Update tm_listeditems set bidderswatchers = ?, maxbid = ? , viewcount=? where listingid = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
		    pstmt.setInt(1, biddersWatchers);
		    pstmt.setFloat(2, maxBid);
		    pstmt.setInt(3, viewCount);
		    pstmt.setLong(4, listingId);
		   
		    
		    int executeUpdate = pstmt.executeUpdate();
		   // System.out.println("DAO updated: " + executeUpdate);
			return executeUpdate;
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return -1;
	}
	
	public HashMap<String, Integer> getActivePartsStockLevelByWholesaler(int wholesalerId) {
		HashMap<String, Integer> partStocks = new HashMap<String, Integer>();
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("Select wholesalerpartno, stock from part where isactive = true and wholesalerid = ?");
			pstmt.setInt(1, wholesalerId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String partnum = rs.getString(1);
				int stock = rs.getInt(2);
				partStocks.put(partnum, stock);
				
			} 
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return partStocks;		
	}
		
		
	
	private static final String STOCK_CHECK_SQL = 
			"SELECT a.makename, a.modelname, a.submodel, a.partnumber, a.stock, b.listingid, b.status " +
			"FROM " +
			" 	(select m.name as makename, o.name as modelname, o.subname as submodel, p.wholesalerpartno as partnumber, p.stock as stock " +
			" 		from part p, make m, model o " +
			" 		where p.modelid = o.id and o.makeid = m.id and p.stock <= 2 and p.isactive = true) as a " +
			"LEFT JOIN " +
			"	(select wholesalerpartno, listingid, status from tm_listeditems where status in ('LISTED', 'SOLD')) b " +
			"ON a.partnumber = b.wholesalerpartno " +
			"ORDER BY b.listingid, a.partnumber"; 
	
	public StringBuffer checkLowStocks() {
		Connection con = null;
		StringBuffer sb = new StringBuffer();
		try{
			con = getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(STOCK_CHECK_SQL);
			int cnt = 0;

			while (rs.next()) {					
				sb.append("Stock level: ").append(rs.getInt(5)).append("  - ");
				sb.append(rs.getString(1)).append(" ");
				sb.append(rs.getString(2)).append(" ");
				sb.append(rs.getString(3)).append(" ");
				sb.append(rs.getString(4)).append(" ");
				if (rs.getLong(6) > 0) {
					sb.append(" (TRADEME LISTING: ").append(rs.getLong(6)).append(" - ");
					sb.append(rs.getString(7)).append(")");
				}	
				sb.append("\n");
				cnt++;
			} 
			
			if (cnt == 0) {
				sb.append("STOCK LEVELS are OK.");
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return sb;
	}
	
	public int saveListedItem(String partsId, long listingId, ListingStatus status) {
		Connection con = null;
		try {
			con = getConnection();
		    PreparedStatement pstmt = con.prepareStatement(INSERT_LISTED_ITEM);
		    pstmt.setString(1, partsId);
		    pstmt.setLong(2, listingId);
		    pstmt.setString(3, status.toString());
		    
		    return pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		return -1;

	}
	
	public int savePhotoId(String partsId, int photoId) {
		Connection con = null;
		try {
			con = getConnection();
		    PreparedStatement pstmt = con.prepareStatement(INSERT_PHOTO_ID);
		    pstmt.setString(1, partsId);
		    pstmt.setInt(2, photoId);
		    
		    return pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
		return -1;

	}
	
	public void getAllMakes() {
		Connection con = null;
		try{
		con = getConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SELECT_FROM_MAKE);
		
		while (rs.next()) {
			int int1 = rs.getInt(1);
			String str = rs.getString(2);
			System.out.println(int1 + " " + str);
		} //end while
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public ArrayList<Integer> getAllMakeIds() {
		Connection con = null;
		ArrayList<Integer> makeIds = new ArrayList<Integer>();
		try{
		con = getConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SELECT_FROM_MAKE);
		
		while (rs.next()) {
			makeIds.add(rs.getInt(1));
		} //end while
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return makeIds;
	}
	
	public int getMakeId(String makeName) {
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_MAKE_ID);
			pstmt.setString(1, makeName);	    
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				System.out.println("makeId: " + id);
				return id;
			} 
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -1;		
	}

	public int getModelId (final Car car){
		Connection con = null;
		try{
			con = getConnection();
			final int makeId = getMakeId(car.getMake());
			PreparedStatement pstmt = con.prepareStatement(SELECT_MODEL_ID);
			pstmt.setInt(1, makeId);
			pstmt.setString(2, car.getModel().getName().trim());
			pstmt.setString(3, car.getModel().getSubName().trim());
			pstmt.setString(4, car.getYear().trim());	    
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				System.out.println("modelId: " + id);
				return id;
			} 
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -1;

	}
	
	public int saveMake(String make) {
		Connection con = null;
		int execute = -1;
		try {
			con = getConnection();
		    PreparedStatement pstmt = con.prepareStatement(INSERT_INTO_MAKE);
		    pstmt.setString(1, make.trim());
		    
		    execute = pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	    if (execute > 0) {
	    	return getMakeId(make);
	    }

		return -1;

	}
	
	public int saveModel(final Car car) {
		
		Connection con = null;
		int execute = -1;
		try {
			con = getConnection();
		    int makeId = getMakeId(car.getMake());
		    System.out.println("getting MakeId: " + makeId);
		    if (makeId == -1) {
		    	//save new make
		    	System.out.println("saving new Make");
		    	makeId = saveMake(car.getMake());
		    	System.out.println("new MakeId: " + makeId);
		    }
		    PreparedStatement pstmt = con.prepareStatement(INSERT_MODEL);
		    pstmt.setInt(1, makeId);
		    pstmt.setString(2, car.getModel().getName().trim());
		    pstmt.setString(3, car.getModel().getSubName().trim());
		    pstmt.setString(4, car.getYear().trim());
		    pstmt.setString(5, car.getModel().getPhotoName().trim());
		    
		    execute = pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	    if (execute > 0) {
	    	return getModelId(car);
	    }

		return -1;
	}
	
	public boolean isPartExist(String partNumber) {
		Connection con = null;		
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_WHOLESALER_PART_NO);
			pstmt.setString(1, partNumber.trim());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return false;
	}
	
	private static String SELECT_RETAIL_PRICE = "Select retailprice from part where wholesalerpartno = ?";
	public float getPartRetailPrice(String partNumber) {
		Connection con = null;		
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_RETAIL_PRICE);
			pstmt.setString(1, partNumber.trim());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getFloat(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return 0;	
	}
	
	public int updatePart(final Part part) {
		Connection con = null;		
		try {
			con = getConnection();
		    // Prepare a statement to update a record
		    PreparedStatement pstmt = con.prepareStatement(UPDATE_PART);
		    pstmt.setInt(1, part.getModelId());
		    pstmt.setFloat(2, part.getWholesalePrice());
		    pstmt.setFloat(3, part.getRetailPrice());
		    pstmt.setString(4, part.getPartName());
		    pstmt.setString(5, part.getSource());
		    pstmt.setInt(6, part.getStockLevel());
		    pstmt.setString(7, part.getPhotoName());
		    pstmt.setInt(8, part.getWholesalerId());
		    pstmt.setString(9, part.getLinkId());
		    pstmt.setString(10, part.getDetails());
		    pstmt.setString(11, part.getPartNumber());
		    
		    return pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return -1;	
	}
	
	public int updatePartWholesalePrice(final String partNumber, final float wholeSalePrice) {
		Connection con = null;		
		try {
			con = getConnection();
		    // Prepare a statement to update a record
		    PreparedStatement pstmt = con.prepareStatement(UPDATE_PART_WHOLESALEPRICE);
		    pstmt.setFloat(1, wholeSalePrice);
		    pstmt.setString(2, partNumber);
		    
		    return pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return -1;
	}
	
	public int saveParts(final Part part) {
		Connection con = null;		
		try {
			con = getConnection();
		    // Prepare a statement to insert a record
		    PreparedStatement pstmt = con.prepareStatement(INSERT_PART);
		    pstmt.setInt(1, part.getModelId());
		    pstmt.setFloat(2, part.getWholesalePrice());
		    pstmt.setFloat(3, part.getRetailPrice());
		    pstmt.setString(4, part.getPartName().trim());
		    pstmt.setString(5, part.getSource().trim());
		    pstmt.setInt(6, part.getStockLevel());
		    pstmt.setString(7, part.getPhotoName().trim());
		    pstmt.setString(8, part.getPartNumber().trim());
		    pstmt.setInt(9, part.getWholesalerId());
		    pstmt.setString(10, part.getLinkId().trim());
		    pstmt.setBoolean(11, part.isActive());
		    pstmt.setString(12, part.getDetails());
		    
		    return pstmt.executeUpdate();
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return -1;	
	}
	
	public int clearParts() {

		Connection con = null;
		try{
		con = getConnection();
		Statement stmt = con.createStatement();
		return stmt.executeUpdate(TRUNCATE_PART_TABLE);
		
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	public int clearActiveParts() {

		Connection con = null;
		try{
		con = getConnection();
		Statement stmt = con.createStatement();
		return stmt.executeUpdate("Truncate table app_activeparts");
		
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -1;
	}	
	
	public HashMap<Car, ArrayList<Part>> getRelistableActiveParts2() {		
		return getParts(SELECT_RELISTABLE_ACTIVE_PARTS2);
	}
	
	public HashMap<Car, ArrayList<Part>> getRelistableActiveParts() {		
		return getParts(SELECT_RELISTABLE_ACTIVE_PARTS);
	}

	public HashMap<Car, ArrayList<Part>> getActiveParts() {		
		return getParts(SELECT_ACTIVE_PARTS);
	}
	
	public HashMap<Car, ArrayList<Part>> getAllParts() {		
		return getParts(SELECT_ALL_PARTS);
	}
	
	
	private HashMap<Car, ArrayList<Part>> getParts(final String query) {
		Connection con = null;
		HashMap<Car, ArrayList<Part>> carparts = new HashMap<Car, ArrayList<Part>>();
		Car car = null;
		ArrayList<Part> partList = new ArrayList<Part>();
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {				
				Part part = new Part();
				//p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive
				part.setModelId(rs.getInt(1));
				part.setWholesalePrice(rs.getFloat(2));
				part.setRetailPrice(rs.getFloat(3));
				part.setPartName(rs.getString(4));
				part.setSource(rs.getString(5));
				part.setStockLevel(rs.getInt(6));
				part.setPhotoName(rs.getString(7));
				part.setPartNumber(rs.getString(8));
				part.setWholesalerId(rs.getInt(9));
				part.setLinkId(rs.getString(10));
				part.setActive(rs.getBoolean(11));
				part.setDetails(null==rs.getString(16)? "" : rs.getString(16));
				
				
				PreparedStatement pstmt2 = con.prepareStatement("select groupid, groupname from tm_group where groupid in (select groupid from tm_partgroup where partnumber =?)");
				pstmt2.setString(1, part.getPartNumber());
				ResultSet rs2 = pstmt2.executeQuery();
				if (rs2.next()) {
						PartGroup pg = new PartGroup();
						pg.setGroupId(rs2.getInt(1));
						pg.setGroupName(rs2.getString(2));
						part.setPartGroup(pg);
				} else {
						part.setPartGroup(null);
				}
				
				if (car == null) {
					car = new Car();
					car.setMake(rs.getString(12));
					car.setModel(new Model(rs.getString(13).trim(), rs.getString(14).trim()));
					car.setYear(rs.getString(15));	
					partList = new ArrayList<Part>();
				} else if (!(car.getMake().equalsIgnoreCase(rs.getString(12)) 
						&& car.getModel().getName().trim().equalsIgnoreCase(rs.getString(13).trim()) 
						&& car.getModel().getSubName().trim().equalsIgnoreCase(rs.getString(14).trim())
						&& car.getYear().equalsIgnoreCase(rs.getString(15)))){
										
					carparts.put(car, partList);
					partList = new ArrayList<Part>();
					
					car = new Car();
					car.setMake(rs.getString(12));
					car.setModel(new Model(rs.getString(13).trim(), rs.getString(14).trim()));
					car.setYear(rs.getString(15));	
					
				}	
				
				partList.add(part);				
				
			} 
			carparts.put(car, partList);
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return carparts;

	}

	public HashMap<Car, ArrayList<Part>> getAllPartsByMake(Integer makeid) {		
		Connection con = null;
		HashMap<Car, ArrayList<Part>> carparts = new HashMap<Car, ArrayList<Part>>();
		Car car = null;
		ArrayList<Part> partList = new ArrayList<Part>();
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_ALL_PARTS_BY_MAKE);
			pstmt.setInt(1, makeid);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {				
				Part part = new Part();
				//p.modelid, p.wholesaleprice, p.retailprice, p.partname, p.source, p.stock, p.photoname, p.wholesalerpartno, p.wholesalerid, p.linkid, p.isactive
				part.setModelId(rs.getInt(1));
				part.setWholesalePrice(rs.getFloat(2));
				part.setRetailPrice(rs.getFloat(3));
				part.setPartName(rs.getString(4));
				part.setSource(rs.getString(5));
				part.setStockLevel(rs.getInt(6));
				part.setPhotoName(rs.getString(7));
				part.setPartNumber(rs.getString(8));
				part.setWholesalerId(rs.getInt(9));
				part.setLinkId(rs.getString(10));
				part.setActive(rs.getBoolean(11));
				part.setDetails(null==rs.getString(16)? "" : rs.getString(16));
				
				if (car == null) {
					car = new Car();
					car.setMake(rs.getString(12));
					car.setModel(new Model(rs.getString(13).trim(), rs.getString(14).trim()));
					car.setYear(rs.getString(15));	
					partList = new ArrayList<Part>();
				} else if (!(car.getMake().equalsIgnoreCase(rs.getString(12)) 
						&& car.getModel().getName().trim().equalsIgnoreCase(rs.getString(13).trim()) 
						&& car.getModel().getSubName().trim().equalsIgnoreCase(rs.getString(14).trim())
						&& car.getYear().equalsIgnoreCase(rs.getString(15)))){
										
					carparts.put(car, partList);
					partList = new ArrayList<Part>();
					
					car = new Car();
					car.setMake(rs.getString(12));
					car.setModel(new Model(rs.getString(13).trim(), rs.getString(14).trim()));
					car.setYear(rs.getString(15));	
					
				}	
				
				partList.add(part);				
				
			} 
			carparts.put(car, partList);
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return carparts;


	}

	public HashMap<Integer, Wholesaler> getAllWholesalers() {
		HashMap<Integer, Wholesaler> wholesalers = new HashMap<Integer, Wholesaler>();
		Connection con = null;
		try {
			con = getConnection();	
			PreparedStatement pstmt = con.prepareStatement("SELECT id, name, code FROM wholesaler");
		    ResultSet result = pstmt.executeQuery();
		    while(result.next()) {
		    	int wholesalerId = result.getInt(1);
				Wholesaler w = new Wholesaler(wholesalerId, result.getString(2), result.getString(3));
				wholesalers.put(wholesalerId, w);
		    }		 		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return wholesalers;
	}
	
	public ArrayList<Integer> getGroupWithLowStock() {
		ArrayList<Integer> groupList = new ArrayList<Integer>();
		Connection con = null;
		try {
			con = getConnection();	
			PreparedStatement pstmt = con.prepareStatement(SELECT_PARTGROUP_WITH_LOW_STOCK);
		    ResultSet result = pstmt.executeQuery();
		    while(result.next()) {
		    	groupList.add(new Integer(result.getInt(1)));
		    }
		 		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return groupList;
	}
	
	public HashMap<String, Integer> getPhotoIds() {
		HashMap<String, Integer> partPhotoIdMap = new HashMap<String, Integer> ();
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_PHOTO_IDS);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {				
				partPhotoIdMap.put(rs.getString(1), rs.getInt(2));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return partPhotoIdMap;
	}
	
	public ArrayList<Model> getModelNames() {
		ArrayList<Model> modelNames = new ArrayList<Model>();
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(SELECT_MODEL_NAMES);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				modelNames.add(new Model(rs.getInt(1), rs.getString(2), "", ""));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return modelNames;
	}
	
	public void updateModel(String name, String subname, String photoname, int id) {
		Connection con = null;
		String updateModel = "UPDATE model SET name = ?, subname = ? , photoname = ? WHERE id = ?";
		try {
			con = getConnection();
		
			PreparedStatement pstmt = con.prepareStatement(updateModel);
		    pstmt.setString(1, name);
		    pstmt.setString(2, subname);
		    pstmt.setString(3, photoname);
		    pstmt.setInt(4, id);
		    
		    int executeUpdate = pstmt.executeUpdate();
		    System.out.println("DAO updated: " + executeUpdate);
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void splitModelName() {
		ArrayList<Model> modelNames = getModelNames();
		ArrayList<Model> modelNamesSplit = new ArrayList<Model>();
		for (Model mName : modelNames) {
//			System.out.println(name);
			String name = mName.getName();
			String[] split = name.split(" ");
			String subname = "";
			if (split.length > 1) { 
				subname = name.substring(split[0].length() + 1, name.length());
			}
			modelNamesSplit.add(new Model(mName.getId(), split[0], subname));
			System.out.println("name: " + split[0] + " subname: "  + subname);
//			updateModel(split[0], subname, null, mName.getId());
			
		}
	}
	
	public HashMap<Integer, String> getMakeNames() {
		HashMap<Integer, String> makes = new HashMap<Integer, String>();

		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT id, name FROM make");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				makes.put(rs.getInt(1), rs.getString(2));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return makes;
		
	}

	//get distinct model names
	public ArrayList<String> getMainModelNamesByMake(int makeId) {
		ArrayList<String> modelNames = new ArrayList<String>();

		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT DISTINCT name FROM model where makeid = ?");
		    pstmt.setInt(1, makeId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				modelNames.add(rs.getString(1));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return modelNames;
		
	}
	
	public class ModelSubInfo {
		private String subname;
		private String year;
		private String photoname;
		
		public ModelSubInfo (String subname, String year, String photoname) {
			this.subname = subname;
			this.year = year;
			this.photoname = photoname;
		}
		
		public String getPhotoname() {
			return photoname;
		}

		public void setPhotoname(String photoname) {
			this.photoname = photoname;
		}


		public String getSubname() {
			return subname;
		}

		public void setSubname(String subname) {
			this.subname = subname;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}
		
	
	}
	
	//get subname and year
	public ArrayList<ModelSubInfo> getSubModelNamesAndYearByMainModel(String mainModelName) {
		ArrayList<ModelSubInfo> subModelNames = new ArrayList<ModelSubInfo>();

		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement("SELECT subname, year, photoname FROM model where TRIM(name) = TRIM(?) ");
			pstmt.setString(1, mainModelName.trim());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				subModelNames.add(new ModelSubInfo(rs.getString(1), rs.getString(2), rs.getString(3)));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return subModelNames;
	}	
	
	
	public void populateActiveParts() {
		Connection con = null;
		String sql =  "select m.name, o.name, o.subname, o.year, p.partname, p.wholesalerpartno, p.wholesaleprice, p.retailprice, p.stock "
				+ " from make m, model o, part p "
				+ " where m.id = o.makeid and o.id = p.modelid " 
				+ " and p.isactive = 1 ";
//				+ " and p.wholesalerpartno in ('D4098',  'D4001')";
		
		String insertSql = "insert into app_activeparts(make,model,submodel,year,partname, wholesaleprice,retailprice,wholesaleandgst,appprice,shippingcost,trademefee,grossprofit,netprofit,partnumber,stock,comments) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				//String make, String model, String submodel, String year, String partName, String partNumber, float wholesalePrice, float retailPrice, int stock
				ActivePart ap = new ActivePart(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getInt(9));
				
				//create insert statement
				PreparedStatement pstmt2 = con.prepareStatement(insertSql);
				pstmt2.setString(1, ap.getMake());
				pstmt2.setString(2, ap.getModel());
				pstmt2.setString(3, ap.getSubmodel());
				pstmt2.setString(4, ap.getYear());
				pstmt2.setString(5, ap.getPartName());
				pstmt2.setFloat(6, ap.getWholesalePrice());
				pstmt2.setFloat(7, ap.getRetailPrice());
				pstmt2.setFloat(8, ap.getWholesaleAndGst());
				pstmt2.setFloat(9, ap.getAppPrice());
				pstmt2.setFloat(10, ap.getShippingCost());
				pstmt2.setFloat(11, ap.getTrademeFee());
				pstmt2.setFloat(12, ap.getGrossProfit());
				pstmt2.setFloat(13, ap.getNetProfit());
				pstmt2.setString(14, ap.getPartNumber());
				pstmt2.setInt(15, ap.getStock());
				pstmt2.setString(16, ap.getComments());				
				pstmt2.executeUpdate();
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void calculateSpecificPartPrices() {
		Connection con = null;
		String sql =  "select m.name, o.name, o.subname, o.year, p.partname, p.wholesalerpartno, p.wholesaleprice, p.retailprice, p.stock "
				+ " from make m, model o, part p "
				+ " where m.id = o.makeid and o.id = p.modelid " 				
				+ " and p.wholesalerpartno in (select temp from temp)";
		
		String insertSql = "insert into app_activeparts(make,model,submodel,year,partname, wholesaleprice,retailprice,wholesaleandgst,appprice,shippingcost,trademefee,grossprofit,netprofit,partnumber,stock,comments) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try{
			con = getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {		
				//String make, String model, String submodel, String year, String partName, String partNumber, float wholesalePrice, float retailPrice, int stock
				ActivePart ap = new ActivePart(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getFloat(7), rs.getFloat(8), rs.getInt(9));
				
				//create insert statement
				PreparedStatement pstmt2 = con.prepareStatement(insertSql);
				pstmt2.setString(1, ap.getMake());
				pstmt2.setString(2, ap.getModel());
				pstmt2.setString(3, ap.getSubmodel());
				pstmt2.setString(4, ap.getYear());
				pstmt2.setString(5, ap.getPartName());
				pstmt2.setFloat(6, ap.getWholesalePrice());
				pstmt2.setFloat(7, ap.getRetailPrice());
				pstmt2.setFloat(8, ap.getWholesaleAndGst());
				pstmt2.setFloat(9, ap.getAppPrice());
				pstmt2.setFloat(10, ap.getShippingCost());
				pstmt2.setFloat(11, ap.getTrademeFee());
				pstmt2.setFloat(12, ap.getGrossProfit());
				pstmt2.setFloat(13, ap.getNetProfit());
				pstmt2.setString(14, ap.getPartNumber());
				pstmt2.setInt(15, ap.getStock());
				pstmt2.setString(16, ap.getComments());				
				pstmt2.executeUpdate();
				
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}	

	private static final String INSERT_INTO_GROUP = "insert into tm_group(groupname) values(?)";
	private static final String INSERT_INTO_PART_GROUP = "insert into tm_partgroup(groupid, partnumber) values(?,?)";
	
	public void saveGroups(final HashMap <String, ArrayList<String>> partGroups) {
		Connection con = null;		
		try {
			con = getConnection();
			
			Iterator<Entry<String, ArrayList<String>>> iterator2 = partGroups.entrySet().iterator();
			while (iterator2.hasNext())  {
				Entry<String, ArrayList<String>> next2 = iterator2.next();
				String groupName = next2.getKey();
				ArrayList<String> partNumbers = next2.getValue();
				System.out.print(groupName + " : " );
				// Prepare a statement to insert a record
				PreparedStatement pstmt = con.prepareStatement(INSERT_INTO_GROUP);
				pstmt.setString(1, groupName);
				pstmt.executeUpdate();
				String q = "Select max(groupid) from tm_group";
				ResultSet rs = con.prepareStatement(q).executeQuery();				
				if (rs.next()) {
					int groupId = rs.getInt(1);
					for (String num : partNumbers) {
						PreparedStatement pstmt2 = con.prepareStatement(INSERT_INTO_PART_GROUP);
						pstmt2.setInt(1, groupId);
						pstmt2.setString(2, num);
						pstmt2.executeUpdate();
						System.out.print(num + ", ");
					}
					System.out.println();
				}

			}
		    
		    		    
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	//*****************TEMP
	
	public static void main(String args[]) {
		CarPartsDao dao = DBManager.getCarPartsDaoInstance();
		dao.clearActiveParts();
		dao.populateActiveParts();

	}
	
	
	
//	public HashMap<Car, ArrayList<Part>> getCarPartsByMake(String make) throws IOException {
//
//		//new HashMap<Car, ArrayList<Part>>();		
//		FileInputStream fstream = new FileInputStream("data/NASA_Nissan_1346655240108.txt");
//		DataInputStream in = new DataInputStream(fstream);
//		BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		String str;
//		HashMap<Car, ArrayList<Part>> carparts = new HashMap<Car, ArrayList<Part>>();
//		Car car = null;
//		ArrayList<Part> partList = new ArrayList<Part>();
//		while ((str = br.readLine()) != null) {
//			if (str.startsWith(MODEL_YEAR)) {
//				System.out.println(str);
//				final String modelYear = str.trim().replace(MODEL_YEAR, "");
//				final String[] data = modelYear.split("\\|");
//				if (car != null && !(car.getModel().equalsIgnoreCase(data[0]) && car.getYear().equalsIgnoreCase(data[1]))) {
//					//save to map with arraylist
//					System.out.println(car.getModel() + " " + car.getYear());
//
//				}
//				//new car
//				//how about Make?
//				car = new Car();
//				car.setModel(data[0]);
//				car.setYear(data[1]);		
//			} else {
//				final String[] data = str.split("\\|");
//				//System.out.println(str);
//				final Part part = new Part();
//				part.setPartName(data[0]);
//				part.setPartNumber(data[1]);
//				part.setSource(data[2]);
//				part.setStockLevel(Integer.parseInt(data[3]));
//				//TODO: TEMP HANDLING
//				if (data[4].trim().equalsIgnoreCase("call")) {
//					part.setRetailPrice(0);
//				} else {
//					part.setRetailPrice(Float.parseFloat(data[4]));
//				}
//
//				part.setPhotoName(data[5]);
//				partList.add(part);
//			}			
//
//
//
//		}
//		if (!carparts.containsKey(car)) carparts.put(car, partList);
//		in.close();
//		System.out.println(carparts.size());
//		return carparts;
//	}
	
}



