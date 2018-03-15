package com.eduboss.domainVo;


public class FeedbackReplyVo {
	private String id;
	private String feedbackId;
	private String userId;
	private String userName;
	private String createTime;
	private String replyContent;
	
	private String userOrgId;
	private String userOrgName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(String feedbackId) {
		this.feedbackId = feedbackId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserOrgId() {
		return userOrgId;
	}
	public void setUserOrgId(String userOrgId) {
		this.userOrgId = userOrgId;
	}
	public String getUserOrgName() {
		return userOrgName;
	}
	public void setUserOrgName(String userOrgName) {
		this.userOrgName = userOrgName;
	}
	
	

}
