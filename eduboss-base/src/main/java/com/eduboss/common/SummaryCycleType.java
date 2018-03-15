package com.eduboss.common;

public enum SummaryCycleType {
	
	WEEK("WEEK", "周报表"),// 周
	MONTH("MONTH", "月报表"),// 月
	QUARTER("QUARTER", "季报表"),// 季
	YEAR("YEAR", "年报表"); // 年
	
	private String value;
	private String name;
	
	private SummaryCycleType(String value, String name) {
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
