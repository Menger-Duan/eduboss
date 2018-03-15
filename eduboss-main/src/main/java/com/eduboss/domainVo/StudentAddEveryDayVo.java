package com.eduboss.domainVo;

public class StudentAddEveryDayVo {
	/** 新增学生数量*/
	private String studentNum;
	
	/** 组织架构*/
	private String organizationId;
	private String organizationName;
	
	/** 集团名*/
	private String groupName;
	
	/** 分公司名*/
	private String branchName;
	
	/** 校区名*/
	private String campusName;

	public String getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
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

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
}
