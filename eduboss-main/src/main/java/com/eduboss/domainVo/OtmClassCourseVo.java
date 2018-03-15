package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.common.AuditStatus;

public class OtmClassCourseVo {

	// Fields
	private String startDate;
	private String endDate;
	private String studentId;
	private String studentName;
	private String otmClassCourseId;
	private String otmClassId;
	private String otmClassName; // 一对多班级名称
	private String courseDate; // 课程日期
	private String courseTime; // 课程时间
	private String courseStatus;
	private String courseStatusName;
	private String courseEndTime; // 课程结束时间
	private Double courseHours;
	private String teacherId;
	private String teacherName;
	private String subjectId;
	private String subjectName;
	private String gradeId;
	private String gradeName;
	private String attendacePicName; // 课程考勤图片
	private String blCampusId;
	private String blCampusName;
	private String studyManagerName; //班主任姓名
	
	private Integer studentCount;// 报名人数
//	private Integer leaveClassPeopleNum;// 请假人数
	private Integer lateClassPeopleNum; //迟到人数
	private Integer absentClassPeopleNum;// 缺勤人数
	private Integer attendanceCount;//考勤数量
	private Integer deductionCount;//扣费数量
	private Integer chargedPeopleQuantity;// 扣费人数
	private Double chargedMoneyQuantity;// 扣费金额
	private String otmClassAttendanceStatus;// 考勤状态
	private String chargeStatus;// 扣费状态
	private Integer otmClassCourseStudentNum;// 一对多课程上课人数
	private Integer newClassPeopleNum;// 未上课人数
	private Integer completeClassPeopleNum;// 已上课人数
	private Integer noAttendanceCount;// 未考勤数量
	
	private String searchParam;//手机端搜索参数
	
	private String teacherMobile;//老师手机
	private String studyManegerMobile;//班主任手机
	
	private String searchMonth;
	private String currentRoleId;
	
	private List<StudyManagerMobileVo> studyManagerMobileVos;
	
	private int crashInd; //检测学生课程冲突
	
	private String cancelStatus; //判断是否是取消课程操作
	
	private String weekDay; //上课日期对应的星期
	private String appendCourseTime; //拼接上课时间
	private int studentNums;// 课程应到学生人数
	private String otmClassTypeId;
	
	
	private AuditStatus auditStatus; //财务审批状态
	private String auditStatusName;
	
	private String isWashed; // TRUE：发生过冲销，FALSE：未发发生过冲销
	
	private String washRemark; // 冲销原因详情
	
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
	public String getOtmClassCourseId() {
		return otmClassCourseId;
	}
	public void setOtmClassCourseId(String otmClassCourseId) {
		this.otmClassCourseId = otmClassCourseId;
	}
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
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public Double getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(Double courseHours) {
		this.courseHours = courseHours;
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
	public String getAttendacePicName() {
		return attendacePicName;
	}
	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
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
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public Integer getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}
//	public Integer getLeaveClassPeopleNum() {
//		return leaveClassPeopleNum;
//	}
//	public void setLeaveClassPeopleNum(Integer leaveClassPeopleNum) {
//		this.leaveClassPeopleNum = leaveClassPeopleNum;
//	}
	public Integer getAbsentClassPeopleNum() {
		return absentClassPeopleNum;
	}
	public void setAbsentClassPeopleNum(Integer absentClassPeopleNum) {
		this.absentClassPeopleNum = absentClassPeopleNum;
	}
	public Integer getAttendanceCount() {
		return attendanceCount;
	}
	public void setAttendanceCount(Integer attendanceCount) {
		this.attendanceCount = attendanceCount;
	}
	public Integer getDeductionCount() {
		return deductionCount;
	}
	public void setDeductionCount(Integer deductionCount) {
		this.deductionCount = deductionCount;
	}
	public Integer getChargedPeopleQuantity() {
		return chargedPeopleQuantity;
	}
	public void setChargedPeopleQuantity(Integer chargedPeopleQuantity) {
		this.chargedPeopleQuantity = chargedPeopleQuantity;
	}
	public Double getChargedMoneyQuantity() {
		return chargedMoneyQuantity;
	}
	public void setChargedMoneyQuantity(Double chargedMoneyQuantity) {
		this.chargedMoneyQuantity = chargedMoneyQuantity;
	}
	public String getOtmClassAttendanceStatus() {
		return otmClassAttendanceStatus;
	}
	public void setOtmClassAttendanceStatus(String miniClassAttendanceStatus) {
		this.otmClassAttendanceStatus = miniClassAttendanceStatus;
	}
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public Integer getOtmClassCourseStudentNum() {
		return otmClassCourseStudentNum;
	}
	public void setOtmClassCourseStudentNum(Integer miniClassCourseStudentNum) {
		this.otmClassCourseStudentNum = miniClassCourseStudentNum;
	}
	public Integer getNewClassPeopleNum() {
		return newClassPeopleNum;
	}
	public void setNewClassPeopleNum(Integer newClassPeopleNum) {
		this.newClassPeopleNum = newClassPeopleNum;
	}
	public Integer getCompleteClassPeopleNum() {
		return completeClassPeopleNum;
	}
	public void setCompleteClassPeopleNum(Integer completeClassPeopleNum) {
		this.completeClassPeopleNum = completeClassPeopleNum;
	}
	public Integer getNoAttendanceCount() {
		return noAttendanceCount;
	}
	public void setNoAttendanceCount(Integer noAttendanceCount) {
		this.noAttendanceCount = noAttendanceCount;
	}
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
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
	public String getSearchMonth() {
		return searchMonth;
	}
	public void setSearchMonth(String searchMonth) {
		this.searchMonth = searchMonth;
	}
	public String getCurrentRoleId() {
		return currentRoleId;
	}
	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
	}
	public List<StudyManagerMobileVo> getStudyManagerMobileVos() {
		return studyManagerMobileVos;
	}
	public void setStudyManagerMobileVos(
			List<StudyManagerMobileVo> studyManagerMobileVos) {
		this.studyManagerMobileVos = studyManagerMobileVos;
	}
	public int getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(int crashInd) {
		this.crashInd = crashInd;
	}
	
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getAppendCourseTime() {
		return appendCourseTime;
	}
	public void setAppendCourseTime(String appendCourseTime) {
		this.appendCourseTime = appendCourseTime;
	}
	public int getStudentNums() {
		return studentNums;
	}
	public void setStudentNums(int studentNums) {
		this.studentNums = studentNums;
	}
	public String getOtmClassTypeId() {
		return otmClassTypeId;
	}
	public void setOtmClassTypeId(String otmClassTypeId) {
		this.otmClassTypeId = otmClassTypeId;
	}
	public String getCancelStatus() {
		return cancelStatus;
	}
	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public String getIsWashed() {
		return isWashed;
	}
	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}
	public String getWashRemark() {
		return washRemark;
	}
	public void setWashRemark(String washRemark) {
		this.washRemark = washRemark;
	}

	public Integer getLateClassPeopleNum() {
		return lateClassPeopleNum;
	}

	public void setLateClassPeopleNum(Integer lateClassPeopleNum) {
		this.lateClassPeopleNum = lateClassPeopleNum;
	}
}
