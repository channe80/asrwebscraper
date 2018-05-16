package com.as.app.trademe.beans;



import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Listings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Listings {

	@XmlElement(name = "TotalCount")
	private int totalCount;

	@XmlElement(name = "Page")
	private int page;

	@XmlElement(name = "PageSize")
	private int pageSize;

	@XmlElementWrapper(name = "List")
	@XmlElement(name = "Listing")
	private List<Item> list;

	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<Item> getList() {
		return list;
	}
	public void setList(List<Item> list) {
		this.list = list;
	}
}

