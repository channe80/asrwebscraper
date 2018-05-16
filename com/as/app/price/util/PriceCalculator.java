package com.as.app.price.util;


public class PriceCalculator {

	protected static final double GST = 0.15;
	protected static final double COMPANY_TAX_RATE = 0.28;
	
	protected float wholesalePrice;
	protected float retailPrice;
	protected float wholesaleAndGst;
	protected float appPrice;
	protected float shippingCost;
	protected float grossProfit;
	protected float netProfit;
	protected String comments;
	
	public PriceCalculator(float wholesalePrice, float retailPrice) {
		this.setWholesalePrice(wholesalePrice);
		this.retailPrice = retailPrice;
		
		this.wholesaleAndGst = calcWholesaleAndGst(this.wholesalePrice);

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

	public float getGrossProfit() {
		return grossProfit;
	}

	public void setGrossProfit(float grossProfit) {
		this.grossProfit = grossProfit;
	}

	public float getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(float netProfit) {
		this.netProfit = netProfit;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public static double getGst() {
		return GST;
	}

	public static double getCompanyTaxRate() {
		return COMPANY_TAX_RATE;
	}
	
	public static float calcWholesaleAndGst(float wholesalePrice) {
		return (float) (wholesalePrice + (wholesalePrice * GST));
		
	}
	
	public static int roundUpNearest5(double num) {
	    return (int) (Math.ceil(num / 5d) * 5);
	}
	
	public static int roundUpNearest10(double num) {
	    return (int) (Math.ceil(num / 10d) * 10);
	}
}
