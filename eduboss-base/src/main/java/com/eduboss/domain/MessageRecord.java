package com.eduboss.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MessageDeliverType;
import com.eduboss.common.MessageType;

/**
 * MessageRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message_record")
public class MessageRecord implements java.io.Serializable {

	// Fields

	private String messageId;
	private MessageType messageType;
	private String title;
	private String content;
	private MessageDeliverType deliverType;
	private String deliverTagetId;
	private String sendUserId;
	private String sendTime;
	private String createUserId;
	private String creaetTime;
	private Set<MessageDeliverRecord> messageDeliverRecords = new HashSet<MessageDeliverRecord>(0);
	
	private String sendUserName;
	private String deliverTagetName;
	

	// Constructors

	/** default constructor */
	public MessageRecord() {
	}

	/** full constructor */
	public MessageRecord(MessageType messageType, String title, String content, MessageDeliverType deliverType, String deliverTagetId, String sendUserId, String sendTime,
			String createUserId, String creaetTime, Set<MessageDeliverRecord> messageDeliverRecords) {
		this.messageType = messageType;
		this.title = title;
		this.content = content;
		this.deliverType = deliverType;
		this.deliverTagetId = deliverTagetId;
		this.sendUserId = sendUserId;
		this.sendTime = sendTime;
		this.createUserId = createUserId;
		this.creaetTime = creaetTime;
		this.messageDeliverRecords = messageDeliverRecords;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "MESSAGE_ID", unique = true, nullable = false, length = 32)
	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "MESSAGE_TYPE", length = 32)
	public MessageType getMessageType() {
		return this.messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	@Column(name = "TITLE", length = 256)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "CONTENT", length = 15000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DELIVER_TYPE", length = 4096)
	public MessageDeliverType getDeliverType() {
		return this.deliverType;
	}

	public void setDeliverType(MessageDeliverType deliverType) {
		this.deliverType = deliverType;
	}

	@Column(name = "DELIVER_TAGET_ID", length = 32)
	public String getDeliverTagetId() {
		return this.deliverTagetId;
	}

	public void setDeliverTagetId(String deliverTagetId) {
		this.deliverTagetId = deliverTagetId;
	}

	@Column(name = "SEND_USER_ID", length = 32)
	public String getSendUserId() {
		return this.sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	@Column(name = "SEND_TIME", length = 20)
	public String getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREAET_TIME", length = 20)
	public String getCreaetTime() {
		return this.creaetTime;
	}

	public void setCreaetTime(String creaetTime) {
		this.creaetTime = creaetTime;
	}
	
	@Transient
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "messageRecord")
	public Set<MessageDeliverRecord> getMessageDeliverRecords() {
		return this.messageDeliverRecords;
	}

	public void setMessageDeliverRecords(Set<MessageDeliverRecord> messageDeliverRecords) {
		this.messageDeliverRecords = messageDeliverRecords;
	}

	@Transient
	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	@Transient
	public String getDeliverTagetName() {
		return deliverTagetName;
	}

	public void setDeliverTagetName(String deliverTagetName) {
		this.deliverTagetName = deliverTagetName;
	}
	
	
}