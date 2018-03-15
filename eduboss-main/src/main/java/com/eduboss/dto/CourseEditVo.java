package com.eduboss.dto;

import com.eduboss.common.CourseStatus;

import java.math.BigDecimal;



public class CourseEditVo {
	
	
	private String id;
	private String teacherId;
	private String courseTime;
	private BigDecimal courseMinutes;// 课程分钟数
	private String courseDate;
	private CourseStatus courseStatus; 
	private String grade;
	private String subject;
	private BigDecimal planHours;
	private BigDecimal realHours;
	private BigDecimal staduyManagerAuditHours;
	private BigDecimal teachingManagerAuditHours;
    private String productId;
    private String studentId;
    
    private String cancelCourse;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}

	public BigDecimal getPlanHours() {
		return planHours;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
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
	public BigDecimal getTeachingManagerAuditHours() {
		return teachingManagerAuditHours;
	}
	public void setTeachingManagerAuditHours(BigDecimal teachingManagerAuditHours) {
		this.teachingManagerAuditHours = teachingManagerAuditHours;
	}
	/**
	 * @return the courseDate
	 */
	public String getCourseDate() {
		return courseDate;
	}
	/**
	 * @param courseDate the courseDate to set
	 */
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	/**
	 * @return the courseStatus
	 */
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}
	/**
	 * @param courseStatus the courseStatus to set
	 */
	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}
	/**
	 * @return the gradeId
	 */
	public String getGrade() {
		return grade;
	}
	/**
	 * @param gradeId the gradeId to set
	 */
	public void setGrade(String grade) {
		this.grade = grade;
	}
	/**
	 * @return the subjectId
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCancelCourse() {
		return cancelCourse;
	}
	public void setCancelCourse(String cancelCourse) {
		this.cancelCourse = cancelCourse;
	}
    
    
}
