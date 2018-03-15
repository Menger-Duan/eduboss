package com.eduboss.dto;

import com.eduboss.common.CourseRequirementCetegory;


public class CourseRequirementEditVo {
	
	
	private String id;
	private String studentId;
	private String studentName;
	private String teacherId;
	private String grade;
	private String subject;
	private String courseTime;
	private String courseDateDesciption;
	private Double courseHours;
	private String courseArrangerId;
	private String studyManagerId;
	private String requirementStatus;
	private String startDate;
	private String endDate;
	private String lastArrangeTime;
	private String remark;
    private String createUserName;
	private CourseRequirementCetegory requirementCetegory;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getCourseDateDesciption() {
		return courseDateDesciption;
	}
	public void setCourseDateDesciption(String courseDateDesciption) {
		this.courseDateDesciption = courseDateDesciption;
	}
	public Double getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}
	public String getCourseArrangerId() {
		return courseArrangerId;
	}
	public void setCourseArrangerId(String courseArrangerId) {
		this.courseArrangerId = courseArrangerId;
	}
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public String getRequirementStatus() {
		return requirementStatus;
	}
	public void setRequirementStatus(String requirementStatus) {
		this.requirementStatus = requirementStatus;
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
	public String getLastArrangeTime() {
		return lastArrangeTime;
	}
	public void setLastArrangeTime(String lastArrangeTime) {
		this.lastArrangeTime = lastArrangeTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CourseRequirementCetegory getRequirementCetegory() {
		return requirementCetegory;
	}
	public void setRequirementCetegory(CourseRequirementCetegory requirementCetegory) {
		this.requirementCetegory = requirementCetegory;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
