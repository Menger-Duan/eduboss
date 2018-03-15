package com.eduboss.domainVo;

public class CourseInfoExcelVo {      
      private String courseDate;
      private String courseTime;
      private String studyManagerName;
      private String studentName;
      private String grade;
      private String subject;
      private String weekDay;
      private String planHours;
      private String teacherName;
      private String realCheck;//实际查勤
      private String checkUser;//查勤人
      
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
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
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
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getPlanHours() {
		return planHours;
	}
	public void setPlanHours(String planHours) {
		this.planHours = planHours;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getRealCheck() {
		return realCheck;
	}
	public void setRealCheck(String realCheck) {
		this.realCheck = realCheck;
	}
	public String getCheckUser() {
		return checkUser;
	}
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}
      
      
}
