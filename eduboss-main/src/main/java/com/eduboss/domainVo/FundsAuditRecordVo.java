package com.eduboss.domainVo;

/**
 * 收款审批流水Vo
 * @author lixuejun
 *
 */
public class FundsAuditRecordVo {

	private int id;
	private String receiptUserId;
	private String receiptUserName;
	private String submitTime;
	private String auditUserId;
	private String auditUserName;
	private String auditTime;
	private String remark;
	private String auditStatusName;
	private String auditStatusValue;
	private String fundsChangeHistoryId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReceiptUserId() {
		return receiptUserId;
	}
	public void setReceiptUserId(String receiptUserId) {
		this.receiptUserId = receiptUserId;
	}
	public String getReceiptUserName() {
		return receiptUserName;
	}
	public void setReceiptUserName(String receiptUserName) {
		this.receiptUserName = receiptUserName;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getAuditUserName() {
		return auditUserName;
	}
	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public String getAuditStatusValue() {
		return auditStatusValue;
	}
	public void setAuditStatusValue(String auditStatusValue) {
		this.auditStatusValue = auditStatusValue;
	}
	public String getFundsChangeHistoryId() {
		return fundsChangeHistoryId;
	}
	public void setFundsChangeHistoryId(String fundsChangeHistoryId) {
		this.fundsChangeHistoryId = fundsChangeHistoryId;
	}
	
}
