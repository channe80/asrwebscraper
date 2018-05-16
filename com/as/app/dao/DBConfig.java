package com.as.app.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {
	
	private static final String PRESTASHOP = "prestashop";
	private static final String TMSANDBOX = "tmsandbox";
	private static final String TRADEME = "trademe";
	
	private String url;
	private String classStr;
	private String user;
	private String pwd;
	
		public DBConfig(String domain) {
			
			Properties configFile = new Properties();
			String fileName;
			if (domain.equalsIgnoreCase(TRADEME)) {
				System.out.println("getting stockinventory database config");
				fileName = "conf/database.properties";
			} else if (domain.equalsIgnoreCase(TMSANDBOX)) {
				System.out.println("getting sandbox stockinventory database config");
				fileName = "conf/sandboxDb.properties";
			} else if(domain.equalsIgnoreCase(PRESTASHOP)) {
				System.out.println("getting prestashop database config");
				fileName = "conf/prestashopDb.properties";
			} else {
				fileName = "";
			}
			
			InputStream is;

			try {
				is = new FileInputStream(fileName);
				configFile.load(is);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.url = configFile.getProperty("db.url");
			this.classStr = configFile.getProperty("db.class");
			this.user = configFile.getProperty("db.user");
			this.pwd = configFile.getProperty("db.pwd");
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getClassStr() {
			return classStr;
		}

		public void setClassStr(String classStr) {
			this.classStr = classStr;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getPwd() {
			return pwd;
		}

		public void setPwd(String pwd) {
			this.pwd = pwd;
		}
		

		
	
	
}
