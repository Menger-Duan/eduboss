package com.eduboss.domainJdbc;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseStatus;

/**
 * 
 * @author lixuejun
 * 2016-08-22
 */
public class MiniClassCourseJdbc {

	// Fields

	private String miniClassCourseId;
	private String courseTime;
	private CourseStatus courseStatus;
	private String crashInd;
	private String miniClassId;
	private String miniClassName;
	private String courseDate;
	private Double courseHours;
	private String courseEndTime;
	private String blCampusId;
	private String blCampusName;
	private String teacherId;
	private String teacherName;
	private String studyManegerId;
	private String studyManegerName;
	private String subjectId;
	private String subjectName;
	private String gradeId;
	private String gradeName;
	private String classroomId;
	private String attendacePicName;
	private AuditStatus auditStatus;//财务审批状态
	private String teacherMobile;//老师手机
	private String studyManegerMobile;//班主任手机
	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}
	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(String crashInd) {
		this.crashInd = crashInd;
	}
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
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public Double getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
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
	public String getStudyManegerId() {
		return studyManegerId;
	}
	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}
	public String getStudyManegerName() {
		return studyManegerName;
	}
	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
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
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public String getAttendacePicName() {
		return attendacePicName;
	}
	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
	}
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getTeacherMobile() {
		return teacherMobile;
	}
	public void setTeacherMobile(String teacherMobile) {
		this.teacherMobile = teacherMobile;
	}
	public String getStudyManegerMobile() {
		return studyManegerMobile;
	}
	public void setStudyManegerMobile(String studyManegerMobile) {
		this.studyManegerMobile = studyManegerMobile;
	}
	
}