package com.eduboss.domainVo;

public class TwoClassCourseStudentRosterVo {

	private String twoClassId;
	private String twoClassName;
	private String startDate;
	private String endDate;
	private String classTime;
	private Double everyCourseClassNum;
	private Integer classTimeLength;
	private String teacherName;//主班老师名字
	private String teacherTwoName;//辅班老师 辅导老师名字
	private String[] studentNames;
	private String[] studentContacts;
	private String[] courseDates;

	private String classroomName;

	public String getTwoClassId() {
		return twoClassId;
	}
	public void setTwoClassId(String twoClassId) {
		this.twoClassId = twoClassId;
	}
	public String getTwoClassName() {
		return twoClassName;
	}
	public void setTwoClassName(String twoClassName) {
		this.twoClassName = twoClassName;
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
	public String getTeacherTwoName() {
		return teacherTwoName;
	}
	public void setTeacherTwoName(String teacherTwoName) {
		this.teacherTwoName = teacherTwoName;
	}
	public String[] getStudentNames() {
		return studentNames;
	}
	public void setStudentNames(String[] studentNames) {
		this.studentNames = studentNames;
	}
	public String[] getStudentContacts() {
		return studentContacts;
	}
	public void setStudentContacts(String[] studentContacts) {
		this.studentContacts = studentContacts;
	}
	public String[] getCourseDates() {
		return courseDates;
	}
	public void setCourseDates(String[] courseDates) {
		this.courseDates = courseDates;
	}


	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}
}
