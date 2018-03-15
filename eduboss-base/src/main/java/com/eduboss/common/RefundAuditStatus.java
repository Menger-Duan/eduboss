package com.eduboss.common;

public enum RefundAuditStatus {

	TO_SUBMIT("TO_SUBMIT", "待提交"),// 待提交
	AUDITING("AUDITING", "审批中"), // 审批中
	AUDIT_COMPLETED("AUDIT_COMPLETED", "审批完成"), // 审批完成
	AUDIT_REVOKE("AUDIT_REVOKE", "撤销审批"); // 撤销审批
	
	private String value;
	private String name;
	
	private RefundAuditStatus(String value, String name) {
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
