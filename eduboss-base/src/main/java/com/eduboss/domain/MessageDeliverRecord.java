package com.eduboss.domain;

import javax.persistence.*;

/**
 * MessageDeliverRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "message_deliver_record")
public class MessageDeliverRecord implements java.io.Serializable {

	// Fields

	private int id;
	private MessageRecord messageRecord;
	private String sendTime;
	private String readTime;
	private User user;
	
	private String readUserName;
	// Constructors

	/** default constructor */
	public MessageDeliverRecord() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MESSAGE_ID", nullable = false, insertable = false, updatable = false)
	public MessageRecord getMessageRecord() {
		return this.messageRecord;
	}

	public void setMessageRecord(MessageRecord messageRecord) {
		this.messageRecord = messageRecord;
	}

	@Column(name = "SEND_TIME", length = 20)
	public String getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "READ_TIME", length = 20)
	public String getReadTime() {
		return this.readTime;
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Transient
	public String getReadUserName() {
		return readUserName;
	}

	public void setReadUserName(String readUserName) {
		this.readUserName = readUserName;
	}
}