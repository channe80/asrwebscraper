package com.as.app.trademe.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PhotoUploadRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PhotoUploadRequest {
	@XmlElement(name = "PhotoData")
	private String photoData;
	
	@XmlElement(name = "FileName")
	private String fileName;
	
	@XmlElement(name = "FileType")
	private String fileType;
	
	@XmlElement(name = "IsWaterMarked")
	private boolean isWaterMarked;
	
	@XmlElement(name = "IsUsernameAdded")
	private boolean isUsernameAdded;

	public PhotoUploadRequest() {}
	
	public String getPhotoData() {
		return photoData;
	}
	public void setPhotoData(String photoData) {
		this.photoData = photoData;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public boolean isWaterMarked() {
		return isWaterMarked;
	}
	public void setWaterMarked(boolean isWaterMarked) {
		this.isWaterMarked = isWaterMarked;
	}
	public boolean isUsernameAdded() {
		return isUsernameAdded;
	}
	public void setUsernameAdded(boolean isUsernameAdded) {
		this.isUsernameAdded = isUsernameAdded;
	}
}
