package com.as.app.price.util;

public class APPWebsitePriceCalculator extends PriceCalculator{
	
	public APPWebsitePriceCalculator(float wholesalePrice, float retailPrice) {
		super(wholesalePrice, retailPrice);
		this.appPrice = calcAppPrice(retailPrice, this.getWholesaleAndGst());
		this.shippingCost = calcShippingCost();
		this.grossProfit = calcGrossProfit();
		this.netProfit = calcNetProfit();
		this.comments = assignComment();
		
	}


	public static float calcShippingCost() {
		//temp
		return 10;
	}
	
	private float calcAppPrice(float retailPrice, float wholesaleandgst) {
		 
		float calculatedAppPrice = 0;
		
		double discount = 0.20; //0.10;
		
		calculatedAppPrice = (float) Math.ceil((retailPrice - ((retailPrice-wholesaleandgst) * discount)));
		
		return calculatedAppPrice;
		
//		double less = 0.9;
//		
//		double lessRetail = Math.ceil(retailPrice * less);

		//round off to nearest 10-1 - if less than RRP the it's the appPrice
//		if ((roundUpNearest10(lessRetail) - 1) < retailPrice ) {
//			calculatedAppPrice = roundUpNearest10(lessRetail) - 1;
//		} else if (roundUpNearest5(lessRetail) < retailPrice) {
//			calculatedAppPrice = roundUpNearest5(lessRetail);
//		} else  {
//			calculatedAppPrice = (float) lessRetail;
//		}
		
//		return calculatedAppPrice;
	}
	
	private float preCalculateNetProfit() {
		float tempGrossProfit = this.appPrice - this.wholesaleAndGst;
		float tempNetProfit = (float) (tempGrossProfit - (tempGrossProfit * COMPANY_TAX_RATE));
		
		return tempNetProfit;
	}
	
	private float calcGrossProfit() {
		float tempNetProfit = preCalculateNetProfit();
		if (tempNetProfit < 10) {
			//change app price
			this.appPrice = this.retailPrice;
			
		} 
		
		return this.appPrice - this.wholesaleAndGst;
		
		
	}
	
	private float calcNetProfit() {
		return (float) (this.grossProfit - (this.grossProfit * COMPANY_TAX_RATE));
	}
	
	
	private String  assignComment() {
		if (this.netProfit < 10) {
			return "Net Profit is less than 10!";
		} else {
			return "Net Profit OK";
		}
	}

}
