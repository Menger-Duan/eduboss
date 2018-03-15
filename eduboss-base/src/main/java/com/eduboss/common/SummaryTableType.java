package com.eduboss.common;

public enum SummaryTableType {
	
	CAMPUS("CAMPUS", "校区组"),// 校区组
	STUDY_MANAGER("STUDY_MANAGER", "学管组"),// 学管组
	CONSULTOR("CONSULTOR", "咨询组"); // 咨询组
	
	private String value;
	private String name;
	
	private SummaryTableType(String value, String name) {
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
