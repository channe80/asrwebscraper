package com.as.app.trademe.beans;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	@XmlElement(name = "ListingId")
	private long listingId;
	@XmlElement(name = "Title")
	private String title;
	@XmlElement(name = "Category")
	private String category;
	@XmlElement(name = "StartPrice")
	private float startPrice;
	@XmlElement(name = "BuyNowPrice")
	private float buyNowPrice;
//	private Date startDate;
//	private Date endDate;
	@XmlElement(name = "ListingLength")
	private String listingLength;
	@XmlElement(name = "Restriction")
	private String restriction;
	@XmlElement(name = "IsFeatured")
	private boolean isFeatured;
	@XmlElement(name = "HasGallery")
	private boolean hasGallery;
	@XmlElement(name = "IsBold")
	private boolean isBold;
	@XmlElement(name = "IsHighlighted")
	private boolean isHighlighted;
	@XmlElement(name = "HasHomePageFeature")
	private boolean hasHomePageFeature;
	@XmlElement(name = "HasExtraPhotos")
	private boolean hasExtraPhotos;
	@XmlElement(name = "HasScheduledEndDate")
	private boolean hasScheduledEndDate;
	@XmlElement(name = "BidderAndWatchers")
	private int bidderAndWatchers;
	@XmlElement(name = "ViewCount")
	private int viewCount;
	@XmlElement(name = "BidCount")
	private int bidCount;
	@XmlElement(name = "MaxBidAmount")
	private float maxBidAmount;
	@XmlElement(name = "AsAt")
	private Date asAt;
	@XmlElement(name = "CategoryPath")
	private String categoryPath;
	@XmlElement(name = "PictureHref")
	private String pictureHref;
	@XmlElement(name = "PhotoId")
	private int photoId;
	@XmlElement(name = "StartDate")
	private Date startDate;	
	@XmlElement(name = "EndDate")
	private Date endDate;
	@XmlElement(name = "IsNew")
	private boolean newItem;	
	@XmlElement(name = "HasBuyNow")
	private boolean hasBuyNow;	
	
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getBidCount() {
		return bidCount;
	}
	public void setBidCount(int bidCount) {
		this.bidCount = bidCount;
	}
	public boolean isHasBuyNow() {
		return hasBuyNow;
	}
	public void setHasBuyNow(boolean hasBuyNow) {
		this.hasBuyNow = hasBuyNow;
	}
	public boolean isNewItem() {
		return newItem;
	}
	public void setNewItem(boolean newItem) {
		this.newItem = newItem;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public long getListingId() {
		return listingId;
	}
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public float getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(float startPrice) {
		this.startPrice = startPrice;
	}
	public float getBuyNowPrice() {
		return buyNowPrice;
	}
	public void setBuyNowPrice(float buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}
	public String getListingLength() {
		return listingLength;
	}
	public void setListingLength(String listingLength) {
		this.listingLength = listingLength;
	}
	public String getRestriction() {
		return restriction;
	}
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}
	public boolean isFeatured() {
		return isFeatured;
	}
	public void setFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}
	public boolean isHasGallery() {
		return hasGallery;
	}
	public void setHasGallery(boolean hasGallery) {
		this.hasGallery = hasGallery;
	}
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	public boolean isHighlighted() {
		return isHighlighted;
	}
	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	public boolean isHasHomePageFeature() {
		return hasHomePageFeature;
	}
	public void setHasHomePageFeature(boolean hasHomePageFeature) {
		this.hasHomePageFeature = hasHomePageFeature;
	}
	public boolean isHasExtraPhotos() {
		return hasExtraPhotos;
	}
	public void setHasExtraPhotos(boolean hasExtraPhotos) {
		this.hasExtraPhotos = hasExtraPhotos;
	}
	public boolean isHasScheduledEndDate() {
		return hasScheduledEndDate;
	}
	public void setHasScheduledEndDate(boolean hasScheduledEndDate) {
		this.hasScheduledEndDate = hasScheduledEndDate;
	}
	public int getBidderAndWatchers() {
		return bidderAndWatchers;
	}
	public void setBidderAndWatchers(int bidderAndWatchers) {
		this.bidderAndWatchers = bidderAndWatchers;
	}
	public float getMaxBidAmount() {
		return maxBidAmount;
	}
	public void setMaxBidAmount(float maxBidAmount) {
		this.maxBidAmount = maxBidAmount;
	}
	public Date getAsAt() {
		return asAt;
	}
	public void setAsAt(Date asAt) {
		this.asAt = asAt;
	}
	public String getCategoryPath() {
		return categoryPath;
	}
	public void setCategoryPath(String categoryPath) {
		this.categoryPath = categoryPath;
	}
	public String getPictureHref() {
		return pictureHref;
	}
	public void setPictureHref(String pictureHref) {
		this.pictureHref = pictureHref;
	}
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
}
