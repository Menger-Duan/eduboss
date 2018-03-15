package com.eduboss.domainVo;


import com.eduboss.common.CourseSummaryType;

import java.math.BigDecimal;

public class CourseSummarySearchResultVo {


    private String courseSummaryId;
    private String courseRequeirmentId;
    private String studentName;
    private String studentId;
    private String grade;
    private String gradeId;
    private String subject;
    private String subjectId;
    private String teacherName;
    private String teacherId;
    private String courseTime;
    private BigDecimal courseMinutes;// 课程分钟数
    private String startDate;
    private String endDate;
    private Double spanHours;
    private Double arrengedNotUsedHours;
    private Double remainingHours;
    private String teachingManagerName;
    private String studyManagerName;
    private String courseStatus;
    private String weekDay; // 星期，为空则为非周期性排课
    private Integer weekInterval; // 星期间隔，间隔weekInterval周
    private Double planHours; // 每次课时
    private CourseSummaryType type; // 排课类型，面向谁来排课
    private String productId;
    private String productName;

    public Double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(Double planHours) {
        this.planHours = planHours;
    }

    public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public Integer getWeekInterval() {
        return weekInterval;
    }

    public void setWeekInterval(Integer weekInterval) {
        this.weekInterval = weekInterval;
    }

    public String getCourseSummaryId() {
        return courseSummaryId;
    }

    public void setCourseSummaryId(String courseSummaryId) {
        this.courseSummaryId = courseSummaryId;
    }

    public String getCourseRequeirmentId() {
        return courseRequeirmentId;
    }

    public void setCourseRequeirmentId(String courseRequeirmentId) {
        this.courseRequeirmentId = courseRequeirmentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
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

    public Double getSpanHours() {
        return spanHours;
    }

    public void setSpanHours(Double spanHours) {
        this.spanHours = spanHours;
    }

    public Double getArrengedNotUsedHours() {
        return arrengedNotUsedHours;
    }

    public void setArrengedNotUsedHours(Double arrengedNotUsedHours) {
        this.arrengedNotUsedHours = arrengedNotUsedHours;
    }

    public Double getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(Double remainingHours) {
        this.remainingHours = remainingHours;
    }

    public String getTeachingManagerName() {
        return teachingManagerName;
    }

    public void setTeachingManagerName(String teachingManagerName) {
        this.teachingManagerName = teachingManagerName;
    }

    public String getStudyManagerName() {
        return studyManagerName;
    }

    public void setStudyManagerName(String studyManagerName) {
        this.studyManagerName = studyManagerName;
    }

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public CourseSummaryType getType() {
        return type;
    }

    public void setType(CourseSummaryType type) {
        this.type = type;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getCourseMinutes() {
        return courseMinutes;
    }

    public void setCourseMinutes(BigDecimal courseMinutes) {
        this.courseMinutes = courseMinutes;
    }
}
