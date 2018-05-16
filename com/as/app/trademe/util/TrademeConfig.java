package com.as.app.trademe.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TrademeConfig {
	
	public TrademeConfig(String domain) {
		
		Properties configFile = new Properties();
		String fileName;
		if (domain.equalsIgnoreCase("trademe")) {
			System.out.println("getting trademe config");
			fileName = "conf/trademe.properties";
		} else if (domain.equalsIgnoreCase("tmsandbox")) {
			fileName = "conf/tmsandbox.properties";
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
		
		this.domain = configFile.getProperty("trademe.domain");
		this.consumerKeyValue = configFile.getProperty("trademe.consumer.key.value");
		this.consumerSecretValue = configFile.getProperty("trademe.consumer.secret.value");
		this.oauthTokenValue = configFile.getProperty("trademe.oauth.token.value");
		this.oauthTokenSecret = configFile.getProperty("trademe.oauth.token.secret");
	}
	
	private String domain;
	private String consumerKeyValue;
	private String consumerSecretValue;
	private String oauthTokenValue;
	private String oauthTokenSecret;

	public String getDomain() {
		return domain;
	}
	public String getConsumerKeyValue() {
		return consumerKeyValue;
	}
	public String getConsumerSecretValue() {
		return consumerSecretValue;
	}
	public String getOauthTokenValue() {
		return oauthTokenValue;
	}
	public String getOauthTokenSecret() {
		return oauthTokenSecret;
	}	
	
}
