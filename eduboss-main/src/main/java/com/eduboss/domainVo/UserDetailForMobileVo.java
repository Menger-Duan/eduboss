package com.eduboss.domainVo;

public class UserDetailForMobileVo {

	private String userId;
	private String name;
	private String organizationId;
	private String organizationName;
	private String campusId;//校区ID
	private String campus;//所在校区
	private String brenchId;//分公司ID
	private String brench;//所在分公司
	private String orgType;
	
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
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	public String getBrench() {
		return brench;
	}
	public void setBrench(String brench) {
		this.brench = brench;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	
}
