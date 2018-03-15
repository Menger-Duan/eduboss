package com.eduboss.common;

public enum MobileType {
	
//	DEPOSIT("DEPOSIT", "订金"),//订金
	ANDROID("ANDROID", "ANDROID"),//ANDROID
	IOS("IOS", "IOS");
	
	private String value;
	private String name;
	
	private MobileType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
