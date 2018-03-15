package com.eduboss.domainVo;

import com.eduboss.common.UserType;
import com.eduboss.domain.UserOrganizationRole;

import java.util.List;

public class UserEditDto {
	private String userId;
	private UserType type;
	private List<UserOrganizationRole> userOrgRoles;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<UserOrganizationRole> getUserOrgRoles() {
		return userOrgRoles;
	}

	public void setUserOrgRoles(List<UserOrganizationRole> userOrgRoles) {
		this.userOrgRoles = userOrgRoles;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}
