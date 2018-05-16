package com.as.app.trademe.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * <PhotoResponse xmlns="http://api.trademe.co.nz/v1">  
 * 	<Status>Failure</Status>  
 * 	<PhotoId>0</PhotoId>  
 * 	<Description>ABC</Description>
 * </PhotoResponse>
 * 
 * @author atorres
 *
 */

@XmlRootElement(name = "PhotoResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoResponse {
	
	@XmlElement(name = "Status")
	private String status;
	
	@XmlElement(name = "PhotoId")
	private int photoId;
	
	@XmlElement(name = "Description")
	private String description;

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
