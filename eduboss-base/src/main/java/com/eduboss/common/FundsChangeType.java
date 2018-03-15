package com.eduboss.common;

public enum FundsChangeType {

	HUMAN("HUMAN", "人工录入"), // 人工录入
	SYSTEM("SYSTEM", "系统录入"); // 系统审核

	private String value;
	private String name;

	private FundsChangeType(String value, String name) {
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
