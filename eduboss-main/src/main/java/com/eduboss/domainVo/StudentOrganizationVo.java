package com.eduboss.domainVo;

public class StudentOrganizationVo {
	private String id;
	private String student;
	private String studentName;
	private String organization;
	private String organizationName;
	private String studyManager;
	private String studyManagerName;
	private String isMainOrganization;//是否是主校区
	private String oper;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getStudyManager() {
		return studyManager;
	}
	public void setStudyManager(String studyManager) {
		this.studyManager = studyManager;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getIsMainOrganization() {
		return isMainOrganization;
	}
	public void setIsMainOrganization(String isMainOrganization) {
		this.isMainOrganization = isMainOrganization;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	
}
