package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.ValidStatus;
import com.eduboss.domain.ResourcePoolRole;

public class DistributableRoleVo {

	private String roleId;
	private String roleName;
	private String relateRoleId;
	private String relateRoleName;
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
	public String getRelateRoleId() {
		return relateRoleId;
	}
	public void setRelateRoleId(String relateRoleId) {
		this.relateRoleId = relateRoleId;
	}
	public String getRelateRoleName() {
		return relateRoleName;
	}
	public void setRelateRoleName(String relateRoleName) {
		this.relateRoleName = relateRoleName;
	}

}
