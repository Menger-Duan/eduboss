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

import com.eduboss.common.MsgStatus;
import com.eduboss.common.SessionType;

/**
 * MessageDeliverRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MOBILE_PUSH_MSG_USER_RECORDS")
public class MobilePushMsgUserRecord implements java.io.Serializable {

	// Fields

	private String id;
	private MobileUser mobileUser ;
	private MobilePushMsgRecord record ;
	private MobilePushMsgSession session ;
	private MsgStatus status ;
	private String pushTime ;
	private String readTime ;
	private SessionType sessionType;
	
	

	/** default constructor */
	public MobilePushMsgUserRecord() {
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOBILE_USER_ID")
	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_ID")
	public MobilePushMsgRecord getRecord() {
		return record;
	}

	public void setRecord(MobilePushMsgRecord record) {
		this.record = record;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SESSION_ID")
	public MobilePushMsgSession getSession() {
		return session;
	}

	public void setSession(MobilePushMsgSession session) {
		this.session = session;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20)
	public MsgStatus getStatus() {
		return status;
	}
	
	public void setStatus(MsgStatus status) {
		this.status = status;
	}
	
	@Column(name = "PUSH_TIME", length = 20)
	public String getPushTime() {
		return pushTime;
	}


	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	@Column(name = "READ_TIME", length = 20)
	public String getReadTime() {
		return readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SESSION_TYPE", length = 20)
	public SessionType getSessionType() {
		return sessionType;
	}
	public void setSessionType(SessionType sessionType) {
		this.sessionType = sessionType;
	}
	
	
}