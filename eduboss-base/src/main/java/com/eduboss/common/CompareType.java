package com.eduboss.common;
/**
 * 对比类型
 * @author arvin
 *
 */
public enum CompareType {
    GREATER_THAN("GREATER_THAN", "大于"),
    GREATER_THAN_EQUAL("GREATER_THAN_EQUAL","大于等于"),
	LESS_THAN("LESS_THAN","小于"),
	LESS_THAN_EQUAL("LESS_THAN_EQUAL","小于");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private CompareType(String value, String name) {
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
