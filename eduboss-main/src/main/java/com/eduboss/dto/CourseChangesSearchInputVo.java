package com.eduboss.dto;

import com.eduboss.common.CourseStatus;


public class CourseChangesSearchInputVo {
	
	
	private String startDate;
	private String endDate;
	private String studentName;
	private String teacherName;
	private String grade;
	private String subject;
	private CourseStatus courseStatus;
	
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}
}
