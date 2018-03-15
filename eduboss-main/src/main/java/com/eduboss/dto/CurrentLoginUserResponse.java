package com.eduboss.dto;

import java.util.List;
import java.util.Map;


import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.domainVo.ResourceVo;

public class CurrentLoginUserResponse extends Response {
	
	private String userId;
	private String name;
	private String account;
	private String organizationName;
	private String organizationId;
	private String orgLevel;
	private String roleName;
	private String roleId;
	private String roleCode;
	private String campusId;
	private String campusName;
	private String brenchId;
	private String brenchName;
	private String grounpId;
	private String grounpName;
	private String token;
	private String isBlCampus;
	private String isBlBrench;
	private String isBlGrounp;
	private String mobileUserId;
	private String contact;
	private String serverDate;
	private String serverTime;	
	private String allRoleName;
	private String allOrganizationName;
	private List<ResourceVo> menuList;
	
	
	private String emailAccount;
	private String emailPwd;
	private String ccpAccount;
	private String ccpPwd;
	private Integer ccpStatus;
	private String aliPath;
	
	private String parentOrgId;
	private String orgType;
	private String headImagePath;
	
	private String deptName;
	private String jobName;
	
	private String mobileType; //手机类型 ios Android
	private String platFormUserId;
	private String platFormChannelId;
	private String ccpAppId;
	private String ccpAppToken;
	
	private String isConsultor;//是否是咨询师或者咨询主管
	
	private Map<String, Integer> deptJobs;
	
    private Boolean receptionist; //判断是否具有前台 之外的非基本权限角色

	private int receptionistValue; //模式的值

	private String ossAccessUrl;

	private String institution; //机构： 用于前端页面区分星火和培优
	
	private Boolean openScan;
	
	private Boolean noneBossUser;
	/**
	 * 阿里云转换视频路径
	 */
	private String aliVideoPath;

	private String employeeNo;

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getServerDate() {
		return serverDate;
	}

	public void setServerDate(String serverDate) {
		this.serverDate = serverDate;
	}

