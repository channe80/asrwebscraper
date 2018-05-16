package com.as.app.beans;


public class Car {

	private String make;
	private Model model;
	private String year;
	
	public Car() {
		
	}
	
	public Car(String make, Model model, String year) {
		this.make = make;
		this.model = model;
		this.year = year;
	}
	

	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

}
