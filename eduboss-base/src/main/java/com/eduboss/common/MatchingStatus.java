package com.eduboss.common;

public enum MatchingStatus {
	
	NOT_YET_MATCH("NOT_YET_MATCH", "未匹配"),//未匹配
	SYSTEM_MATCH("SYSTEM_MATCH", "系统匹配"),//系统匹配
	ARTIFICIAL_MATCH("ARTIFICIAL_MATCH", "人工匹配");//人工匹配
	
	private String value;
	private String name;
	
	private MatchingStatus(String value, String name) {
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
