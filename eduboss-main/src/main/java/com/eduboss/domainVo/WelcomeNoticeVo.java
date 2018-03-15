package com.eduboss.domainVo;


import com.eduboss.utils.PropertiesUtils;

public class WelcomeNoticeVo {

	private String id;
	private String title;
	private String createTime;
	private String createUserName;
	private String createUserHeadImg;
	private String createUserId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateUserHeadImg() {
		return PropertiesUtils.getStringValue("oss.access.url.prefix")+"MOBILE_HEADER_BIG_"+createUserId+".jpg";
	}

	public void setCreateUserHeadImg(String createUserHeadImg) {
		this.createUserHeadImg = createUserHeadImg;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
}
