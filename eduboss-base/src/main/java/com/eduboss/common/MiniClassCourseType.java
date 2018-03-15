package com.eduboss.common;

public enum MiniClassCourseType {
	CONSTANT("CONSTANT", "固定课时"),
	EXTENDED("EXTENDED", "可延伸课时");
	
	private String value;
	private String name;
	
	private MiniClassCourseType(String value, String name) {
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
