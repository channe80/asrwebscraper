package com.as.app.beans;

public class Part {

	private int modelId;
	private String partName;
	private String partNumber;
	private String details = "";
	private String source;
	private int stockLevel; 
	private float retailPrice;
	private String photoName;
	private float wholesalePrice;
	private int wholesalerId;
	private String linkId;
	private boolean active = false;
	private PartGroup partGroup;

	public PartGroup getPartGroup() {
		return partGroup;
	}
	public void setPartGroup(PartGroup partGroup) {
		this.partGroup = partGroup;
	}
	public int getModelId() {
		return modelId;
	}
	public void setModelId(int modelId) {
		this.modelId = modelId;
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getStockLevel() {
		return stockLevel;
	}
	public void setStockLevel(int stockLevel) {
		this.stockLevel = stockLevel;
	}
	public float getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(float retailPrice) {
		this.retailPrice = retailPrice;
	}
	public String getPhotoName() {
		return photoName;
	}
	public void setPhotoName(String imageName) {
		this.photoName = imageName;
	}
	public float getWholesalePrice() {
		return wholesalePrice;
	}
	public void setWholesalePrice(float wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}
	public int getWholesalerId() {
		return wholesalerId;
	}
	public void setWholesalerId(int wholesalerId) {
		this.wholesalerId = wholesalerId;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
}
