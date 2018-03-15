package com.eduboss.domainVo;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.MiniClassAttendanceStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;

import java.math.BigDecimal;

public class TwoTeacherClassStudentAttendentVo {

	private int id;
	private String twoTeacherClassCourseId;
	private String twoTeacherClassCourseName;
	private String studentId;
	private String studentName;
	private String studentContact;
	private String courseDateTime;
	private String attendentUserId;
	private String attendentUserName;
	private MiniClassStudentChargeStatus chargeStatus;
	private MiniClassAttendanceStatus miniClassAttendanceStatus;
	private BaseStatus hasTeacherAttendance;
	private String createTime;
	private String createUserId;
	private String createUserName;
	private String modifyTime;
	private String modifyUserId;
	private String modifyUserName;
	private String absentRemark; // 缺勤备注
	private String supplementDate; // 补课日期

	private ContractProductStatus contractProductStatus;

	private BigDecimal contractProductRemainingAmount;

	private String attendanceType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTwoTeacherClassCourseId() {
		return twoTeacherClassCourseId;
	}

	public void setTwoTeacherClassCourseId(String twoTeacherClassCourseId) {
		this.twoTeacherClassCourseId = twoTeacherClassCourseId;
	}

	public String getTwoTeacherClassCourseName() {
		return twoTeacherClassCourseName;
	}

	public void setTwoTeacherClassCourseName(String twoTeacherClassCourseName) {
		this.twoTeacherClassCourseName = twoTeacherClassCourseName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public String getAttendentUserName() {
		return attendentUserName;
	}

	public void setAttendentUserName(String attendentUserName) {
		this.attendentUserName = attendentUserName;
	}

	public MiniClassStudentChargeStatus getChargeStatus() {
		return chargeStatus;
	}

	public void setChargeStatus(MiniClassStudentChargeStatus chargeStatus) {
		this.chargeStatus = chargeStatus;
	}

	public MiniClassAttendanceStatus getMiniClassAttendanceStatus() {
		return miniClassAttendanceStatus;
	}

	public void setMiniClassAttendanceStatus(MiniClassAttendanceStatus miniClassAttendanceStatus) {
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
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

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public ContractProductStatus getContractProductStatus() {
		return contractProductStatus;
	}

	public void setContractProductStatus(ContractProductStatus contractProductStatus) {
		this.contractProductStatus = contractProductStatus;
	}

	public BigDecimal getContractProductRemainingAmount() {
		return contractProductRemainingAmount;
	}

	public void setContractProductRemainingAmount(BigDecimal contractProductRemainingAmount) {
		this.contractProductRemainingAmount = contractProductRemainingAmount;
	}

	public String getAttendanceType() {
		return attendanceType;
	}

	public void setAttendanceType(String attendanceType) {
		this.attendanceType = attendanceType;
	}

	public String getStudentContact() {
		return studentContact;
	}

	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
}
