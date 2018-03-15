package com.eduboss.common;


public enum PromiseAuditStatus {

	UNAUDIT("UNAUDIT", "未审批"),//未审批
	RAUDIT("RAUDIT", "审批通过"),//审批通过
	AUDIT_FAILED("AUDIT_FAILED", "审批不通过"); // 审批不通过

	private String value;
	private String name;

	private PromiseAuditStatus(String value, String name) {
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
