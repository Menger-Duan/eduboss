package com.eduboss.common;

public enum MiniClassAttendanceStatus {
	
	NEW("NEW", "未上课"),//未考勤
	CONPELETE("CONPELETE", "准时上课"),// 已准时上课
	LEAVE("LEAVE", "请假"),// 请假
	ABSENT("ABSENT", "缺勤"),// 缺勤
	LATE("LATE", "迟到");// 迟到
	
	private String value;
	private String name;
	
	private MiniClassAttendanceStatus(String value, String name) {
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
