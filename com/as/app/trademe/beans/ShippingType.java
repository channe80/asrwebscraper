package com.as.app.trademe.beans;

public enum ShippingType {
	None(0),
	Unknown(1),
	Undecided(2),
	Pickup(3),
	Free(4),
	Custom(5);
	
	private int type;
	ShippingType(int type){
		this.type = type;
	}
	
	public int type() {
		return this.type;
	}
}
