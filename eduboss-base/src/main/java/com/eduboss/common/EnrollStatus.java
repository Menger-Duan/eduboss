package com.eduboss.common;

public enum EnrollStatus {
	ING("ING", "招生中"),
	END("END", "已满员");

	private String value;
	private String name;

	private EnrollStatus(String value, String name) {
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
