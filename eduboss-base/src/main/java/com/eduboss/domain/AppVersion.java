package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MobileType;

/**
 * AppVersion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "APP_VERSION")
public class AppVersion implements java.io.Serializable {

	// Fields
	private String id;
	private String appName;  //名字
	private String appId;    //ID
	private String version;  //版本号
	private String downloadUrl; //下载地址
	private String description;  //描述
	private String createTime;   
	private User createUser; 
	private MobileType mobileType;  //手机型号
	private String isUpdate; //是否强制更新
	
	// Constructors

	/** default constructor */
	public AppVersion() {
	}

	/** minimal constructor */
	public AppVersion(String id) {
		this.id = id;
	}

	/** full constructor */
	public AppVersion(String id, String appName, String appId, String version,
			String downloadUrl, String description, String createTime,
			User createUser,String isUpdate) {
		this.id = id;
		this.appName = appName;
		this.appId = appId;
		this.version = version;
		this.downloadUrl = downloadUrl;
		this.description = description;
		this.createTime = createTime;
		this.createUser = createUser;
		this.isUpdate=isUpdate;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "APP_NAME", length = 32)
	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Column(name = "APP_ID", length = 32)
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "VERSION", length = 32)
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Column(name = "DOWNLOAD_URL", length = 1000)
	public String getDownloadUrl() {
		return this.downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Column(name = "DESCRIPTION", length = 512)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CREATE_TIME", length = 32)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@ManyToOne
	@JoinColumn(name="CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MOBILE_TYPE", length = 20)
	public MobileType getMobileType() {
		return mobileType;
	}

	public void setMobileType(MobileType mobileType) {
		this.mobileType = mobileType;
	}

	@Column(name = "is_update", length = 32)	
	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	
	
}