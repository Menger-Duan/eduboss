package com.eduboss.domainVo;

import com.eduboss.domain.Course;
import com.eduboss.utils.HibernateUtils;

public class MiniClassCourseScheduleDayVo {
	
	/** 小班课程ID*/
	private String miniClassCourseId;
	/** 小班*/
	private String miniClassId;	
	private String miniClassName;
	/** 上课日期*/
	private String courseDate;
	/** 上课时间*/
	private String courseStartTime;
	/** 课程结束时间*/
	private String courseEndTime;
	/** 老师*/
	private String teacherId;
	private String teacherName;
	/** 实到人数*/
	private int arrivals;
	/** 应到人数*/
	private int promisers;
	/** 课室名称*/
	private String classroom;
	/** 校区*/
	private String organizationName;
	/** 考勤状态*/
	private String attendance;
	
	private String classroomId;
	
	/** 未上课*/
	private int nonono;
	/** 请假*/
	private int leave;
	/** 缺勤*/
	private int absent;
	/** 结算*/
	private int paid;
	
	private int crashInd ;
	
	private int late; //迟到 
	
	
	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}
	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}
	public String getCourseStartTime() {
		return courseStartTime;
	}
	public void setCourseStartTime(String courseStartTime) {
		this.courseStartTime = courseStartTime;
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
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
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
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getAttendance() {
		return attendance;
	}
	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public int getArrivals() {
		return arrivals;
	}
	public void setArrivals(int arrivals) {
		this.arrivals = arrivals;
	}
	public int getPromisers() {
		return promisers;
	}
	public void setPromisers(int promisers) {
		this.promisers = promisers;
	}
	public int getNonono() {
		return nonono;
	}
	public void setNonono(int nonono) {
		this.nonono = nonono;
	}
	public int getLeave() {
		return leave;
	}
	public void setLeave(int leave) {
		this.leave = leave;
	}
	public int getAbsent() {
		return absent;
	}
	public void setAbsent(int absent) {
		this.absent = absent;
	}
	public int getPaid() {
		return paid;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	public int getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(int crashInd) {
		this.crashInd = crashInd;
	}
	public int getLate() {
		return late;
	}
	public void setLate(int late) {
		this.late = late;
	}
	
	
	
}
