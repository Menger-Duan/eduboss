package com.eduboss.common;
/**
 * 
 * @author xiaojinwang
 *  转介绍客户审核状态
 */
public enum CustomerAuditStatus {
    
	TOBE_AUDIT("TOBE_AUDIT","待审核"),
	NOT_PASS("NOT_PASS","不通过"),
	PASS("PASS","通过");
	private String value;
	private String name;
	private CustomerAuditStatus(String value, String name) {
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
