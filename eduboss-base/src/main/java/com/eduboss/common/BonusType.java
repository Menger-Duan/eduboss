package com.eduboss.common;

/**
 * 业绩类型
 * @author yao
 *
 */
public enum BonusType {
	NORMAL("NORMAL", "收费"),//正常 
	FEEDBACK_REFUND("FEEDBACK_REFUND", "退费");
	
	private String value;
	private String name;
	
	private BonusType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
