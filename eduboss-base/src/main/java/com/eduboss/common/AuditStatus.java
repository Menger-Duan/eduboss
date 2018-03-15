package com.eduboss.common;

public enum AuditStatus {
	
	VALIDATE("VALIDATE", "审批通过"), // 有效
	UNVALIDATE("UNVALIDATE", "审批不通过"), // 无效
	UNAUDIT("UNAUDIT", "未审批"); //未审批
	
	private String value;
	private String name;
	
	private AuditStatus(String value, String name) {
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
