package com.eduboss.domainVo;

import java.math.BigDecimal;

public class OneOnOneBatchAttendanceExcelVo {
	private String courseId;
	private String productName;
	private String courseDate;
	private String campusName;
	private String studyManagerName;
	private String teacherName;
	private String studentName;
	private String grade;
	private String subject;
	private int courseTimeLong;
	private String courseTime;
	private BigDecimal planHours;
	private BigDecimal realHours;
	private BigDecimal oneOnOneRemainingHour;
	private BigDecimal staduyManagerAuditHours;
	private String courseStatusName;
	
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public int getCourseTimeLong() {
		return courseTimeLong;
	}
	public void setCourseTimeLong(int courseTimeLong) {
		this.courseTimeLong = courseTimeLong;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}
	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	public BigDecimal getPlanHours() {
		return planHours;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getRealHours() {
		return realHours;
	}
	public void setRealHours(BigDecimal realHours) {
		this.realHours = realHours;
	}
	public BigDecimal getStaduyManagerAuditHours() {
		return staduyManagerAuditHours;
	}
	public void setStaduyManagerAuditHours(BigDecimal staduyManagerAuditHours) {
		this.staduyManagerAuditHours = staduyManagerAuditHours;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
}