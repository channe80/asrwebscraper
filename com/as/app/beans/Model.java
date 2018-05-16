package com.as.app.beans;

public class Model {
	private String name;
	private String subName;
	private int id;
	private String photoName;
	

	public Model (int id, String name, String subName, String photoName) {
		this.id = id;
		this.name  = name;
		this.subName = subName;
		this.photoName = photoName;
	}
	
	public Model (int id, String name, String subName) {
		this.id = id;
		this.name  = name;
		this.subName = subName;
	}
	
	public Model (String name, String subName) {
		this.name  = name;
		this.subName = subName;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubName() {
		return subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static Model splitModelName(String name) {
		String[] split = name.split(" ");
		String subname = "";
		if (split.length > 1) { 
			subname = name.substring(split[0].length() + 1, name.length());
		}
		System.out.println("name: " + split[0] + " subname: "  + subname);
		return new Model(split[0].trim(), subname.trim());
	}
	
}