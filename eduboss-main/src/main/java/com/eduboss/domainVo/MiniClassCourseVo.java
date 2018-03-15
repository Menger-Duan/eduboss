package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.AuditStatus;

/**
 * MiniClassCourse entity. @author MyEclipse Persistence Tools
 */

public class MiniClassCourseVo implements java.io.Serializable {

	// Fields

	private String startDate;
	private String endDate;
	private String miniClassCourseId;
	private String courseTime;
	private String courseStatus;
	private String courseStatusName;
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
	private String studyManegerName;
	private String subject;
	private String grade;
	private String subjectId;
	private String gradeId;
	private String classroom;
	private String classroomId;
	private String searchMonth;
	private AuditStatus auditStatus;  //课程审批状态
	private String auditStatusName;
	private String weekDay;//上课日期对应的星期 
	private int studentNums;//应到学生数
	private String appendCourseTime;//拼接课程时间
	
	private Integer mainProductCourseTime;
	
	private String cancelStatus; //判断是否是取消课程操作
	
	private String isWashed; // TRUE：发生过冲销，FALSE：未发生过冲销
	
	private String washRemark; // 冲销原因详情
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	private String miniClassType;
	private String studentId;
	private String studentName;
	private String currentRoleId;
	private Integer attendanceCount;//考勤数量
	private Integer deductionCount;//扣费数量
	private Integer chargedPeopleQuantity;// 扣费人数
	private Double chargedMoneyQuantity;// 扣费金额
	private String miniClassAttendanceStatus;// 考勤状态
	private String chargeStatus;// 扣费状态
	private BigDecimal chargeFinance;// 扣费金额
	private Integer miniClassCourseStudentNum;// 小班课程上课人数
	private Integer newClassPeopleNum;// 未上课人数
	private Integer completeClassPeopleNum;// 已上课人数
	private Integer lateClassPeopleNum;//迟到人数
//	private Integer leaveClassPeopleNum;// 请假人数
	private Integer absentClassPeopleNum;// 缺勤人数
	private Integer studentCount;// 报名人数
	private Integer noAttendanceCount;// 未考勤数量
	private String attendacePicName; // 考勤图片名称
	private Integer makeUp;// 补课人数
	private String searchParam;//手机端搜索参数
	
	private String teacherMobile;//老师手机
	private String studyManegerMobile;//班主任手机
	private String studyManegerId;
	private String completeAndLate; // 上课（迟到）

	private String courseName;//课程名称
	private int courseNum;

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public String getCurrentRoleId() {
		return currentRoleId;
	}
	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
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
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
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
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudyManegerName() {
		return studyManegerName;
	}
	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getMiniClassType() {
		return miniClassType;
	}
	public void setMiniClassType(String miniClassType) {
		this.miniClassType = miniClassType;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
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
	public Integer getCompleteClassPeopleNum() {
		return completeClassPeopleNum;
	}
	public void setCompleteClassPeopleNum(Integer completeClassPeopleNum) {
		this.completeClassPeopleNum = completeClassPeopleNum;
	}
	public Double getChargedMoneyQuantity() {
		return chargedMoneyQuantity;
	}
	public void setChargedMoneyQuantity(Double chargedMoneyQuantity) {
		this.chargedMoneyQuantity = chargedMoneyQuantity;
	}
	public String getMiniClassAttendanceStatus() {
		return miniClassAttendanceStatus;
	}
	public void setMiniClassAttendanceStatus(String miniClassAttendanceStatus) {
		this.miniClassAttendanceStatus = miniClassAttendanceStatus;
	}
	public String getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(String chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public BigDecimal getChargeFinance() {
		return chargeFinance;
	}
	public void setChargeFinance(BigDecimal chargeFinance) {
		this.chargeFinance = chargeFinance;
	}
	public Integer getMiniClassCourseStudentNum() {
		return miniClassCourseStudentNum;
	}
	public void setMiniClassCourseStudentNum(Integer miniClassCourseStudentNum) {
		this.miniClassCourseStudentNum = miniClassCourseStudentNum;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}

	public String getCrashInd() {
		return crashInd;
	}

	public void setCrashInd(String crashInd) {
		this.crashInd = crashInd;
	}

	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public Integer getNewClassPeopleNum() {
		return newClassPeopleNum;
	}
	public void setNewClassPeopleNum(Integer newClassPeopleNum) {
		this.newClassPeopleNum = newClassPeopleNum;
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
	public Integer getStudentCount() {
		return studentCount;
	}
	public void setStudentCount(Integer studentCount) {
		this.studentCount = studentCount;
	}
	public String getSearchMonth() {
		return searchMonth;
	}
	public void setSearchMonth(String searchMonth) {
		this.searchMonth = searchMonth;
	}
	public Integer getNoAttendanceCount() {
		return noAttendanceCount;
	}
	public void setNoAttendanceCount(Integer noAttendanceCount) {
		this.noAttendanceCount = noAttendanceCount;
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
	public Integer getMakeUp() {
		return makeUp;
	}
	public void setMakeUp(Integer makeUp) {
		this.makeUp = makeUp;
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
	public String getStudyManegerId() {
		return studyManegerId;
	}
	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}	
	public int getStudentNums() {
		return studentNums;
	}
	public void setStudentNums(int studentNums) {
		this.studentNums = studentNums;
	}
	public String getAppendCourseTime() {
		return appendCourseTime;
	}
	public void setAppendCourseTime(String appendCourseTime) {
		this.appendCourseTime = appendCourseTime;
	}
	public Integer getMainProductCourseTime() {
		return mainProductCourseTime;
	}
	public void setMainProductCourseTime(Integer mainProductCourseTime) {
		this.mainProductCourseTime = mainProductCourseTime;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public String getCancelStatus() {
		return cancelStatus;
	}
	public void setCancelStatus(String cancelStatus) {
		this.cancelStatus = cancelStatus;
	}

	public Integer getLateClassPeopleNum() {
		return lateClassPeopleNum;
	}
	public void setLateClassPeopleNum(Integer lateClassPeopleNum) {
		this.lateClassPeopleNum = lateClassPeopleNum;
	}
	public String getCompleteAndLate() {
		return completeAndLate;
	}
	public void setCompleteAndLate(String completeAndLate) {
		this.completeAndLate = completeAndLate;
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


	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}