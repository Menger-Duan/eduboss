package com.eduboss.domainVo;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseStatus;

import java.math.BigDecimal;


public class TwoTeacherClassCourseVo implements java.io.Serializable {

	private int courseId;
	private String twoTeacherClassId;
	private String twoTeacherClassName;
	private int twoTeacherClassTwoId; //辅班Id
	private String twoTeacherClassTwoName; //辅班

	private String courseName;
	private String courseTime;
	private BigDecimal courseMinutes;// 课程分钟数
	private CourseStatus courseStatus;
	private String courseStatusValue;
	private String courseDate;
	private Double courseHours;
	private String courseEndTime;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String attendacePicName;
	private AuditStatus auditStatus;//财务审批状态
	private String teacherAttendTime;  //老师考勤时间
	private String studyManageChargeTime; //教务扣费时间
	private String firstAttendTime; //老师第一次考勤时间

	private String courseStatusName; //课程状态
	private String auditStatusName;//审批状态

	private String subjectName;
	private String subjectId;
	private String gradeName;
	private String gradeId;
	private String productName;

	private String startDate;
	private String endDate;

	private String teacherName;//主班老师
	private String teacherId;
	private String teacherPhone;//主讲老师联系方式

	private String teacherTwoName; //辅班老师
	private String teacherTwoPhone;//辅导老师联系方式
	private String teacherTwoUserId;//辅班老师id

	private int courseNum;

	private String blCampusName;

	private String classTypeName;//班级类型

	private int studentNums;//应到学生数

	private int completeClassPeopleNum;//准时上课人数

	private int lateClassPeopleNum;//迟到人数

	private int absentClassPeopleNum;//缺勤人数

	private int noAttendanceCount; //未考勤人数

	private int deductionCount;// 扣费人数

	private int makeUp; //补课人数

	private String classroom;

	private String classroomId;

	private String isWashed; // TRUE：发生过冲销，FALSE：未发生过冲销

	private String washRemark; // 冲销原因详情

	private String searchParam; //手机端搜索参数
	
	private String weekDay;//上课日期对应的星期 
	private String appendCourseTime;//拼接课程时间
	

	public int getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}
	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getCourseStatusName() {
		return courseStatusName;
	}

	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}


	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}

	public CourseStatus getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
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

	public String getTeacherAttendTime() {
		return teacherAttendTime;
	}

	public void setTeacherAttendTime(String teacherAttendTime) {
		this.teacherAttendTime = teacherAttendTime;
	}

	public String getStudyManageChargeTime() {
		return studyManageChargeTime;
	}

	public void setStudyManageChargeTime(String studyManageChargeTime) {
		this.studyManageChargeTime = studyManageChargeTime;
	}

	public String getFirstAttendTime() {
		return firstAttendTime;
	}

	public void setFirstAttendTime(String firstAttendTime) {
		this.firstAttendTime = firstAttendTime;
	}

	public String getTwoTeacherClassId() {
		return twoTeacherClassId;
	}

	public void setTwoTeacherClassId(String twoTeacherClassId) {
		this.twoTeacherClassId = twoTeacherClassId;
	}

	public String getTwoTeacherClassName() {
		return twoTeacherClassName;
	}

	public void setTwoTeacherClassName(String twoTeacherClassName) {
		this.twoTeacherClassName = twoTeacherClassName;
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

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getTeacherTwoName() {
		return teacherTwoName;
	}

	public void setTeacherTwoName(String teacherTwoName) {
		this.teacherTwoName = teacherTwoName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getTwoTeacherClassTwoName() {
		return twoTeacherClassTwoName;
	}

	public void setTwoTeacherClassTwoName(String twoTeacherClassTwoName) {
		this.twoTeacherClassTwoName = twoTeacherClassTwoName;
	}


	public int getStudentNums() {
		return studentNums;
	}

	public void setStudentNums(int studentNums) {
		this.studentNums = studentNums;
	}

	public int getCompleteClassPeopleNum() {
		return completeClassPeopleNum;
	}

	public void setCompleteClassPeopleNum(int completeClassPeopleNum) {
		this.completeClassPeopleNum = completeClassPeopleNum;
	}

	public int getLateClassPeopleNum() {
		return lateClassPeopleNum;
	}

	public void setLateClassPeopleNum(int lateClassPeopleNum) {
		this.lateClassPeopleNum = lateClassPeopleNum;
	}

	public int getAbsentClassPeopleNum() {
		return absentClassPeopleNum;
	}

	public void setAbsentClassPeopleNum(int absentClassPeopleNum) {
		this.absentClassPeopleNum = absentClassPeopleNum;
	}

	public int getNoAttendanceCount() {
		return noAttendanceCount;
	}

	public void setNoAttendanceCount(int noAttendanceCount) {
		this.noAttendanceCount = noAttendanceCount;
	}

	public int getDeductionCount() {
		return deductionCount;
	}

	public void setDeductionCount(int deductionCount) {
		this.deductionCount = deductionCount;
	}

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}

	public int getTwoTeacherClassTwoId() {
		return twoTeacherClassTwoId;
	}

	public void setTwoTeacherClassTwoId(int twoTeacherClassTwoId) {
		this.twoTeacherClassTwoId = twoTeacherClassTwoId;
	}

	public String getTeacherPhone() {
		return teacherPhone;
	}

	public void setTeacherPhone(String teacherPhone) {
		this.teacherPhone = teacherPhone;
	}

	public String getTeacherTwoPhone() {
		return teacherTwoPhone;
	}

	public void setTeacherTwoPhone(String teacherTwoPhone) {
		this.teacherTwoPhone = teacherTwoPhone;
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

	public String getCourseStatusValue() {
		return courseStatusValue;
	}

	public void setCourseStatusValue(String courseStatusValue) {
		this.courseStatusValue = courseStatusValue;
	}

	public String getTeacherTwoUserId() {
		return teacherTwoUserId;
	}

	public void setTeacherTwoUserId(String teacherTwoUserId) {
		this.teacherTwoUserId = teacherTwoUserId;
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

	public String getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}

	public int getMakeUp() {
		return makeUp;
	}

	public void setMakeUp(int makeUp) {
		this.makeUp = makeUp;
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
	
	
}