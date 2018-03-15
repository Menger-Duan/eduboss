package com.eduboss.dto;


public class CourseSummarySearchInputVo {

	private String startDate;
	private String endDate;
	// private String startTime;
	// private String endTime;
	private String courseTime;
	private String studentId;
	private String studentName;
	private String teacherName;
	private String grade;
	private String gradeId;
	private String subject;
	private String subjectId;
	private String courseStatus;
	private String studyManagerName;
    private String weekDay;
    private Boolean end;
    private String productId;
    private String productName;

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
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

	// public String getStartTime() {
	// return startTime;
	// }
	//
	// public void setStartTime(String startTime) {
	// this.startTime = startTime;
	// }
	//
	// public String getEndTime() {
	// return endTime;
	// }
	//
	// public void setEndTime(String endTime) {
	// this.endTime = endTime;
	// }

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
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

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	/**
	 * @return the gradeId
	 */
	public String getGradeId() {
		return gradeId;
	}

	/**
	 * @param gradeId
	 *            the gradeId to set
	 */
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	/**
	 * @return the subjectId
	 */
	public String getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId
	 *            the subjectId to set
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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
}
