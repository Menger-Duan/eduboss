package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.ResourcePoolJobType;

public class ResourcePoolRoleVo {
	private int id;
	private String organizationId;
	private String organizationName;
	private String roleId;
	private String roleName;
	private BigDecimal oneTimeResource;
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public BigDecimal getOneTimeResource() {
		return oneTimeResource;
	}
	public void setOneTimeResource(BigDecimal oneTimeResource) {
		this.oneTimeResource = oneTimeResource;
	}

	
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
