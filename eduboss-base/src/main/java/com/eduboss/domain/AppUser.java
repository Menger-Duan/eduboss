package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * AppUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "app_user")
public class AppUser implements java.io.Serializable {

	// Fields

	private String userId;
	private String appId;
	private String appTag;
	private String userName;
	private String password;
	private String description;
	private String downloadUrl;
	private String startTime;
	private String chennel;
	private String createTime;


	// Constructors

	/** default constructor */
	public AppUser() {
	}

	/** full constructor */
	public AppUser(String appId, String appTag, String userName, String password, String description,
			String downloadUrl, String startTime, String chennel, String createTime) {
		this.appId = appId;
		this.appTag = appTag;
		this.userName = userName;
		this.password = password;
		this.description = description;
		this.downloadUrl = downloadUrl;
		this.startTime = startTime;
		this.chennel = chennel;
		this.createTime = createTime;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "USER_ID", unique = true, nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "APP_ID", length = 50)
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Column(name = "APP_TAG", length = 50)
	public String getAppTag() {
		return this.appTag;
	}

	public void setAppTag(String appTag) {
		this.appTag = appTag;
	}

	@Column(name = "USER_NAME", length = 50)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "DESCRIPTION", length = 2000)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DOWNLOAD_URL", length = 200)
	public String getDownloadUrl() {
		return this.downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Column(name = "START_TIME", length = 50)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "CHENNEL", length = 20)
	public String getChennel() {
		return this.chennel;
	}

	public void setChennel(String chennel) {
		this.chennel = chennel;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