	private List<RoleCode> userRoleCode;
	private OrganizationType organizationType;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the organizationName
	 */
	public String getOrganizationName() {
		return organizationName;
	}
	/**
	 * @param organizationName the organizationName to set
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	/**
	 * @return the organizationId
	 */
	public String getOrganizationId() {
		return organizationId;
	}
	/**
	 * @param organizationId the organizationId to set
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the campusId
	 */
	public String getCampusId() {
		return campusId;
	}
	/**
	 * @param campusId the campusId to set
	 */
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	/**
	 * @return the campusName
	 */
	public String getCampusName() {
		return campusName;
	}
	/**
	 * @param campusName the campusName to set
	 */
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	/**
	 * @return the brenchId
	 */
	public String getBrenchId() {
		return brenchId;
	}
	/**
	 * @param brenchId the brenchId to set
	 */
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	/**
	 * @return the brenchName
	 */
	public String getBrenchName() {
		return brenchName;
	}
	/**
	 * @param brenchName the brenchName to set
	 */
	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}
	/**
	 * @param roleCode the roleCode to set
	 */
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getIsBlCampus() {
		return isBlCampus;
	}
	public void setIsBlCampus(String isBlCampus) {
		this.isBlCampus = isBlCampus;
	}
	public String getIsBlBrench() {
		return isBlBrench;
	}
	public void setIsBlBrench(String isBlBrench) {
		this.isBlBrench = isBlBrench;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getGrounpId() {
		return grounpId;
	}
	public void setGrounpId(String grounpId) {
		this.grounpId = grounpId;
	}
	public String getGrounpName() {
		return grounpName;
	}
	public void setGrounpName(String grounpName) {
		this.grounpName = grounpName;
	}
	public String getIsBlGrounp() {
		return isBlGrounp;
	}
	public void setIsBlGrounp(String isBlGrounp) {
		this.isBlGrounp = isBlGrounp;
	}
	public String getMobileUserId() {
		return mobileUserId;
	}
	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public List<RoleCode> getUserRoleCode() {
		return userRoleCode;
	}
	public void setUserRoleCode(List<RoleCode> userRoleCode) {
		this.userRoleCode = userRoleCode;
	}
	public OrganizationType getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public String getAllRoleName() {
		return allRoleName;
	}

	public void setAllRoleName(String allRoleName) {
		this.allRoleName = allRoleName;
	}

	public String getAllOrganizationName() {
		return allOrganizationName;
	}

	public void setAllOrganizationName(String allOrganizationName) {
		this.allOrganizationName = allOrganizationName;
	}


	public List<ResourceVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<ResourceVo> menuList) {
		this.menuList = menuList;
	}

	public String getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	public String getEmailPwd() {
		return emailPwd;
	}

	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
	}

	public String getCcpAccount() {
		return ccpAccount;
	}

	public void setCcpAccount(String ccpAccount) {
		this.ccpAccount = ccpAccount;
	}

	public String getCcpPwd() {
		return ccpPwd;
	}

	public void setCcpPwd(String ccpPwd) {
		this.ccpPwd = ccpPwd;
	}

	public Integer getCcpStatus() {
		return ccpStatus;
	}

	public void setCcpStatus(Integer ccpStatus) {
		this.ccpStatus = ccpStatus;
	}

	public String getAliPath() {
		return aliPath;
	}

	public void setAliPath(String aliPath) {
		this.aliPath = aliPath;
	}

	public String getParentOrgId() {
		return parentOrgId;
	}

	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getHeadImagePath() {
		return headImagePath;
	}

	public void setHeadImagePath(String headImagePath) {
		this.headImagePath = headImagePath;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getPlatFormUserId() {
		return platFormUserId;
	}

	public void setPlatFormUserId(String platFormUserId) {
		this.platFormUserId = platFormUserId;
	}

	public String getPlatFormChannelId() {
		return platFormChannelId;
	}

	public void setPlatFormChannelId(String platFormChannelId) {
		this.platFormChannelId = platFormChannelId;
	}

	public String getCcpAppId() {
		return ccpAppId;
	}

	public void setCcpAppId(String ccpAppId) {
		this.ccpAppId = ccpAppId;
	}

	public String getCcpAppToken() {
		return ccpAppToken;
	}

	public void setCcpAppToken(String ccpAppToken) {
		this.ccpAppToken = ccpAppToken;
	}

	public String getIsConsultor() {
		return isConsultor;
	}

	public void setIsConsultor(String isConsultor) {
		this.isConsultor = isConsultor;
	}

	public Map<String, Integer> getDeptJobs() {
		return deptJobs;
	}

	public void setDeptJobs(Map<String, Integer> deptJobs) {
		this.deptJobs = deptJobs;
	}

	public String getOssAccessUrl() {
		return ossAccessUrl;
	}

	public void setOssAccessUrl(String ossAccessUrl) {
		this.ossAccessUrl = ossAccessUrl;
	}

	public Boolean getReceptionist() {
		return receptionist;
	}

	public void setReceptionist(Boolean receptionist) {
		this.receptionist = receptionist;
	}

	public int getReceptionistValue() {
		return receptionistValue;
	}

	public void setReceptionistValue(int receptionistValue) {
		this.receptionistValue = receptionistValue;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public Boolean getOpenScan() {
		return openScan;
	}

	public void setOpenScan(Boolean openScan) {
		this.openScan = openScan;
	}

	public Boolean getNoneBossUser() {
		return noneBossUser;
	}

	public void setNoneBossUser(Boolean noneBossUser) {
		this.noneBossUser = noneBossUser;
	}


	public void setAliVideoPath(String aliVideoPath) {
		this.aliVideoPath = aliVideoPath;
	}

	public String getAliVideoPath() {
		return aliVideoPath;
	}
}
