package com.eduboss.common;

public enum MonitorSubject {

	RECEPTIONIST_INPUT_RES("RECEPTIONIST_INPUT_RES", "前台录入资源"),// 前台录入资源
	CONSULTOR_PRINT_RECEIPT("CONSULTOR_PRINT_RECEIPT", "咨询师打印收据"),// 咨询师打印收据
	CONSULTOR_PRINT_CONTRACT("CONSULTOR_PRINT_CONTRACT", "咨询师打印合同"),// 咨询师打印合同
	STUDY_MANAGER_PRINT_RECEIPT("STUDY_MANAGER_PRINT_RECEIPT", "学管师打印收据"),// 学管师打印收据
	STUDY_MANAGER_PRINT_CONTRACT("STUDY_MANAGER_PRINT_CONTRACT", "学管师打印合同"),// 学管师打印合同
	CONSULTOR_COURSE_REQUIREMENT("CONSULTOR_COURSE_REQUIREMENT", "咨询师提交新生排课数量"),// 咨询师提交新生排课数量
	STUDY_MANAGER_WITH_CARD_AND_FINGERPRINT_AND_STUDENT("STUDY_MANAGER_WITH_CARD_AND_FINGERPRINT_AND_STUDENT", "学管绑卡，绑指纹，所管学生"),// 学管绑卡，绑指纹，所管学生
	STUDY_MANAGER_COURSE_REQUIREMENT("STUDY_MANAGER_COURSE_REQUIREMENT", "学管提交排课需求数量"),// 学管提交排课需求数量
	STUDY_MANAGER_ATTENDANCE("STUDY_MANAGER_ATTENDANCE", "学管确认老师考勤数量"),// 学管确认老师考勤数量
	ARRANGEMENT_COURSE_STUDENT("ARRANGEMENT_COURSE_STUDENT", "教务排课的学生数量"),// 教务排课的学生数量
	CHARGE_RECORDS("CHARGE_RECORDS", "教务确认学生上课扣费数量"),// 教务确认学生上课扣费数量
	TEACHER_ATTENDANCE("TEACHER_ATTENDANCE", "老师考勤数量（学生）"),// 老师考勤数量（学生）
	NEW_COURSE_STUDENT_QUANTITY("NEW_COURSE_STUDENT_QUANTITY", "要上课学生数量"),// 要上课学生数量
	ATTENDANCE_STUDENT_QUANTITY("ATTENDANCE_STUDENT_QUANTITY", "学生考勤数量"),// 学生考勤数量
	TEACHER_NOT_ATTENDANCE_COURSE_HOUR("TEACHER_NOT_ATTENDANCE_COURSE_HOUR", "老师未考勤课时"),// 老师未考勤课时
	COURSE_HOUR_CONSUME_OF_TEACHER("COURSE_HOUR_CONSUME_OF_TEACHER", "老师课程消耗"),// 老师课程消耗
	COURSE_HOUR_CONSUME_OF_STUDY_MANAGER("COURSE_HOUR_CONSUME_OF_STUDY_MANAGER", "学管课程消耗");// 学管课程消耗
	
	private String value;
	private String name;
	
	private MonitorSubject(String value, String name) {
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
