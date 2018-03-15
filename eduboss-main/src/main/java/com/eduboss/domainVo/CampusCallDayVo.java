package com.eduboss.domainVo;

public class CampusCallDayVo {

	/** 来电量*/
	private String callNum;
	
	/** 组织架构*/
	private String organizationId;
	private String organizationName;
	
	/** 集团名*/
	private String groupName;
	
	/** 分公司名*/
	private String branchName;
	
	/** 校区名*/
	private String campusName;
	
	

	public String getCallNum() {
		return callNum;
	}

	public void setCallNum(String callNum) {
		this.callNum = callNum;
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
	
}
