package com.eduboss.common;

public enum FundsChangeAuditType {
	
	ARTIFICIAL("ARTIFICIAL", "非系统审核"), // 非系统审核
	SYSTEM("SYSTEM", "系统审核"); // 系统审核
	
	private String value;
	private String name;
	
	private FundsChangeAuditType(String value, String name) {
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
