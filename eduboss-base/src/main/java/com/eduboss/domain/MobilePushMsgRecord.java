package com.eduboss.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MsgStatus;
import com.eduboss.common.StoredDataType;



/**
 * 消息推送 不同与之前的 awr 推送， 用于手机的推送消息 
 */
@Entity
@Table(name = "MOBILE_PUSH_MSG_RECORD")
public class MobilePushMsgRecord implements java.io.Serializable {

	// Fields
	private String id;
	private MobilePushMsgSession session;
//	private String sessionType;
	
	private String createTime;
	private MobileUser sender; 
	//private MobileUser receiver;
	private StoredDataType dataType;
    private String msgTitle;
	private String msgContent;
	private MsgStatus msgStatus;
	
	// Constructors

	/** default constructor */
	public MobilePushMsgRecord() {
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
	@JoinColumn(name = "SESSION_ID")
	public MobilePushMsgSession getSession() {
		return session;
	}
	public void setSession(MobilePushMsgSession session) {
		this.session = session;
	}


//	@Column(name = "SESSION_TYPE", length = 20)
//	public String getSessionType() {
//		return sessionType;
//	}
//	public void setSessionType(String sessionType) {
//		this.sessionType = sessionType;
//	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SENDER")
	public MobileUser getSender() {
		return sender;
	}
	public void setSender(MobileUser sender) {
		this.sender = sender;
	}
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "RECEIVER")
//	public MobileUser getReceiver() {
//		return receiver;
//	}
//	public void setReceiver(MobileUser receiver) {
//		this.receiver = receiver;
//	}


	@Column(name = "MSG_CONTENT", length = 10000)
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MSG_DATA_TYPE", length = 20)
	public StoredDataType getDataType() {
		return dataType;
	}
	public void setDataType(StoredDataType dataType) {
		this.dataType = dataType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MSG_STATUS", length = 20)
	public MsgStatus getMsgStatus() {
		return msgStatus;
	}
	public void setMsgStatus(MsgStatus msgStatus) {
		this.msgStatus = msgStatus;
	}

    @Column(name = "MSG_TITLE", length = 100)
    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }
}