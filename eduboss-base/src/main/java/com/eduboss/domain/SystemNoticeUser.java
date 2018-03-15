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

@Entity
@Table(name = "SYSTEM_NOTICE_USER")
public class SystemNoticeUser implements java.io.Serializable{
	
	private String id;
	
	private SystemNotice systemNotice;
	
	private User user;
	
	private String readingTime;

	
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
	@JoinColumn(name = "SYSTEM_NOTICE_ID")
	public SystemNotice getSystemNotice() {
		return systemNotice;
	}

	public void setSystemNotice(SystemNotice systemNotice) {
		this.systemNotice = systemNotice;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "READING_TIME")
	public String getReadingTime() {
		return readingTime;
	}

	public void setReadingTime(String readingTime) {
		this.readingTime = readingTime;
	}

	

	
	
	
	

}
