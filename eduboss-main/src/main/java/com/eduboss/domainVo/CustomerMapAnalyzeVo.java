package com.eduboss.domainVo;

public class CustomerMapAnalyzeVo {
	
	/**
	 * ID
	 * */
	private int id;
	
	/**
	 * 学生名字
	 * */
	private String studentName;
	
	/**
	 * 地址
	 * */
	private String address;
	
	
	/**
	 * 经度
	 * */
	private String longitude;
	
	/**
	 * 纬度
	 * */
	private String latitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}


}
