package com.eduboss.domainVo;

import java.math.BigDecimal;


public class TodayCourseBillVo {

	private String courseId;//课程id
	private String courseType;//课程类型 是一对一还是一对多
	private String courseName;//课程名字 年级+科目
	private String courseDate;//上课日期
	private String campusName;//校区名字
	private String studyManagerName;//学管名字
	private String teacherName;//老师名字
	private String studentName ;//学生名字
	private String studentNameList;
    private String gradeName;//年级
    private String subjectName;//科目 
	private String courseTime;//上课时间
	private BigDecimal planHours;//计划课时 
	private BigDecimal realHours;
    private String courseVersion;
    private String courseStatusName;
    private String picName;
    
    //前端查询条件
    private String campusId;
    
    //前端页面
    private String startDate;
    private String endDate;
    
  //前端页面
    private String startTime;
    private String endTime;
    private String courseAttenceType;//考勤类型
    private String subjectId;//科目 
    
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
 
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNameList() {
		return studentNameList;
	}
	public void setStudentNameList(String studentNameList) {
		this.studentNameList = studentNameList;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public BigDecimal getPlanHours() {
		return planHours;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}
	public String getCourseVersion() {
		return courseVersion;
	}
	public void setCourseVersion(String courseVersion) {
		this.courseVersion = courseVersion;
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
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
	public BigDecimal getRealHours() {
		return realHours;
	}
	public void setRealHours(BigDecimal realHours) {
		this.realHours = realHours;
	}
	public String getPicName() {
		return picName;
	}
	public void setPicName(String picName) {
		this.picName = picName;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCourseAttenceType() {
		return courseAttenceType;
	}
	public void setCourseAttenceType(String courseAttenceType) {
		this.courseAttenceType = courseAttenceType;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	} 
    
    
	
	
}
