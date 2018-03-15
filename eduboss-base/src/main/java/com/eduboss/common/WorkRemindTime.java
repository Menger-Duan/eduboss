package com.eduboss.common;

public enum WorkRemindTime {

	FIVE_MINUTE("FIVE_MINUTE","5分钟前"),
	TEN_MINUTE("TEN_MINUTE","10分钟前"),
	FIFTEEN_MINUTE("FIFTEEN_MINUTE","15分钟前"),
	THIRTY_MINUTE("THIRTY_MINUTE","30分钟前"),
	ONE_HOUR("ONE_HOUR","1小时前"),
	TWO_HOUR("TWO_HOUR","2小时钟前"),
	SIX_HOUR("SIX_HOUR","6小时前"),
	TWELVE_HOUR("TWELVE_HOUR","12小时前"),
	ENGHTEEN_HOUR("ENGHTEEN_HOUR","18小时前"),
	ONE_DAY("ONE_DAY","1天前"),
	TWO_DAY("TWO_DAY","2天前");
	
	private String value;
	private String name;
	
	private WorkRemindTime(String value, String name) {
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
