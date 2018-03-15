package com.eduboss.common;


/**
 * 产品类型 2014-7-31
 */
public enum CourseWeChatType {
	
	OTO("OTO", "一对一"),//一对一 
	SC("SC", "小班"), //小班
	OTM("OTM","一对多");//一对多

	
	private String value;
	private String name;
	
	private CourseWeChatType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
