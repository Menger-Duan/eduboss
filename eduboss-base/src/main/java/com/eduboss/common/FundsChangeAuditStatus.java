package com.eduboss.common;

public enum FundsChangeAuditStatus {
	
	VALIDATE("VALIDATE", "审批通过"), // 审批通过
	UNVALIDATE("UNVALIDATE", "审批不过"), // 审批不过
	UNAUDIT("UNAUDIT", "未审批"); //未审批
	
	private String value;
	private String name;
	
	private FundsChangeAuditStatus(String value, String name) {
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
