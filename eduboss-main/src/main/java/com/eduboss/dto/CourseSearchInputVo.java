package com.eduboss.dto;

import com.eduboss.common.CourseStatus;
import com.eduboss.common.ProductType;

public class CourseSearchInputVo {

    private String startDate;
    private String endDate;
    private String studentId;
    private String studentIds;
    private String studentName;
    private String teacherId;
    private String teacherName;
    private String studyManagerId;
    private String studyManagerName;
    private String grade;
    private String subject;
    private CourseStatus courseStatus;
    private String exceptCharge;  //除开已扣费
    private String courseSummaryId;

    private Integer conflict;
    private Integer dayOfWeek;

    private String productName;
    
    private String blCampusId;
    
    private String courseId;

    private ProductType type;

	public Integer getConflict() {
        return conflict;
    }

    public void setConflict(Integer conflict) {
        this.conflict = conflict;
    }

    public String getStudyManagerId() {
        return studyManagerId;
    }

    public void setStudyManagerId(String studyManagerId) {
        this.studyManagerId = studyManagerId;
    }

    public String getStudyManagerName() {
        return studyManagerName;
    }

    public void setStudyManagerName(String studyManagerName) {
        this.studyManagerName = studyManagerName;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public CourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * @return the teacherId
     */
    public String getTeacherId() {
        return teacherId;
    }

    /**
     * @param teacherId the teacherId to set
     */
    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * @return the courseSummaryId
     */
    public String getCourseSummaryId() {
        return courseSummaryId;
    }

    /**
     * @param courseSummaryId the courseSummaryId to set
     */
    public void setCourseSummaryId(String courseSummaryId) {
        this.courseSummaryId = courseSummaryId;
    }

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

    public String getExceptCharge() {
        return exceptCharge;
    }

    public void setExceptCharge(String exceptCharge) {
        this.exceptCharge = exceptCharge;
    }

    public String getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(String studentIds) {
        this.studentIds = studentIds;
    }
    
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}


}
