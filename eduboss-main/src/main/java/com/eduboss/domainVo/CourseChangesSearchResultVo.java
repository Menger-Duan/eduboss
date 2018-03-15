package com.eduboss.domainVo;

public class CourseChangesSearchResultVo {

	private String courseId;
	private String studentName;
	private String gradeId;
	private String grade;
	private String subjectId;
	private String subject;
	private String teacherId;
	private String teacherName;
	private String courseDate;
	private String courseTime;
	private String planHours;
	private String realHours;
	private String auditHours;
	private String courseStatus;
	private int conflictCount;
	private String studyManagerId;
	private String studyManagerName;
	private String courseStatusName;
	private String isWashed; // TRUE：发送过冲销，未发生过冲销

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getPlanHours() {
		return planHours;
	}

	public void setPlanHours(String planHours) {
		this.planHours = planHours;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	/**
	 * @return the realHours
	 */
	public String getRealHours() {
		return realHours;
	}

	/**
	 * @param realHours
	 *            the realHours to set
	 */
	public void setRealHours(String realHours) {
		this.realHours = realHours;
	}

	/**
	 * @return the auditHours
	 */
	public String getAuditHours() {
		return auditHours;
	}

	/**
	 * @param auditHours
	 *            the auditHours to set
	 */
	public void setAuditHours(String auditHours) {
		this.auditHours = auditHours;
	}

	public int getConflictCount() {
		return conflictCount;
	}

	public void setConflictCount(int conflictCount) {
		this.conflictCount = conflictCount;
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCourseStatusName() {
		return courseStatusName;
	}

	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}

	public String getIsWashed() {
		return isWashed;
	}

	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}

}
