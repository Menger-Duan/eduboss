package com.eduboss.common;

public enum QuarterType {

	AUTUMN("AUTUMN", "秋季"),//秋季
	WINTER("WINTER", "寒假"), //冬季
	SPRINGTIME("SPRINGTIME", "春季"),//春季
	SUMMER("SUMMER", "暑假");//夏季
	
	private String value;
	private String name;
	
	private QuarterType(String value, String name) {
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
