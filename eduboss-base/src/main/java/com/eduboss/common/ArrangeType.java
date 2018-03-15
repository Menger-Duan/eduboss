package com.eduboss.common;

public enum ArrangeType {
	
	DISTRIBUTION("DISTRIBUTION", "实收金额"),//实收金额
	EXTRACT("EXTRACT", "优惠金额");//优惠金额
	
	
	private String value;
	private String name;
	
	private ArrangeType(String value, String name) {
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
