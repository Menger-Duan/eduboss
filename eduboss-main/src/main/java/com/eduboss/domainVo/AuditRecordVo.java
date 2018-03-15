package com.eduboss.domainVo;


/**
 * 存放 无效审核和转介绍审核的记录
 * @author Administrator
 *
 */
public class AuditRecordVo {
    
	//审核日期
	private String auditDate; 
	//审核状态 审核结果 通过 不通过
	private String auditStatus;
	//审核备注
	private String auditRemark;
	//审核者  操作人
	private String auditUserName;
	
	public AuditRecordVo() {
	}

	public String getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	
	
}
