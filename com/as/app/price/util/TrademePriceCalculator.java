package com.as.app.price.util;

public class TrademePriceCalculator extends PriceCalculator{

	private float trademeFee;
	
	public TrademePriceCalculator(float wholesalePrice, float retailPrice) {
		super(wholesalePrice, retailPrice);
		this.appPrice = calcAppPrice(retailPrice, this.getWholesaleAndGst());
		this.shippingCost = calcShippingCost();
		this.trademeFee = calcTrademeFee();
		this.grossProfit = calcGrossProfit();
		this.netProfit = calcNetProfit();
		this.comments = assignComment();
		
	}


	public float getTrademeFee() {
		return trademeFee;
	}

	public void setTrademeFee(float trademeFee) {
		this.trademeFee = trademeFee;
	}
	
	public static float calcShippingCost() {
		//temp
		return 10;
	}
	
	private float calcAppPrice(float retailPrice, float wholesaleandgst) {
		 
		float calculatedAppPrice = 0;
		
		double discount = 0.20; // 0.10; //
		
		calculatedAppPrice = (float) Math.ceil((retailPrice - ((retailPrice-wholesaleandgst) * discount)));
		System.out.print("Discounted: " + calculatedAppPrice + "=>");
		
//		return calculatedAppPrice;
		
//		double less = 0.9;
//		
//		double lessRetail = Math.ceil(retailPrice * less);

		//round off to nearest 10-1 - if less than RRP the it's the appPrice
		float nearest5 = roundUpNearest5(calculatedAppPrice);
		float nearest10 = roundUpNearest10(calculatedAppPrice) - 1;
		float adjustedAppPrice = 0;
		if (nearest10 - calculatedAppPrice < nearest5 - calculatedAppPrice) {
			//use nearest10
			adjustedAppPrice = nearest10;
		} else {
			//use nearest5
			adjustedAppPrice = nearest5;
		}
		
		if (adjustedAppPrice < retailPrice ) {
			calculatedAppPrice = adjustedAppPrice;
		} else  {
			calculatedAppPrice = (float) calculatedAppPrice;
		}
		
		System.out.println("Adjusted: " + calculatedAppPrice);
		
		return calculatedAppPrice;
	}
	
	private float calcTrademeFee() {
		
		float fee = 0;
		
		//=IF(H8<200,H8*0.079,IF(AND(H8>=200,H8<=1500),15.8+(H8*0.049),IF(79.5+(H8*0.019) < 149, 79.5+(H8*0.019), 149)))
		if (this.appPrice < 200) {
			fee = (float) (this.appPrice * 0.079);
		} else if (this.appPrice > 200 && this.appPrice < 1500) {
			//$200 - $1500 --	$15.80 + 4.9% of sale price over $200
			fee = (float) (15.80 + (this.appPrice * 0.049));
		} else {
			//$79.50 + 1.9% of sale price over $1500 (max fee = $149)
			fee = (float) (79.5 + (this.appPrice * 0.019) > 149 ? 149 : 79.5 + (this.appPrice * 0.019));
		}
		
		return fee;
	
	}
	
	private float preCalculateNetProfit() {
		float tempGrossProfit = this.appPrice - (this.wholesaleAndGst + this.trademeFee);
		float tempNetProfit = (float) (tempGrossProfit - (tempGrossProfit * COMPANY_TAX_RATE));
		
		return tempNetProfit;
	}
	
	private float calcGrossProfit() {
		float tempNetProfit = preCalculateNetProfit();
		if (tempNetProfit < 10) {
			//change app price
			this.appPrice = this.retailPrice;
			
		} 
		
		return this.appPrice - (this.wholesaleAndGst + this.trademeFee);
		
		
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
