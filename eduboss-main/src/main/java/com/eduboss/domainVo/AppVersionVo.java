package com.eduboss.domainVo;

import com.eduboss.common.MobileType;

public class AppVersionVo {

	private String id;
	private String appName;  //名字
	private String appId;    //ID
	private String version;  //版本号
	private String downloadUrl; //下载路径
	private String description;  //描述
	private String createTime;   
	private String createUserId; 
	private String createUserName; 
	private MobileType mobileType;  //手机类型
	private String isUpdate; //是否强制更新  1 是 2否
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public MobileType getMobileType() {
		return mobileType;
	}
	public void setMobileType(MobileType mobileType) {
		this.mobileType = mobileType;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	
	
}
