package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MessageDeliverRecordId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class MessageDeliverRecordId implements java.io.Serializable {

	// Fields

	private String messageId;
	private String userId;

	// Constructors

	/** default constructor */
	public MessageDeliverRecordId() {
	}

	/** full constructor */
	public MessageDeliverRecordId(String messageId, String userId) {
		this.messageId = messageId;
		this.userId = userId;
	}

	// Property accessors

	@Column(name = "MESSAGE_ID", nullable = false, length = 32)
	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Column(name = "USER_ID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof MessageDeliverRecordId))
			return false;
		MessageDeliverRecordId castOther = (MessageDeliverRecordId) other;

		return ((this.getMessageId() == castOther.getMessageId()) || (this.getMessageId() != null && castOther.getMessageId() != null && this
				.getMessageId().equals(castOther.getMessageId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null && castOther.getUserId() != null && this
						.getUserId().equals(castOther.getUserId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getMessageId() == null ? 0 : this.getMessageId().hashCode());
		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		return result;
	}

}