package com.eduboss.domainJdbc;

import java.math.BigDecimal;

import com.eduboss.common.CourseStatus;

public class CourseWeekJdbc {
	
	private String courseId;      //课程ID
	private String productName;   //报读产品	
	private String courseDate;  //上课日期
	private String studyManagerName; //学管师
	private String studentId;
	private String studentName; //学生
	private String gradeName;  //年级
	private String subjectName; //科目	
	private String courseTime; //上课时间
    private BigDecimal planHours;//计划课时
    private BigDecimal realHours;//实际课时
    private String teacherId;
    private String teacherName;//老师
    private CourseStatus courseStatus;
	private Integer crashInd;//是否有冲突
	private String campusId;  // 校区id
	private String campusName;  //校区名称
	
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
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
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
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}
	public Integer getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(Integer crashInd) {
		this.crashInd = crashInd;
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
	
}
