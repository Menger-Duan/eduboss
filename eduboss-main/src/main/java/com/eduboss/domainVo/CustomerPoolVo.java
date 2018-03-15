package com.eduboss.domainVo;

import com.eduboss.common.OrganizationType;

public class CustomerPoolVo {
	private String id;
	private String parentId;
	private OrganizationType orgType;
	private String orgLevel;
    private String customerPoolName;
    private String accessRoles;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public OrganizationType getOrgType() {
		return orgType;
	}
	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}
	public String getCustomerPoolName() {
		return customerPoolName;
	}
	public void setCustomerPoolName(String customerPoolName) {
		this.customerPoolName = customerPoolName;
	}
	public String getAccessRoles() {
		return accessRoles;
	}
	public void setAccessRoles(String accessRoles) {
		this.accessRoles = accessRoles;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
    
    
}
