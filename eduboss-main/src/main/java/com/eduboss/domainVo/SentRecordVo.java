package com.eduboss.domainVo;

import com.eduboss.common.MsgNo;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.SysMsgType;

public class SentRecordVo {

	private String id;
	private MsgNo msgNo; // 系统信息编号
	private String msgName; // 系统信息名称
	private String msgTypeId; // 信息类别Id
	private String msgTypeName; // 信息类别Name
	private String sendType; // 发送类型
	private String msgContent; // 信息内容
	private String sentTime; // 发送时间
	private String msgRecipientId; // 接收人ID
	private String msgRecipientName; // 接收人名字
	private String employeeNo; // 接收人工号
	private String msgRecipientPhone; //接收人电话
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String startDate;
	private String endDate;
	private String isReading; // 已读0，未读1
	private PushMsgType pushMsgType; // 发送类型
	private SysMsgType sysMsgType; // 系统信息类型
	private String detailId;
	private String detailedInfromation;//详细信息
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public MsgNo getMsgNo() {
		return msgNo;
	}
	public void setMsgNo(MsgNo msgNo) {
		this.msgNo = msgNo;
	}
	public String getMsgName() {
		return msgName;
	}
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	public String getMsgTypeId() {
		return msgTypeId;
	}
	public void setMsgTypeId(String msgTypeId) {
		this.msgTypeId = msgTypeId;
	}
	public String getMsgTypeName() {
		return msgTypeName;
	}
	public void setMsgTypeName(String msgTypeName) {
		this.msgTypeName = msgTypeName;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getSentTime() {
		return sentTime;
	}
	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}
	public String getMsgRecipientId() {
		return msgRecipientId;
	}
	public void setMsgRecipientId(String msgRecipientId) {
		this.msgRecipientId = msgRecipientId;
	}
	public String getMsgRecipientName() {
		return msgRecipientName;
	}
	public void setMsgRecipientName(String msgRecipientName) {
		this.msgRecipientName = msgRecipientName;
	}
	public String getEmployeeNo() {
        return employeeNo;
    }
    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }
    public String getMsgRecipientPhone() {
		return msgRecipientPhone;
	}
	public void setMsgRecipientPhone(String msgRecipientPhone) {
		this.msgRecipientPhone = msgRecipientPhone;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIsReading() {
		return isReading;
	}
	public void setIsReading(String isReading) {
		this.isReading = isReading;
	}
	public PushMsgType getPushMsgType() {
		return pushMsgType;
	}
	public void setPushMsgType(PushMsgType pushMsgType) {
		this.pushMsgType = pushMsgType;
	}
	public SysMsgType getSysMsgType() {
		return sysMsgType;
	}
	public void setSysMsgType(SysMsgType sysMsgType) {
		this.sysMsgType = sysMsgType;
	}
	public String getDetailId() {
		return detailId;
	}
	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}
	public String getDetailedInfromation() {
		return detailedInfromation;
	}
	public void setDetailedInfromation(String detailedInfromation) {
		this.detailedInfromation = detailedInfromation;
	}
	
	
}
