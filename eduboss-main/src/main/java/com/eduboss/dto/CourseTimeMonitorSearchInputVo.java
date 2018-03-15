package com.eduboss.dto;


public class CourseTimeMonitorSearchInputVo {

	private String startDate;
	private String endDate;
	private String blCampusId;
	private String studyManagerId;
	private String studentId;
	private String schoolId;
	private String gradeId;
    private String status; // 学生状态
    
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
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
