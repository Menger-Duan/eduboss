package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SYSTEM_NOTICE_REPLY")
public class SystemNoticeReply implements java.io.Serializable{
	
	private String id;
	
	private SystemNotice systemNotice;
	
	private User user;
	
	private String createTime;
	
	private String content;

	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "system_notice_id")
	public SystemNotice getSystemNotice() {
		return systemNotice;
	}

	public void setSystemNotice(SystemNotice systemNotice) {
		this.systemNotice = systemNotice;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	
	
	
	

}
