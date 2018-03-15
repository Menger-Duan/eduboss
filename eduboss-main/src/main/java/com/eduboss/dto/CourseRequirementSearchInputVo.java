package com.eduboss.dto;

import com.eduboss.common.CourseRequirementCetegory;
import com.eduboss.common.CourseRequirementStatus;

public class CourseRequirementSearchInputVo {

	private String startDate;
	private String endDate;
	private String studentName;
	private String teacherName;
	private String grade;
	private String subject;
	private CourseRequirementStatus requirementStatus;
	private CourseRequirementCetegory requirementCetegory;
	private String createTimeStart;
	private String createTimeEnd;

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

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
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

	public CourseRequirementStatus getRequirementStatus() {
		return requirementStatus;
	}

	public void setRequirementStatus(CourseRequirementStatus requirementStatus) {
		this.requirementStatus = requirementStatus;
	}

	/**
	 * @return the requirementCetegory
	 */
	public CourseRequirementCetegory getRequirementCetegory() {
		return requirementCetegory;
	}

	/**
	 * @param requirementCetegory
	 *            the requirementCetegory to set
	 */
	public void setRequirementCetegory(
			CourseRequirementCetegory requirementCetegory) {
		this.requirementCetegory = requirementCetegory;
	}
}
