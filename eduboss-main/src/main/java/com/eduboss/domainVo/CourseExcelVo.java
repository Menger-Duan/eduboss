package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.CourseStatus;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

public class CourseExcelVo {
	
	private String courseId;      //课程ID	
	private String productName;   //报读产品	
	private String courseDate;  //上课日期
	private String campusName;  //校区名称
	private String courseTime; //上课时间
	private String studyManegerName; //学管师	
	private String studentName; //学生
	private String grade;  //年级
	private String subject; //科目	
	private String week;//上课星期
    private BigDecimal planHours;//计划课时
    private BigDecimal realHours;//实际课时
	private String teacherName;//老师
	private String courseStatusValue;	//课程状态
	private String crashInd;//是否有冲突
	
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
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
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
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getCourseStatusValue() {
		return CourseStatus.valueOf(courseStatusValue).getName();
	}
	public void setCourseStatusValue(String courseStatusValue) {
			this.courseStatusValue =courseStatusValue ;
	}
	public String getCrashInd() {
		if(crashInd.equals("0")){
			return "无冲突";
		}else{
			return crashInd+"节课冲突";
		}
	}
	public void setCrashInd(String crashInd) {
		this.crashInd = crashInd;
	}
	public String getWeek() {
		if(StringUtil.isNotEmpty(courseDate))
		return DateTools.getWeekOfDate(DateTools.getDate(courseDate));
		else
		return "";
	}
	public void setWeek(String week) {
		this.week = week;
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
}
