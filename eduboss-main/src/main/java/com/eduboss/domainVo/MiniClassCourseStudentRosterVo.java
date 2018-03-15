package com.eduboss.domainVo;

public class MiniClassCourseStudentRosterVo {

	private String miniClassId;
	private String miniClassName;
	private String startDate;
	private String endDate;
	private String classTime;
	private Double everyCourseClassNum;
	private Integer classTimeLength;
	private String teacherName;
	private String[] studentNames;
	private String[] studentContacts;
	private String[] courseDates;

	private String classroomName;
	
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	public String getMiniClassName() {
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getClassTime() {
		return classTime;
	}
	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}
	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}
	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}
	public Integer getClassTimeLength() {
		return classTimeLength;
	}
	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String[] getStudentNames() {
		return studentNames;
	}
	public void setStudentNames(String[] studentNames) {
		this.studentNames = studentNames;
	}
	public String[] getCourseDates() {
		return courseDates;
	}
	public void setCourseDates(String[] courseDates) {
		this.courseDates = courseDates;
	}
	public String[] getStudentContacts() {
		return studentContacts;
	}
	public void setStudentContacts(String[] studentContacts) {
		this.studentContacts = studentContacts;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}
}
