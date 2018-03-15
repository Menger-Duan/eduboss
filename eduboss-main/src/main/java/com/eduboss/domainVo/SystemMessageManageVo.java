package com.eduboss.domainVo;

import com.eduboss.common.MsgNo;
import com.eduboss.common.ValidStatus;

public class SystemMessageManageVo {

	private String id;
	private MsgNo msgNo; // 系统信息编号
	private ValidStatus status; // 状态
	private String msgName; // 系统信息名称
	private String msgTypeId; // 信息类别Id
	private String msgTypeName; // 信息类别Name
	private String logicSql; // 逻辑sql
	private String labelGather; // 标签集合
	private String msgContent; // 信息内容
	private String sendType; // 发送类型
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
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
	public ValidStatus getStatus() {
		return status;
	}
	public void setStatus(ValidStatus status) {
		this.status = status;
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
	public String getLogicSql() {
		return logicSql;
	}
	public void setLogicSql(String logicSql) {
		this.logicSql = logicSql;
	}
	public String getLabelGather() {
		return labelGather;
	}
	public void setLabelGather(String labelGather) {
		this.labelGather = labelGather;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
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
	public String getDetailedInfromation() {
		return detailedInfromation;
	}
	public void setDetailedInfromation(String detailedInfromation) {
		this.detailedInfromation = detailedInfromation;
	}
	
	
	
}
