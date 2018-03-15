package com.eduboss.common;

public enum LectureClassStatus {
	
	PENDDING_START("PENDDING_START", "未上课"),//未上课
	CONPELETE("CONPELETE", "已完成");//已完成
	
	private String value;
	private String name;
	
	private LectureClassStatus(String value, String name) {
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
