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

import com.eduboss.common.MsgNo;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.SysMsgType;

/**
 * 
 * @author lixuejun
 *
 */
@Entity
@Table(name="sent_record")
public class SentRecord {

	private String id;
	private MsgNo msgNo; // 系统信息编号
	private String msgName; // 系统信息名称
	private DataDict msgType; // 信息类别
	private String sendType; // 发送类型
	private String msgContent; // 信息内容
	private String sentTime; // 发送时间
	private User msgRecipient; // 接收人
	private String msgRecipientPhone; // 接收人电话
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String isReading; // 已读0，未读1
	private PushMsgType pushMsgType; // 发送类型
	private SysMsgType sysMsgType; // 系统信息类型
	private String detailId;
	private String detailedInfromation;//详细信息
	
	public SentRecord() {
		super();
	}
	public SentRecord(String id, MsgNo msgNo, String msgName,
			DataDict msgType, String sendType, String msgContent,
			String sentTime, User msgRecipient, String msgRecipientPhone,
			String createUserId, String createTime, String modifyUserId,
			String modifyTime, String isReading, PushMsgType pushMsgType,
			SysMsgType sysMsgType, String detailId) {
		super();
		this.id = id;
		this.msgNo = msgNo;
		this.msgName = msgName;
		this.msgType = msgType;
		this.sendType = sendType;
		this.msgContent = msgContent;
		this.sentTime = sentTime;
		this.msgRecipient = msgRecipient;
		this.msgRecipientPhone = msgRecipientPhone;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
		this.isReading = isReading;
		this.pushMsgType = pushMsgType;
		this.sysMsgType = sysMsgType;
		this.detailId = detailId;
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
	
	@Enumerated(EnumType.STRING)
	@Column(name="MSG_NO")
	public MsgNo getMsgNo() {
		return msgNo;
	}
	public void setMsgNo(MsgNo msgNo) {
		this.msgNo = msgNo;
	}
	
	@Column(name="MSG_NAME", length=50)
	public String getMsgName() {
		return msgName;
	}
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MSG_TYPE")
	public DataDict getMsgType() {
		return msgType;
	}
	public void setMsgType(DataDict msgType) {
		this.msgType = msgType;
	}
	
	@Column(name="SEND_TYPE", length=50)
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
	@Column(name="MSG_CONTENT", length=1000)
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	@Column(name="SENT_TIME", length=20)
	public String getSentTime() {
		return sentTime;
	}
	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MSG_RECIPIENT")
	public User getMsgRecipient() {
		return msgRecipient;
	}
	public void setMsgRecipient(User msgRecipient) {
		this.msgRecipient = msgRecipient;
	}
	
	@Column(name="MSG_RECIPIENT_PHONE", length=20)
	public String getMsgRecipientPhone() {
		return msgRecipientPhone;
	}
	public void setMsgRecipientPhone(String msgRecipientPhone) {
		this.msgRecipientPhone = msgRecipientPhone;
	}

	@Column(name="CREATE_USER_ID", length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name="CREATE_TIME", length=20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name="MODIFY_USER_ID", length=32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name="MODIFY_TIME", length=20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name="IS_READING", length=1)
	public String getIsReading() {
		return isReading;
	}
	public void setIsReading(String isReading) {
		this.isReading = isReading;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="PUSH_MSG_TYPE")
	public PushMsgType getPushMsgType() {
		return pushMsgType;
	}
	public void setPushMsgType(PushMsgType pushMsgType) {
		this.pushMsgType = pushMsgType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="SYS_MSG_TYPE")
	public SysMsgType getSysMsgType() {
		return sysMsgType;
	}
	public void setSysMsgType(SysMsgType sysMsgType) {
		this.sysMsgType = sysMsgType;
	}
	
	@Column(name="DETAIL_ID", length=32)
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	
	@Column(name="DETAILED_INFORMATION", length=1000)
	public String getDetailedInfromation() {
		return detailedInfromation;
	}
	public void setDetailedInfromation(String detailedInfromation) {
		this.detailedInfromation = detailedInfromation;
	}
	
	
}
