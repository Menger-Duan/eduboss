package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.NewsType;

@Entity
@Table(name = "app_news_manage")
public class AppNewsManage {
	private String id;
	private User createUser; 
	private String modifyTime;
	private String createTime; 
	private NewsType type; //五大类型
	private String valideStatus; //有效状态 （VALID 有效，INVALID无效）
	private String title;
	private String coverImagePath; //封面图片
	private String coverImageScreenshot; //修改过后的图
	private String content;
	private String contentUrl; //内容通过url展示
	private String contentStatus; //区分是url，还是填写的有内容
	private String topButn;//置顶
	private String  coverImageName;
	private String coverImageScreenshotOne; //一比一截图
	private User modifyUser; 
	private String assistantTitle;//副标题 
	private String publishUser;//发布人  存文字
	
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER")
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	
	
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="TYPE")
	public NewsType getType() {
		return type;
	}
	public void setType(NewsType type) {
		this.type = type;
	}
	
	@Column(name = "VALIDE_STATUS", length = 30)
	public String getValideStatus() {
		return valideStatus;
	}
	public void setValideStatus(String valideStatus) {
		this.valideStatus = valideStatus;
	}
	
	@Column(name = "TITLE", length = 50)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "CONTENT_URL", length = 100)
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	
	@Column(name = "CONTENT_STATUS", length = 20)
	public String getContentStatus() {
		return contentStatus;
	}
	public void setContentStatus(String contentStatus) {
		this.contentStatus = contentStatus;
	}
	
	@Column(name = "COVER_IMAGE_SCREENSHOT", length = 100)
	public String getCoverImageScreenshot() {
		return coverImageScreenshot;
	}
	public void setCoverImageScreenshot(String coverImageScreenshot) {
		this.coverImageScreenshot = coverImageScreenshot;
	}
	
	@Column(name = "TOP_BUTN", length = 10)
	public String getTopButn() {
		return topButn;
	}
	public void setTopButn(String topButn) {
		this.topButn = topButn;
	}
	
	@Column(name = "COVER_IMAGE_NAME", length = 100)
	public String getCoverImageName() {
		return coverImageName;
	}
	public void setCoverImageName(String coverImageName) {
		this.coverImageName = coverImageName;
	}
	
	@Column(name = "COVER_IMAGE_PATH", length = 64)
	public String getCoverImagePath() {
		return coverImagePath;
	}
	public void setCoverImagePath(String coverImagePath) {
		this.coverImagePath = coverImagePath;
	}
	
	@Column(name = "COVER_IMAGE_SCREENSHOT_ONE", length = 100)
	public String getCoverImageScreenshotOne() {
		return coverImageScreenshotOne;
	}
	public void setCoverImageScreenshotOne(String coverImageScreenshotOne) {
		this.coverImageScreenshotOne = coverImageScreenshotOne;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER")
	public User getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	@Column(name = "ASSISTANT_TITLE", length = 60)	
	public String getAssistantTitle() {
		return assistantTitle;
	}
	public void setAssistantTitle(String assistantTitle) {
		this.assistantTitle = assistantTitle;
	}
	
	@Column(name = "PUBLISH_USER", length = 30)	
	public String getPublishUser() {
		return publishUser;
	}
	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}
	
	
	
	
	
	
	
	
	
	

}
