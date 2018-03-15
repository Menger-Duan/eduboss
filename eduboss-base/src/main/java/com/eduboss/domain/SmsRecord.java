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

/**
 * SmsRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SMS_RECORD")
public class SmsRecord implements java.io.Serializable {
	
	public static final String SEND = "SEND";
	public static final String RECEIVE = "RECEIVE";

	// Fields

	private String smsId;
	private String mobileNumber;
	private String content;
	private String receiverInd;
	private String sendType;
	private User sendUser;
	private String sendTime;
	private String readInd;
	private String sendStatus;

	// Constructors

	/** default constructor */
	public SmsRecord() {
	}

	/** minimal constructor */
	public SmsRecord(String smsId, String mobileNumber) {
		this.smsId = smsId;
		this.mobileNumber = mobileNumber;
	}

	/** full constructor */
	public SmsRecord(String smsId, String mobileNumber, String content,
			String receiverInd, String sendType, User sendUser,
			String sendTime, String readInd) {
		this.smsId = smsId;
		this.mobileNumber = mobileNumber;
		this.content = content;
		this.receiverInd = receiverInd;
		this.sendType = sendType;
		this.sendUser = sendUser;
		this.sendTime = sendTime;
		this.readInd = readInd;
	}

	// Property accessors
	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
    @GeneratedValue(generator="generator")
	@Column(name = "SMS_ID", unique = true, nullable = false, length = 32)
	public String getSmsId() {
		return this.smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	@Column(name = "MOBILE_NUMBER", nullable = false, length = 32)
	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Column(name = "CONTENT", length = 70)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "RECEIVER_IND", length = 20)
	public String getReceiverInd() {
		return this.receiverInd;
	}

	public void setReceiverInd(String receiverInd) {
		this.receiverInd = receiverInd;
	}

	@Column(name = "SEND_TYPE", length = 6)
	public String getSendType() {
		return this.sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getSendUser() {
		return sendUser;
	}

	public void setSendUser(User sendUser) {
		this.sendUser = sendUser;
	}

	@Column(name = "SEND_TIME", length = 20)
	public String getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "READ_IND", length = 20)
	public String getReadInd() {
		return this.readInd;
	}

	public void setReadInd(String readInd) {
		this.readInd = readInd;
	}

	@Column(name = "SEND_STATUS", length = 20)
	public String getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

}