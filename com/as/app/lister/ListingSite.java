package com.as.app.lister;

public enum ListingSite {
	
	TRADEME("TradeMe");
	
	private String value;
	ListingSite(String value){
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
