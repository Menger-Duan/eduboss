package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;

import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.utils.PropertiesUtils;

public class SystemNoticeMobileDetailVo {

	
	private String id;
	
	private String title;
	
	private String isReading;
	
	private String filePath;
	
	private String createTime;
	
	private String createUserId;
	
	private String createUserName;
	
	private String modyfyTime;
	
	private String modifyUserId;
	
	private String modifyUserName;
	
	private String createUserOrgId;
	
	private String modifyUserOrgId;
	
	private String createUserOrg;
	
	private String modifyUserOrg;
	
	private String noticeType;
	
	private String noticeTypeName;
	
	private String realFileName;
	
	private String aliPath;
	
	private String content;
	
	private String contentUrl;
	
	private String contentStatus;
	
	public String getAliPath() {
		return PropertiesUtils.getStringValue("oss.access.url.prefix")+""+filePath;
	}

	public void setAliPath(String aliPath) {
		this.aliPath = aliPath;
	}

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

	public String getIsReading() {
		return isReading;
	}

	public void setIsReading(String isReading) {
		this.isReading = isReading;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getModyfyTime() {
		return modyfyTime;
	}

	public void setModyfyTime(String modyfyTime) {
		this.modyfyTime = modyfyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyUserName() {
		return modifyUserName;
	}

	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}

	public String getCreateUserOrg() {
		return createUserOrg;
	}

	public void setCreateUserOrg(String createUserOrg) {
		this.createUserOrg = createUserOrg;
	}

	public String getModifyUserOrg() {
		return modifyUserOrg;
	}

	public void setModifyUserOrg(String modifyUserOrg) {
		this.modifyUserOrg = modifyUserOrg;
	}

	public String getCreateUserOrgId() {
		return createUserOrgId;
	}

	public void setCreateUserOrgId(String createUserOrgId) {
		this.createUserOrgId = createUserOrgId;
	}

	public String getModifyUserOrgId() {
		return modifyUserOrgId;
	}

	public void setModifyUserOrgId(String modifyUserOrgId) {
		this.modifyUserOrgId = modifyUserOrgId;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

	public String getNoticeTypeName() {
		return noticeTypeName;
	}

	public void setNoticeTypeName(String noticeTypeName) {
		this.noticeTypeName = noticeTypeName;
	}

	public String getRealFileName() {
		return realFileName;
	}

	public void setRealFileName(String realFileName) {
		this.realFileName = realFileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	
	

	
}
