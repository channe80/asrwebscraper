package com.as.app.trademe.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum
public enum Pickup {
	
	None, 
	Allow, 
	Demand, 
	Forbid;
	
//	None(0), 
//	Allow(1), 
//	Demand(2), 
//	Forbid(3);
//
//	private int pickup;
//	Pickup(int pickup) {
//		this.pickup = pickup;
//	}
//
//	public int pickup() {
//		return this.pickup;
//	}
}
