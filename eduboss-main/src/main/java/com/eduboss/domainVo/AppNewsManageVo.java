package com.eduboss.domainVo;

import org.apache.commons.lang3.StringUtils;

import com.eduboss.common.NewsType;
import com.eduboss.utils.PropertiesUtils;

public class AppNewsManageVo {

	private String id;
	private String createUserId;
	private String createUserName;
	private String modifyTime;
	private String createTime; 
	private NewsType type; //五大类型
	private String valideStatus; //有效状态 （VALID 有效，INVALID无效）
	private String title;
	private String coverImagePath; //封面图片
	private String coverImageScreenshot; //修改过后的封面图
	private String content;
	private String contentUrl; //内容通过url展示
	private String contentStatus; //区分是url，还是填写的有内容
	private String topButn;//置顶
	private String coverImageName;
	private String coverImageScreenshotOne;
	private String modifyUserId;
	private String modifyUserName;
	private String assistantTitle;//副标题
	private String publishUser; //发布人
	
	private String aliPath;
	
	
	public String getAliPath() {
		if(coverImageScreenshot!=null && StringUtils.isNotBlank(coverImageScreenshot) ){
			return PropertiesUtils.getStringValue("oss.access.url.prefix")+""+coverImageScreenshot;
		}else{
			return PropertiesUtils.getStringValue("oss.access.url.prefix")+""+coverImagePath;
		}
	
	}

	public void setAliPath(String aliPath) {
		this.aliPath = aliPath;
	}
	
	
	
	
	private String brenchId; //分公司信息发送给到的分公司id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public NewsType getType() {
		return type;
	}
	public void setType(NewsType type) {
		this.type = type;
	}
	public String getValideStatus() {
		return valideStatus;
	}
	public void setValideStatus(String valideStatus) {
		this.valideStatus = valideStatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	public String getCoverImageScreenshot() {
		return coverImageScreenshot;
	}
	public void setCoverImageScreenshot(String coverImageScreenshot) {
		this.coverImageScreenshot = coverImageScreenshot;
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
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	public String getTopButn() {
		return topButn;
	}
	public void setTopButn(String topButn) {
		this.topButn = topButn;
	}
	public String getCoverImagePath() {
		return coverImagePath;
	}
	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
	}
	public String getCoverImageName() {
		return coverImageName;
	}
	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
	}

	public String getCoverImageScreenshotOne() {
		return coverImageScreenshotOne;
	}

	public void setCoverImageScreenshotOne(String coverImageScreenshotOne) {
		this.coverImageScreenshotOne = coverImageScreenshotOne;
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

	public String getAssistantTitle() {
		return assistantTitle;
	}

	public void setAssistantTitle(String assistantTitle) {
		this.assistantTitle = assistantTitle;
	}

	public String getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}
	
	
	
	
}
