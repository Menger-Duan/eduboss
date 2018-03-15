package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.OtmClassStatus;
import com.eduboss.common.StudentStatus;

public class OtmClassStudentVo {

	private String otmClassId;
	private String otmClassName;
	private String studentId;
	private String studentName;
	private String signUpDate; // 报班日期
	private OtmClassStatus otmClassStudyStatus;
	private String otmClassStudentChargeStatus;
	private Integer otmType; // 一对多类型
	private String firstSchoolTime; //首次上课时间
	private BigDecimal otmClassSurplusFinance;// 一对多剩余资金
	private BigDecimal otmClassSurplusCourseHour;// 一对多剩余课时
	private BigDecimal otmClassTotalCharged;// 累计扣费
	private String studentContact;//学生电话
	private String courseDate;
	private String otmClassAttendanceStatus; 
	private BaseStatus hasTeacherAttendance;
	private BaseStatus canCharge;
	private String studyManagerName;
	private String absentRemark; // 缺勤备注 暂时没用
	
	private StudentStatus stuStatus; //学生状态 
	
	public String getOtmClassId() {
		return otmClassId;
	}
	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
	}
	public String getOtmClassName() {
		return otmClassName;
	}
	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
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
	public String getSignUpDate() {
		return signUpDate;
	}
	public void setSignUpDate(String signUpDate) {
		this.signUpDate = signUpDate;
	}
	public OtmClassStatus getOtmClassStudyStatus() {
		return otmClassStudyStatus;
	}
	public void setOtmClassStudyStatus(OtmClassStatus otmClassStudyStatus) {
		this.otmClassStudyStatus = otmClassStudyStatus;
	}
	public String getOtmClassStudentChargeStatus() {
		return otmClassStudentChargeStatus;
	}
	public void setOtmClassStudentChargeStatus(
			String otmClassStudentChargeStatus) {
		this.otmClassStudentChargeStatus = otmClassStudentChargeStatus;
	}
	public Integer getOtmType() {
		return otmType;
	}
	public void setOtmType(Integer otmType) {
		this.otmType = otmType;
	}
	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}
	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}
	public BigDecimal getOtmClassSurplusFinance() {
		return otmClassSurplusFinance;
	}
	public void setOtmClassSurplusFinance(BigDecimal otmClassSurplusFinance) {
		this.otmClassSurplusFinance = otmClassSurplusFinance;
	}
	public BigDecimal getOtmClassSurplusCourseHour() {
		return otmClassSurplusCourseHour;
	}
	public void setOtmClassSurplusCourseHour(BigDecimal otmClassSurplusCourseHour) {
		this.otmClassSurplusCourseHour = otmClassSurplusCourseHour;
	}
	public BigDecimal getOtmClassTotalCharged() {
		return otmClassTotalCharged;
	}
	public void setOtmClassTotalCharged(BigDecimal otmClassTotalCharged) {
		this.otmClassTotalCharged = otmClassTotalCharged;
	}
	public String getStudentContact() {
		return studentContact;
	}
	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getOtmClassAttendanceStatus() {
		return otmClassAttendanceStatus;
	}
	public void setOtmClassAttendanceStatus(String otmClassAttendanceStatus) {
		this.otmClassAttendanceStatus = otmClassAttendanceStatus;
	}
	public BaseStatus getHasTeacherAttendance() {
		return hasTeacherAttendance;
	}
	public void setHasTeacherAttendance(BaseStatus hasTeacherAttendance) {
		this.hasTeacherAttendance = hasTeacherAttendance;
	}
	public BaseStatus getCanCharge() {
		return canCharge;
	}
	public void setCanCharge(BaseStatus canCharge) {
		this.canCharge = canCharge;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getAbsentRemark() {
		return absentRemark;
	}
	public void setAbsentRemark(String absentRemark) {
		this.absentRemark = absentRemark;
	}
	public StudentStatus getStuStatus() {
		return stuStatus;
	}
	public void setStuStatus(StudentStatus stuStatus) {
		this.stuStatus = stuStatus;
	}
	
	
}
