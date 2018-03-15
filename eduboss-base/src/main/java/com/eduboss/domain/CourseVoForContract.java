package com.eduboss.domain;



/**
* @classname	Course.java 
* @Description	Course 是记录课程信息的一个domain object，里面就包括了 一对一课程，小班课程，和其他收费（到时再商量是不是要放在这里！）
* @author	ZhangYiheng
* @Date	2014-7-5 01:09:47
* @LastUpdate	ZhangYiheng
* @Version	1.0
*/
public class CourseVoForContract {
	
	private int id;
	private String name;
	private float hour;
	private float sub_total;
	private float discount;
	private String shortname;
	private String type;
	private int price;
	
	public CourseVoForContract() {
		
	}
	
	public CourseVoForContract(int id, String name, float hour, float sub_total, float discount, String shortname, String type, int price ) {
		this.id = id;
		this.name = name;
		this.hour = hour;
		this.sub_total = sub_total;
		this.discount = discount;
		this.shortname = shortname;
		this.type =  type;
		this.price = price;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getHour() {
		return hour;
	}
	public void setHour(float hour) {
		this.hour = hour;
	}
	public float getSub_total() {
		return sub_total;
	}
	public void setSub_total(float sub_total) {
		this.sub_total = sub_total;
	}
	public float getDiscount() {
		return discount;
	}
	public void setDiscount(float discount) {
		this.discount = discount;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
}
