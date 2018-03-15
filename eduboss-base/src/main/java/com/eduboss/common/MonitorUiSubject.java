package com.eduboss.common;

public enum MonitorUiSubject {

	TEACHER_NOT_ATTENDANCE_DETAIL("TEACHER_NOT_ATTENDANCE_DETAIL", "老师未考勤课时数（明细）"),// 老师未考勤课时数（明细）
	TEACHER_NOT_ATTENDANCE_SHAME_TOP("TEACHER_NOT_ATTENDANCE_SHAME_TOP", "老师未考勤荣辱榜"),// 老师未考勤荣辱榜
	STUDYMANAGER_UNBOUND_ATTNUM_STU_QUANTITY_DETAIL("STUDYMANAGER_UNBOUND_ATTNUM_STU_QUANTITY_DETAIL", "学管未绑定考勤编号的学生数量（明细）"),// 学管未绑定考勤编号的学生数量（明细）
	CAMPUS_MONITOR_DATA_SUMMARY("CAMPUS_MONITOR_DATA_SUMMARY", "校区数据监控（汇总）"),// 校区数据监控（汇总）
	COURSE_MONITOR_SUMMARY("COURSE_MONITOR_SUMMARY", "课程监控（汇总）");// 课程监控（汇总）
	
	private String value;
	private String name;
	
	private MonitorUiSubject(String value, String name) {
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
