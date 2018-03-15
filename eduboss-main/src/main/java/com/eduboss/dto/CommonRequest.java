package com.eduboss.dto;

import antlr.Token;


public class CommonRequest {

	private String userName;
	private String authentication;
	private String application;
	private String appTag;
	
	private String passowrd;
	private String dynacode;
	
	//imei
	private String imei;
	
	//
	private String currentVersion;
	
	private String selectOptionCategory;
	
	private String token;
	
	private String roleId;
	private String parentOrgId;
	
	private String onlyShowUndelive;
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the authentication
	 */
	public String getAuthentication() {
		return authentication;
	}
	
	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}
	
	/**
	 * @return the application
	 */
	public String getApplication() {
		return application;
	}
	
	/**
	 * @param application the application to set
	 */
	public void setApplication(String application) {
		this.application = application;
	}
	
	/**
	 * @return the passowrd
	 */
	public String getPassowrd() {
		return passowrd;
	}
	
	/**
	 * @param passowrd the passowrd to set
	 */
	public void setPassowrd(String passowrd) {
		this.passowrd = passowrd;
	}
	
	/**
	 * @return the dynacode
	 */
	public String getDynacode() {
		return dynacode;
	}
	
	/**
	 * @param dynacode the dynacode to set
	 */
	public void setDynacode(String dynacode) {
		this.dynacode = dynacode;
	}

	
	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	
	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	
	/**
	 * @return the appTag
	 */
	public String getAppTag() {
		return appTag;
	}

	
	/**
	 * @param appTag the appTag to set
	 */
	public void setAppTag(String appTag) {
		this.appTag = appTag;
	}

	
	/**
	 * @return the currentVersion
	 */
	public String getCurrentVersion() {
		return currentVersion;
	}

	
	/**
	 * @param currentVersion the currentVersion to set
	 */
	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	/**
	 * @return the selectOptionCategory
	 */
	public String getSelectOptionCategory() {
		return selectOptionCategory;
	}

	/**
	 * @param selectOptionCategory the selectOptionCategory to set
	 */
	public void setSelectOptionCategory(String selectOptionCategory) {
		this.selectOptionCategory = selectOptionCategory;
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
	 * @return the parentOrgId
	 */
	public String getParentOrgId() {
		return parentOrgId;
	}

	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId(String parentOrgId) {
		this.parentOrgId = parentOrgId;
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
	 * @return the onlyShowUndelive
	 */
	public String getOnlyShowUndelive() {
		return onlyShowUndelive;
	}

	/**
	 * @param onlyShowUndelive the onlyShowUndelive to set
	 */
	public void setOnlyShowUndelive(String onlyShowUndelive) {
		this.onlyShowUndelive = onlyShowUndelive;
	}
	
	
}
