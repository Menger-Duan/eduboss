package com.eduboss.domainVo;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.OtmClassAttendanceStatus;
import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.domain.OtmClassCourse;

public class OtmClassStudentAttendentVo {
	
	private String id;
	private OtmClassCourse otmClassCourse;
	private String otmClassId;
	private String otmClassCourseId;
	private String studentId;
	private String studentName;
	private String courseDateTime;
	private String attendentUserId;
	private OtmClassAttendanceStatus otmClassAttendanceStatus;
	private OtmClassStudentChargeStatus chargeStatus;
	private String attendanceType;
	private BaseStatus hasTeacherAttendance;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String absentRemark; // 缺勤备注 暂时没用
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OtmClassCourse getOtmClassCourse() {
		return otmClassCourse;
	}
	public void setOtmClassCourse(OtmClassCourse otmClassCourse) {
		this.otmClassCourse = otmClassCourse;
	}
	public String getOtmClassId() {
		return otmClassId;
	}
	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
	}
	public String getOtmClassCourseId() {
		return otmClassCourseId;
	}
	public void setOtmClassCourseId(String otmClassCourseId) {
		this.otmClassCourseId = otmClassCourseId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
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
	public OtmClassAttendanceStatus getOtmClassAttendanceStatus() {
		return otmClassAttendanceStatus;
	}
	public void setOtmClassAttendanceStatus(
			OtmClassAttendanceStatus otmClassAttendanceStatus) {
		this.otmClassAttendanceStatus = otmClassAttendanceStatus;
	}
	
	public OtmClassStudentChargeStatus getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(OtmClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public String getAttendanceType() {
		return attendanceType;
	}
	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}
	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
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
	public String getAbsentRemark() {
		return absentRemark;
	}
	public void setAbsentRemark(String absentRemark) {
		this.absentRemark = absentRemark;
	}
	
}
