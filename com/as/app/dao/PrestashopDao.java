package com.as.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrestashopDao {
	
	private static final String SELECT_QUANTITY_BY_REFERENCE = "select quantity from ps2_stock_available where id_product in (SELECT id_product FROM `ps2_product` WHERE reference = ?)";
	private static final String PRESTASHOP = "prestashop";
	private DBConfig dbConfig;
	
	public PrestashopDao() {
		this.dbConfig = new DBConfig(PRESTASHOP);
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
	
	public int getPartQuantity(String partNumber) {
		Connection con = null;
		try{
		con = getConnection();
		PreparedStatement stmt = con.prepareStatement(SELECT_QUANTITY_BY_REFERENCE);
		stmt.setString(1, partNumber);
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			int quantity = rs.getInt(1);			
			return quantity;
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
		
		return -1;
	}
	
	public int updateAvailableStock(String partNumber, int stockLevel) {
		Connection con = null;
		try{
		con = getConnection();
		PreparedStatement stmt = con.prepareStatement("Update ps2_stock_available set quantity = ? where id_product = (select id_product from ps2_product where reference = ?)");
		stmt.setInt(1, stockLevel);
		stmt.setString(2, partNumber);
		int executeUpdate = stmt.executeUpdate();
		
		return executeUpdate;
		
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

	public static void main(String args[]) {
		PrestashopDao dao = new PrestashopDao();
		dao.getPartQuantity("D2111");
	}
}
