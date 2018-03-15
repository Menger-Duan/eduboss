package com.eduboss.common;

public enum LectureClassAttendanceStatus {
	
	NEW("NEW", "未上课"),//未考勤
	CONPELETE("CONPELETE", "上课");// 已上课;
	
	private String value;
	private String name;
	
	private LectureClassAttendanceStatus(String value, String name) {
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
