package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.FundsChangeAuditStatus;

/**
 * 收款审批流水
 * @author lixuejun
 *
 */
@Entity
@Table(name = "funds_audit_record")
public class FundsAuditRecord {

	private int id;
	private User receiptUser;
	private String submitTime;
	private User auditUser;
	private String auditTime;
	private FundsChangeAuditStatus auditStatus;//审核状态
	private String remark;
	private String fundsChangeHistoryId;
	private String createTime;
	
	public FundsAuditRecord() {
		super();
	}
	
	public FundsAuditRecord(int id, User receiptUser, String submitTime,
			User auditUser, String auditTime, String remark, FundsChangeAuditStatus auditStatus,
			String fundsChangeHistoryId, String createTime) {
		super();
		this.id = id;
		this.receiptUser = receiptUser;
		this.submitTime = submitTime;
		this.auditUser = auditUser;
		this.auditTime = auditTime;
		this.remark = remark;
		this.auditStatus = auditStatus;
		this.fundsChangeHistoryId = fundsChangeHistoryId;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="RECEIPT_USER")
	public User getReceiptUser() {
		return receiptUser;
	}
	
	public void setReceiptUser(User receiptUser) {
		this.receiptUser = receiptUser;
	}
	
	@Column(name = "SUBMIT_TIME", length = 20)
	public String getSubmitTime() {
		return submitTime;
	}
	
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUDIT_USER")
	public User getAuditUser() {
		return auditUser;
	}
	
	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}
	
	@Column(name = "AUDIT_TIME", length = 20)
	public String getAuditTime() {
		return auditTime;
	}
	
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	
	@Column(name = "REMARK", length = 50)
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public FundsChangeAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(FundsChangeAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Column(name = "FUNDS_CHANGE_HISTORY_ID", length = 32)
	public String getFundsChangeHistoryId() {
		return fundsChangeHistoryId;
	}
	
	public void setFundsChangeHistoryId(String fundsChangeHistoryId) {
		this.fundsChangeHistoryId = fundsChangeHistoryId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
