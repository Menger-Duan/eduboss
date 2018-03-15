package com.eduboss.domainVo;

public class FeedBackVo {
	private String id;
	private String title;
	private String content;
	private String createTime;
	private String createUserId;
	private String createUserName;
	private String backType;
	private String backTypeName;
	private String backLevelIds; // 反馈相关
	private String backLevelName;
	private String isBack;
	private String replyTime; 
	
	private String orgId;
	private String orgName;
		
	private Integer feedbackNumbers;
	private String deptName;	
	private String jobName;	
	private String tels;
	
	
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getBackType() {
		return backType;
	}
	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String getBackLevelIds() {
		return backLevelIds;
	}
	public void setBackLevelIds(String backLevelIds) {
		this.backLevelIds = backLevelIds;
	}
	public String getBackLevelName() {
		return backLevelName;
	}
	public void setBackLevelName(String backLevelName) {
		this.backLevelName = backLevelName;
	}
	public String getIsBack() {
		return isBack;
	}
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}

	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getBackTypeName() {
		return backTypeName;
	}
	public void setBackTypeName(String backTypeName) {
		this.backTypeName = backTypeName;
	}
	public Integer getFeedbackNumbers() {
		return feedbackNumbers;
	}
	public void setFeedbackNumbers(Integer feedbackNumbers) {
		this.feedbackNumbers = feedbackNumbers;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getTels() {
		return tels;
	}
	public void setTels(String tels) {
		this.tels = tels;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

		

}
