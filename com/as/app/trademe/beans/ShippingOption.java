package com.as.app.trademe.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ShippingOption")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingOption {
	@XmlElement(name = "Type")
	private ShippingType type;
	@XmlElement(name = "Price")
	private float price;
	@XmlElement(name = "Method")
	private String method;
	@XmlElement(name = "ShippingId")
	private int shippingId;
	
	public ShippingOption() {
		
	}
	
	public ShippingOption(ShippingType type) {
		this.type = type;
	}
	
	public ShippingType getType() {
		return type;
	}
	public void setType(ShippingType type) {
		this.type = type;
	}
	public float setPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getShippingId() {
		return shippingId;
	}
	public void setShippingId(int shippingId) {
		this.shippingId = shippingId;
	}
}
