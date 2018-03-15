package com.eduboss.domainVo;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.domain.MiniClassCourse;

public class MiniClassStudentAttendentVo {

	private String id;
	private MiniClassCourse miniClassCourse;
	private String miniClassId;
	private String miniClassCourseId;
	private String studentId;
	private String studentName;
	private String courseDateTime;
	private String attendentUserId;
	private String chargeStatus;
	private MiniClassAttendanceStatus miniClassAttendanceStatus; 
	private String attendanceType;
	private BaseStatus hasTeacherAttendance;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String absentRemark; // 缺勤备注
	private String supplementDate; // 补课日期
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MiniClassCourse getMiniClassCourse() {
		return miniClassCourse;
	}
	public void setMiniClassCourse(MiniClassCourse miniClassCourse) {
		this.miniClassCourse = miniClassCourse;
	}
	public String getCourseDateTime() {
		return courseDateTime;
	}
	public void setCourseDateTime(String courseDateTime) {
		this.courseDateTime = courseDateTime;
	}
	public String getAttendentUserId() {
		return attendentUserId;
	}
	public void setAttendentUserId(String attendentUserId) {
		this.attendentUserId = attendentUserId;
	}
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}
	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}
	public MiniClassAttendanceStatus getMiniClassAttendanceStatus() {
		return miniClassAttendanceStatus;
	}
	public void setMiniClassAttendanceStatus(
			MiniClassAttendanceStatus miniClassAttendanceStatus) {
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
	}
	public String getAttendanceType() {
		return attendanceType;
	}
	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}
	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}
	public String getAbsentRemark() {
		return absentRemark;
	}
	public void setAbsentRemark(String absentRemark) {
		this.absentRemark = absentRemark;
	}
	public String getSupplementDate() {
		return supplementDate;
	}
	public void setSupplementDate(String supplementDate) {
		this.supplementDate = supplementDate;
	}
	
}
