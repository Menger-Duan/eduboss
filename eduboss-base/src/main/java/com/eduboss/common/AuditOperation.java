package com.eduboss.common;

public enum AuditOperation {

	AUDIT_SUBMIT("AUDIT_SUBMIT", "提交审批"),// 提交审批
	AUDIT_PASS("AUDIT_PASS", "审批通过"), // 审批通过
	AUDIT_ROLLBACK("AUDIT_ROLLBACK", "审批回退"), // 审批回退
	AUDIT_REVOCATION("AUDIT_REVOCATION", "审批撤销"), // 审批撤销
	FINANCE_RECEIVE("FINANCE_RECEIVE", "财务接收"), // 财务接收
	FINANCE_EXPENDITURE("FINANCE_EXPENDITURE", "财务出款"), // 财务出款
	CHANGE_AUDIT_USER("CHANGE_AUDIT_USER", "变更审批用户"); // 变更审批用户
	
	private String value;
	private String name;
	
	private AuditOperation(String value, String name) {
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
