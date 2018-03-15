package com.eduboss.common;

public enum ValidStatus {

	VALID("VALID", "有效"),
	INVALID("INVALID", "无效");
	
	private String name;
	private String value;
	
	private ValidStatus(String value, String name) {
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
