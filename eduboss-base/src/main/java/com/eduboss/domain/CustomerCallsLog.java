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

import com.eduboss.common.PhoneEvent;
import com.eduboss.common.PhoneType;

/**
 * CustomerCallsLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CUSTOMER_CALLS_LOG")
public class CustomerCallsLog implements java.io.Serializable {

	// Fields

	private String id;
	private String phone;
	private User user;
	private String callsTime;
	private Integer talkTime;
	private String createTime;
	private PhoneType phoneType;
	private PhoneEvent phoneEvent;

	// Constructors
	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	/** default constructor */
	public CustomerCallsLog() {
	}


	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "PHONE", length = 32)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}



	@Column(name = "CALLS_TIME", length = 20)
	public String getCallsTime() {
		return callsTime;
	}


	public void setCallsTime(String callsTime) {
		this.callsTime = callsTime;
	}


	@Column(name = "TALK_TIME")
	public Integer getTalkTime() {
		return this.talkTime;
	}

	public void setTalkTime(Integer talkTime) {
		this.talkTime = talkTime;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="PHONE_TYPE")
	public PhoneType getPhoneType() {
		return phoneType;
	}


	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="PHONE_EVENT")
	public PhoneEvent getPhoneEvent() {
		return phoneEvent;
	}


	public void setPhoneEvent(PhoneEvent phoneEvent) {
		this.phoneEvent = phoneEvent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

}