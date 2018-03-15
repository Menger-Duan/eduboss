package com.eduboss.domainVo;

import com.eduboss.domain.UserModifyLog;

import java.util.List;

public class UserDetailDto {
	private String userId;
	private String type;
	private List<UserOrganizationRoleVo> userOrgRoles;
	private List<UserModifyLog> logs;
	private Object userInfo;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<UserOrganizationRoleVo> getUserOrgRoles() {
		return userOrgRoles;
	}

	public void setUserOrgRoles(List<UserOrganizationRoleVo> userOrgRoles) {
		this.userOrgRoles = userOrgRoles;
	}

	public List<UserModifyLog> getLogs() {
		return logs;
	}

	public void setLogs(List<UserModifyLog> logs) {
		this.logs = logs;
	}

	public Object getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Object userInfo) {
		this.userInfo = userInfo;
	}
}
