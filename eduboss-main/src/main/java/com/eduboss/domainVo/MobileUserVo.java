package com.eduboss.domainVo;

import com.eduboss.common.MobileType;
import com.eduboss.common.MobileUserType;
import com.eduboss.domain.User;

public class MobileUserVo {
	
	// Fields
	private String id;
	private String userId ;		// 有可能是 学生 或者 老师
	private MobileUserType userType;
	
	private String createTime;
	//private User createUser; 
	
	private String platFormUserId ; 
	private String platFormChannelId ;
	private MobileType mobileType; 
	
	private int contact;
	private String name;
	
	
	private String appVersion; //APP版本
	private String mobileSystem; // 手机系统
	private String mobileModel; //手机型号
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public MobileUserType getUserType() {
		return userType;
	}
	public void setUserType(MobileUserType userType) {
		this.userType = userType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/*public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}*/
	public int getContact() {
		return contact;
	}
	public void setContact(int contact) {
		this.contact = contact;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public MobileType getMobileType() {
		return mobileType;
	}
	public void setMobileType(MobileType mobileType) {
		this.mobileType = mobileType;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getMobileSystem() {
		return mobileSystem;
	}
	public void setMobileSystem(String mobileSystem) {
		this.mobileSystem = mobileSystem;
	}
	public String getMobileModel() {
		return mobileModel;
	}
	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}
	
	
	
}
