package com.as.app.price.util;


/**
 * temporary class for convenience
 * @author atorres
 *
 */
public class ActivePart {
	
	private String make;
	private String model;
	private String submodel;
	private String year;
	private String partName;
	private String partNumber;
	private float wholesalePrice;
	private float retailPrice;
	private float wholesaleAndGst;
	private float appPrice;
	private float shippingCost;
	private float trademeFee;
	private float grossProfit;
	

	private float netProfit;
	private int stock;
	private String comments;
	
	public ActivePart() {
		
	}
	
	public ActivePart(String make, String model, String submodel, String year, String partName, String partNumber, float wholesalePrice, float retailPrice, int stock) {
		this.make = make;
		this.model = model;
		this.submodel = submodel;
		this.year = year;
		this.partName = partName;
		this.partNumber = partNumber;
		this.wholesalePrice = wholesalePrice;
		this.retailPrice = retailPrice;
		this.stock = stock;
		
		TrademePriceCalculator calc = new TrademePriceCalculator(wholesalePrice, retailPrice);
		
		this.wholesaleAndGst = calc.getWholesaleAndGst();
		this.appPrice = calc.getAppPrice();
		this.shippingCost = calc.getShippingCost();
		this.trademeFee = calc.getTrademeFee();
		this.grossProfit = calc.getGrossProfit();
		this.netProfit = calc.getNetProfit();
		this.comments = calc.getComments(); 
		
	}
	
	
	public float getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(float grossProfit) {
		this.grossProfit = grossProfit;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSubmodel() {
		return submodel;
	}
	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public float getWholesalePrice() {
		return wholesalePrice;
	}
	public void setWholesalePrice(float wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	public float getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(float retailPrice) {
		this.retailPrice = retailPrice;
	}
	public float getWholesaleAndGst() {
		return wholesaleAndGst;
	}
	public void setWholesaleAndGst(float wholesaleAndGst) {
		this.wholesaleAndGst = wholesaleAndGst;
	}
	public float getAppPrice() {
		return appPrice;
	}
	public void setAppPrice(float appPrice) {
		this.appPrice = appPrice;
	}
	public float getShippingCost() {
		return shippingCost;
	}
	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
	public float getTrademeFee() {
		return trademeFee;
	}
	public void setTrademeFee(float trademeFee) {
		this.trademeFee = trademeFee;
	}
	public float getNetProfit() {
		return netProfit;
	}
	public void setNetProfit(float netProfit) {
		this.netProfit = netProfit;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	

	
}