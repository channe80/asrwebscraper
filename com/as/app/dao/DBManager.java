package com.as.app.dao;

import com.as.app.ASAdmin;

public class DBManager {
	
	private static CarPartsDao carpartsDao;
	private static PrestashopDao prestaDao;
	
	public static CarPartsDao getCarPartsDaoInstance() {
		
		if (carpartsDao == null) {
			carpartsDao = new CarPartsDao(ASAdmin.TRADEME_DOMAIN);
		}
		
		return carpartsDao;
	
	}
	
	public static PrestashopDao getPrestashopDaoInstance() {
		
		if (prestaDao == null) {
			prestaDao = new PrestashopDao();
		}
		
		return prestaDao;
	
	}
	
}
