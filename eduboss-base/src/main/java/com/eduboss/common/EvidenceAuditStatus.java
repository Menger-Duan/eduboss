package com.eduboss.common;

/**
 * 凭证审批状态
 * @author lixuejun
 *
 */
public enum EvidenceAuditStatus {
	
	NOT_AUDIT("NOT_AUDIT", "未审"), // 未审
	CAMPUS_AUDITED("CAMPUS_AUDITED", "校区已审"), // 校区已审
	FINANCE_FIRST_AUDITED("FINANCE_FIRST_AUDITED", "财务初审"), // 财务初审
	BRENCH_AUDITED("BRENCH_AUDITED", "分公司已审"), // 分公司已审
	FINANCE_END_AUDITED("FINANCE_END_AUDITED", "财务终审"); // 财务终审
	
	private String value;
	private String name;
	
	private EvidenceAuditStatus(String value, String name) {
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
