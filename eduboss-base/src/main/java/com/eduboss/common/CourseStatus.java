package com.eduboss.common;

public enum CourseStatus {
	
	NEW("NEW", "未上课"),//未上课
	STUDENT_ATTENDANCE("STUDENT_ATTENDANCE", "学生已考勤"),//学生已考勤
	TEACHER_ATTENDANCE("TEACHER_ATTENDANCE", "老师已考勤"),//老师已考勤
	STUDY_MANAGER_AUDITED("STUDY_MANAGER_AUDITED", "学管已核对"),//学管已核对
	//TEACHING_MANAGER_AUDITED("TEACHING_MANAGER_AUDITED", "教务已核对"),//教务已核对
	CHARGED("CHARGED", "已结算"),//已结算
	TEACHER_LEAVE("TEACHER_LEAVE", "老师请假"),//老师请假
	STUDENT_LEAVE("STUDENT_LEAVE", "学生请假"),//学生请假
	CANCEL("CANCEL", "已取消");//取消课程
	
	private String value;
	private String name;
	
	private CourseStatus(String value, String name) {
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
	
	public static void main(String[] args) {
		System.out.println(CourseStatus.NEW.getName());
		System.out.println(CourseStatus.STUDENT_ATTENDANCE.getName());
	}
}
