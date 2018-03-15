package com.eduboss.common;

public enum CustomerStudentStatus {

	NORMAL("NORMAL", "正常"),
	DELETE("DELETE", "删除");
	
	private String name;
	private String value;
	
	private CustomerStudentStatus(String name, String value) {
		this.name = name;
		this.value = value;
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
