package com.eduboss.domainVo;

import java.math.BigDecimal;

public class CourseWeekVo {
	
	private String courseId;      //课程ID	
	private String productName;   //报读产品	
	private String courseDate;  //上课日期
	private String studyManegerName; //学管师	
	private String studentName; //学生
	private String grade;  //年级
	private String subject; //科目	
	private String courseTime; //上课时间
    private BigDecimal planHours;//计划课时
    private BigDecimal realHours;//实际课时
    private String teacherName;//老师
	private String courseStatus;	//课程状态
	private String courseStatusValue;	//课程状态
	private Integer crashInd;//是否有冲突
	private String campusId;  // 校区id
	private String campusName;  //校区名称
	private String isWashed; // TRUE：发送过冲销，未发生过冲销
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getStudyManegerName() {
		return studyManegerName;
	}
	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
	}	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}	
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public BigDecimal getPlanHours() {
		return planHours;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}
	public BigDecimal getRealHours() {
		return realHours;
	}
	public void setRealHours(BigDecimal realHours) {
		this.realHours = realHours;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public Integer getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(Integer crashInd) {
		this.crashInd = crashInd;
	}
	public String getCourseStatusValue() {
		return courseStatusValue;
	}
	public void setCourseStatusValue(String courseStatusValue) {
		this.courseStatusValue = courseStatusValue;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	public String getIsWashed() {
		return isWashed;
	}
	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}
	
}
