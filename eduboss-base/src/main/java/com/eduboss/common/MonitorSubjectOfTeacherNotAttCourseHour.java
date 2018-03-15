package com.eduboss.common;

public enum MonitorSubjectOfTeacherNotAttCourseHour {

	TEACHER_NOT_ATTENDANCE_ONE_ON_ONE_COURSE_HOUR("TEACHER_NOT_ATTENDANCE_ONE_ON_ONE_COURSE_HOUR", "一对一老师未考勤课时"),// 一对一老师未考勤课时
	TEACHER_NOT_ATTENDANCE_MINI_CLASS_COURSE_HOUR("TEACHER_NOT_ATTENDANCE_MINI_CLASS_COURSE_HOUR", "小班老师未考勤课时");// 小班老师未考勤课时
	
	private String value;
	private String name;
	
	private MonitorSubjectOfTeacherNotAttCourseHour(String value, String name) {
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
