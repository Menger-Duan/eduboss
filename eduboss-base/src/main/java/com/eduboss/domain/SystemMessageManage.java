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
import com.eduboss.common.ValidStatus;

/**
 * 
 * @author lixuejun
 *
 */
@Entity
@Table(name="system_message_manage")
public class SystemMessageManage {

	private String id;
	private MsgNo msgNo; // 系统信息编号
	private ValidStatus status; // 状态
	private String msgName; // 系统信息名称
	private DataDict msgType; // 信息类别
	private String logicSql; // 逻辑sql
	private String labelGather; // 标签集合
	private String msgContent; // 信息内容
	private String sendType; // 发送类型
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String detailedInfromation;//详细信息
	
	public SystemMessageManage(){
		super();
	}
	
	public SystemMessageManage(String id, MsgNo msgNo, ValidStatus status,
			String msgName, DataDict msgType, String logicSql,
			String labelGather, String msgContent, String sendType,
			String createUserId, String createTime, String modifyUserId,
			String modifyTime) {
		super();
		this.id = id;
		this.msgNo = msgNo;
		this.status = status;
		this.msgName = msgName;
		this.msgType = msgType;
		this.logicSql = logicSql;
		this.labelGather = labelGather;
		this.msgContent = msgContent;
		this.sendType = sendType;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
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
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS")
	public ValidStatus getStatus() {
		return status;
	}
	public void setStatus(ValidStatus status) {
		this.status = status;
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
	
	@Column(name="LOGIC_SQL", length=1000)
	public String getLogicSql() {
		return logicSql;
	}
	public void setLogicSql(String logicSql) {
		this.logicSql = logicSql;
	}
	
	@Column(name="LABEL_GATHER", length=100)
	public String getLabelGather() {
		return labelGather;
	}
	public void setLabelGather(String labelGather) {
		this.labelGather = labelGather;
	}
	
	@Column(name="MSG_CONTENT", length=1000)
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	
	@Column(name="SEND_TYPE", length=50)
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
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
	
	@Column(name="DETAILED_INFORMATION", length=1000)
	public String getDetailedInfromation() {
		return detailedInfromation;
	}

	public void setDetailedInfromation(String detailedInfromation) {
		this.detailedInfromation = detailedInfromation;
	}
	
	
}
