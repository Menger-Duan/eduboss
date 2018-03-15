package com.eduboss.common;

public enum StudentType {

	ENROLLED("ENROLLED","正式学生"), //已签合同的学生
	POTENTIAL("POTENTIAL","潜在学生");
	
	private String value;
	private String name;
	
	private StudentType(String value, String name) {
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
