package com.eduboss.domainVo;

public class UserInfoForMobileVo {

	private String userId;
	private String name;
	private String headImagePath;
	private String jobName;
	private String organizationId;
	private String organizationName;
	private String deptId;//部门ID
	private String dept;//所在部门名称
	private String campusId;//校区ID
	private String campus;//所在校区
	private String brenchId;//分公司ID
	private String brench;//所在分公司
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadImagePath() {
		return headImagePath;
	}
	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBrench() {
		return brench;
	}
	public void setBrench(String brench) {
		this.brench = brench;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	
	
}
