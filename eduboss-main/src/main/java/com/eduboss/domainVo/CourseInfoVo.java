package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 
 * @author xiaojinwang
 * @description 教育平台接口 课程信息vo
 */
public class CourseInfoVo {

	private String courseId;
	private String type;
	private String state;
	private String teacherId;
	private String teacherName;
	private String classId;
	private String classIdTwo;
	private String teacherIdTwo;
	private String teacherNameTwo;
	private String subjectId;
	private String subjectName;
	private String gradeId;
	private String gradeName;
	private String audiencesId;
	private String audiencesName;
	private String organizationId;
	private String organizationName;
	private String regionId;
	private String regionName;
	private String remainingTime;//剩余课时 一对一是剩余资金
	private String courseDate;
	private String courseTime;
	private String courseEndTime;
	private String courseMinutes;//decimal(10,2)
	private String period;
	private String branchId;
	private String branchName;
	private String courseName;//(课程名称)
	private String classNameTwo;//副班名称
	private Integer remainCourseCount;//剩余讲次
	private String classroomId;
	private String classroomName;
	private Integer courseNo;
	//private String courseTeacherId;
	//private String courseTeacherName;
	
	public Integer getRemainCourseCount() {
		return remainCourseCount;
	}

	public void setRemainCourseCount(Integer remainCourseCount) {
		this.remainCourseCount = remainCourseCount;
	}

	public CourseInfoVo(){
		
	}
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getAudiencesId() {
		return audiencesId;
	}
	public void setAudiencesId(String audiencesId) {
		this.audiencesId = audiencesId;
	}
	public String getAudiencesName() {
		return audiencesName;
	}
	public void setAudiencesName(String audiencesName) {
		this.audiencesName = audiencesName;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRemainingTime() {
		return remainingTime;
	}
	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getCourseMinutes() {
		return courseMinutes;
	}
	public void setCourseMinutes(String courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getClassIdTwo() {
		return classIdTwo;
	}

	public void setClassIdTwo(String classIdTwo) {
		this.classIdTwo = classIdTwo;
	}

	public String getTeacherIdTwo() {
		return teacherIdTwo;
	}

	public void setTeacherIdTwo(String teacherIdTwo) {
		this.teacherIdTwo = teacherIdTwo;
	}

	public String getTeacherNameTwo() {
		return teacherNameTwo;
	}

	public void setTeacherNameTwo(String teacherNameTwo) {
		this.teacherNameTwo = teacherNameTwo;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getClassNameTwo() {
		return classNameTwo;
	}

	public void setClassNameTwo(String classNameTwo) {
		this.classNameTwo = classNameTwo;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public Integer getCourseNo() {
		return courseNo;
	}

	public void setCourseNo(Integer courseNo) {
		this.courseNo = courseNo;
	}

/*	public String getCourseTeacherId() {
		return courseTeacherId;
	}

	public void setCourseTeacherId(String courseTeacherId) {
		this.courseTeacherId = courseTeacherId;
	}

	public String getCourseTeacherName() {
		return courseTeacherName;
	}

	public void setCourseTeacherName(String courseTeacherName) {
		this.courseTeacherName = courseTeacherName;
	}  */
    
	
	
}
