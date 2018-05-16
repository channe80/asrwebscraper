package com.as.app.webscraper;

public enum AutoPartSource {

	NASA("NASA");
	
	private String value;
	AutoPartSource(String value){
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
