package com.eduboss.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MobileType;
import com.eduboss.common.MobileUserType;

/**
 * MessageDeliverRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MOBILE_USER")
public class MobileUser implements java.io.Serializable {

	// Fields

	private String id;
	private String userId ;		// 有可能是 学生 
	private MobileUserType userType;
	
	private String createTime;
	private User createUser; 
	
	private String platFormUserId ; 
	private String platFormChannelId ;
	private MobileType mobileType; 
	
	private String appVersion; //APP版本
	private String mobileSystem; // 手机系统
	private String mobileModel; //手机型号
	private String modifyTime; //修改时间
	
	
	

	/** default constructor */
	public MobileUser() {
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "USER_ID", length = 20)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "USER_TYPE", length = 20)
	public MobileUserType getUserType() {
		return userType;
	}
	public void setUserType(MobileUserType userType) {
		this.userType = userType;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	@Column(name = "PLATFORM_USER_ID", length = 20)
	public String getPlatFormUserId() {
		return platFormUserId;
	}
	public void setPlatFormUserId(String platFormUserId) {
		this.platFormUserId = platFormUserId;
	}
	@Column(name = "PLATFORM_CHANNEL_ID", length = 20)
	public String getPlatFormChannelId() {
		return platFormChannelId;
	}
	public void setPlatFormChannelId(String platFormChannelId) {
		this.platFormChannelId = platFormChannelId;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "MOBILE_TYPE", length = 20)
	public MobileType getMobileType() {
		return mobileType;
	}
	public void setMobileType(MobileType mobileType) {
		this.mobileType = mobileType;
	}

	@Column(name = "APP_VERSION", length = 20)
	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Column(name = "MOBILE_SYSTEM", length = 20)
	public String getMobileSystem() {
		return mobileSystem;
	}

	public void setMobileSystem(String mobileSystem) {
		this.mobileSystem = mobileSystem;
	}

	@Column(name = "MOBILE_MODEL", length = 20)
	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}
	
	
	@Column(name = "modify_time", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}


	
	
	
	
}