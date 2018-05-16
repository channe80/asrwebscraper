package com.as.app.trademe.beans;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListingRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListingRequest {

	@XmlElement(name = "Category")
	private String category;

	@XmlElement(name = "Title")	
	private String title;

	@XmlElement(name = "Subtitle")	
	private String subtitle;
	
	@XmlElementWrapper(name = "Description")
	@XmlElement(name = "Paragraph")
	private List<String> description;

	@XmlElement(name = "StartPrice")	
	private float startPrice;

	@XmlElement(name = "ReservePrice")	
	private float reservePrice;

	@XmlElement(name = "BuyNowPrice")
	private float buyNowPrice;

	@XmlElement(name = "Duration")
	private ListingDuration duration;
	
	@XmlElement(name = "EndDate")
	private String endDate;

	@XmlElement(name = "EndDateTime")
	private Date endDateTime;

	@XmlElement(name = "Pickup")
	private Pickup pickUp;

	@XmlElement(name = "IsBrandNew")
	private boolean isBrandNew;

	@XmlElement(name = "AuthenticatedMemebersOnly")
	private boolean authenticatedMemebersOnly;

	@XmlElement(name = "IsClassified")
	private boolean isClassified;

	@XmlElement(name = "SendPaymentInstructions")
	private boolean sendPaymentInstructions;

	@XmlElement(name = "IsBold")
	private boolean isBold;

	@XmlElement(name = "IsFeatured")
	private boolean isFeatured;

	@XmlElement(name = "IsHomePageFeatured")
	private boolean isHomePageFeatured;

	@XmlElement(name = "HasGallery")
	private boolean hasGallery;

	@XmlElement(name = "IsFlatShippingCharge")
	private boolean isFlatShippingCharge;
	
	@XmlElementWrapper(name = "PhotoIds")
	@XmlElement(name = "PhotoId")
	private List<Integer> photoIds;
	
	@XmlElementWrapper(name = "ShippingOptions")
	@XmlElement(name = "ShippingOption")
	private List<ShippingOption> shippingOptions;
	
	@XmlElementWrapper(name = "PaymentMethods")
	@XmlElement(name = "PaymentMethod")
	private List<PaymentMethod> paymentMethods;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public List<String> getDescription() {
		return description;
	}

	public void setDescription(List<String> description) {
		this.description = description;
	}

	public float getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(float startPrice) {
		this.startPrice = startPrice;
	}

	public float getReservePrice() {
		return reservePrice;
	}

	public void setReservePrice(float reservePrice) {
		this.reservePrice = reservePrice;
	}

	public float getBuyNowPrice() {
		return buyNowPrice;
	}

	public void setBuyNowPrice(float buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public Pickup getPickUp() {
		return pickUp;
	}

	public void setPickUp(Pickup pickUp) {
		this.pickUp = pickUp;
	}

	public boolean isBrandNew() {
		return isBrandNew;
	}

	public void setBrandNew(boolean isBrandNew) {
		this.isBrandNew = isBrandNew;
	}

	public boolean isAuthenticatedMemebersOnly() {
		return authenticatedMemebersOnly;
	}

	public void setAuthenticatedMemebersOnly(boolean authenticatedMemebersOnly) {
		this.authenticatedMemebersOnly = authenticatedMemebersOnly;
	}

	public boolean isClassified() {
		return isClassified;
	}

	public void setClassified(boolean isClassified) {
		this.isClassified = isClassified;
	}

	public boolean isSendPaymentInstructions() {
		return sendPaymentInstructions;
	}

	public void setSendPaymentInstructions(boolean sendPaymentInstructions) {
		this.sendPaymentInstructions = sendPaymentInstructions;
	}

	public boolean isBold() {
		return isBold;
	}

	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}

	public boolean isFeatured() {
		return isFeatured;
	}

	public void setFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public boolean isHomePageFeatured() {
		return isHomePageFeatured;
	}

	public void setHomePageFeatured(boolean isHomePageFeatured) {
		this.isHomePageFeatured = isHomePageFeatured;
	}

	public boolean isHasGallery() {
		return hasGallery;
	}

	public void setHasGallery(boolean hasGallery) {
		this.hasGallery = hasGallery;
	}

	public boolean isFlatShippingCharge() {
		return isFlatShippingCharge;
	}

	public void setFlatShippingCharge(boolean isFlatShippingCharge) {
		this.isFlatShippingCharge = isFlatShippingCharge;
	}

	public List<Integer> getPhotoIds() {
		return photoIds;
	}

	public void setPhotoIds(List<Integer> photoIds) {
		this.photoIds = photoIds;
	}

	public List<ShippingOption> getShippingOptions() {
		return shippingOptions;
	}

	public void setShippingOptions(List<ShippingOption> shippingOptions) {
		this.shippingOptions = shippingOptions;
	}

	public List<PaymentMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public ListingDuration getDuration() {
		return duration;
	}

	public void setDuration(ListingDuration duration) {
		this.duration = duration;
	}
}