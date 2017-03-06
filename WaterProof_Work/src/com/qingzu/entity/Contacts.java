package com.qingzu.entity;

public class Contacts {
	private String name;
	private String phone;
	private String pinyi;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public Contacts(String name, String phone, String pinyi) {
		super();
		this.name = name;
		this.phone = phone;
		this.pinyi = pinyi;
	}

	public Contacts() {
		super();
		// TODO Auto-generated constructor stub
	}

}
