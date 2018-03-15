package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.LectureClassAttendanceStatus;
import com.eduboss.common.LectureClassStudentChargeStatus;



public class LectureClassStudentVo {
	
	
	private String id;
	private String studentId;
	private String studentName;
	private LectureClassStudentChargeStatus chargeStatus;
	private LectureClassAttendanceStatus auditStatus;
	private BaseStatus hasTeacherAttendance;
	

	private String chargeStatusName;
	private String auditStatusName;
	private String hasTeacherAttendanceName;
	private BigDecimal auditHours;
	private String chargeUserId;
	private String chargeUserName;
	private String auditTime;
	private String chargeTime;
	private String lectureId;
	private String lectureName;
	private String teacherId;
	private String teacherName;
	private String createTime;
	private String createUser;
	private String modifyTime;
	private String modifyUser;

	private String contractProductId;
	private String sblBranchId;
	private String blSchoolId;

	private String blSchoolName;
	
	
	
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public LectureClassStudentChargeStatus getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(LectureClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public LectureClassAttendanceStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(LectureClassAttendanceStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}
	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}
	public BigDecimal getAuditHours() {
		return auditHours;
	}
	public void setAuditHours(BigDecimal auditHours) {
		this.auditHours = auditHours;
	}
	public String getChargeUserId() {
		return chargeUserId;
	}
	public void setChargeUserId(String chargeUserId) {
		this.chargeUserId = chargeUserId;
	}
	public String getChargeUserName() {
		return chargeUserName;
	}
	public void setChargeUserName(String chargeUserName) {
		this.chargeUserName = chargeUserName;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getChargeTime() {
		return chargeTime;
	}
	public void setChargeTime(String chargeTime) {
		this.chargeTime = chargeTime;
	}
	public String getLectureId() {
		return lectureId;
	}
	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}
	public String getLectureName() {
		return lectureName;
	}
	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public String getChargeStatusName() {
		return chargeStatusName;
	}
	public void setChargeStatusName(String chargeStatusName) {
		this.chargeStatusName = chargeStatusName;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public String getHasTeacherAttendanceName() {
		return hasTeacherAttendanceName;
	}
	public void setHasTeacherAttendanceName(String hasTeacherAttendanceName) {
		this.hasTeacherAttendanceName = hasTeacherAttendanceName;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public String getSblBranchId() {
		return sblBranchId;
	}
	public void setSblBranchId(String sblBranchId) {
		this.sblBranchId = sblBranchId;
	}
	public String getBlSchoolId() {
		return blSchoolId;
	}
	public void setBlSchoolId(String blSchoolId) {
		this.blSchoolId = blSchoolId;
	}
	public String getBlSchoolName() {
		return blSchoolName;
	}
	public void setBlSchoolName(String blSchoolName) {
		this.blSchoolName = blSchoolName;
	}
	
}
