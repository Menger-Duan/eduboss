package com.eduboss.common;

public enum MiniClassCourseStatus {

	NEW("NEW", "未上课"),//未上课
	CHARGED("CHARGED", "已结算"),//已结算
	TEACHER_ATTENDANCE("TEACHER_ATTENDANCE", "老师已考勤"),//老师已考勤
	CANCEL("CANCEL", "已取消"); //课程已取消

	private String value;
	private String name;

	private MiniClassCourseStatus(String value, String name) {
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
