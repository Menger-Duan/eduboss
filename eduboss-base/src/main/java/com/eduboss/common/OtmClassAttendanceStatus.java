package com.eduboss.common;

public enum OtmClassAttendanceStatus {
	
	NEW("NEW", "未上课"),//未考勤
	CONPELETE("CONPELETE", "准时上课"),// 已准时上课
	LEAVE("LEAVE", "请假"),// 请假
	LATE("LATE", "迟到"),//
	ABSENT("ABSENT", "缺勤");// 缺勤

	private String value;
	private String name;
	
	private OtmClassAttendanceStatus(String value, String name) {
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
