package com.as.app.trademe.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListingResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListingResponse {

	@XmlElement(name = "Success")
	private boolean success;
	
	@XmlElement(name = "Description")
	private String description;
	
	@XmlElement(name = "ListingId")
	private long listingId;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getListingId() {
		return listingId;
	}

	public void setListingId(long listingId) {
		this.listingId = listingId;
	}
}
