package com.eduboss.common;

public enum PayType {
	
	REAL("REAL", "实收金额"),//实收金额
	PROMOTION("PROMOTION", "优惠金额");//优惠金额
	
	
	private String value;
	private String name;
	
	private PayType(String value, String name) {
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
