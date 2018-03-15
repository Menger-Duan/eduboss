package com.eduboss.common;

public enum BaseStatus {

	TRUE("TRUE", "是"),// 是
	FALSE("FALSE", "否");// 否
	
	private String value;
	private String name;
	
	private BaseStatus(String value, String name) {
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
