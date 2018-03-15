package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.domain.StudentOrganization;

public class StudentOrganizationForm {
	List<StudentOrganizationVo> studentOrganizations;
	String studentId;
	public List<StudentOrganizationVo> getStudentOrganizations() {
		return studentOrganizations;
	}
	public void setStudentOrganizations(List<StudentOrganizationVo> studentOrganizations) {
		this.studentOrganizations = studentOrganizations;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
}
