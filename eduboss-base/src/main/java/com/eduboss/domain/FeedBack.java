package com.eduboss.domain;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="feedback")
public class FeedBack {
	private String id;
	private String title;
	private String content;
	private String createTime;
	private User createUser;
	private DataDict backType;
	private String backLevelIds;
	private String isBack;
	private String replyTime; 
	private Organization org;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_user")
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "backType")
	@NotFound(action=NotFoundAction.IGNORE)
	public DataDict getBackType() {
		return backType;
	}
	public void setBackType(DataDict backType) {
		this.backType = backType;
	}
	
	@Column(name="backLevel",precision = 300)
	public String getBackLevelIds() {
		return backLevelIds;
	}
	public void setBackLevelIds(String backLevelIds) {
		this.backLevelIds = backLevelIds;
	}
	
	@Column(name = "isback")
	public String getIsBack() {
		return isBack;
	}
	
	public void setIsBack(String isBack) {
		this.isBack = isBack;
	}
	
	@Column(name = "reply_time")
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_id")
	public Organization getOrg() {
		return org;
	}
	public void setOrg(Organization org) {
		this.org = org;
	}
	
	

}
