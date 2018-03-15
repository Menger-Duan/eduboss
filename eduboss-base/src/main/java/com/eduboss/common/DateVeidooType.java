package com.eduboss.common;

/**
 * 时间维度类型
 * @author ndd
 *
 */
public enum DateVeidooType {
	
	YEAR("YEAR", "年份"),//年份
	MONTH("MONTH", "月份"),//月份
	QUARTER("QUARTER", "季度"); //季度
	
	private String value;
	private String name;
	
	private DateVeidooType(String value, String name) {
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
