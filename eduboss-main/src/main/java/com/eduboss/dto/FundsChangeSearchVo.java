package com.eduboss.dto;

/**
 * 收款记录列表搜索参数
 */
public class FundsChangeSearchVo {
	private String startDate;
	private String endDate;
	private String cusName;
	private String stuName;
	private String signByWho;
	private String auditUserName;
	private String gradeId;
	private String contractType;
	private String contractId;
	private String channel;
	private String checkbyWho;
	private String blCampusId;
	private String auditStatusValue;
	private String auditType;
	private String fundsChangeType;
	private String assignedAmountNotZERO;

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

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getSignByWho() {
		return signByWho;
	}

	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getCheckbyWho() {
		return checkbyWho;
	}

	public void setCheckbyWho(String checkbyWho) {
		this.checkbyWho = checkbyWho;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getAuditStatusValue() {
		return auditStatusValue;
	}

	public void setAuditStatusValue(String auditStatusValue) {
		this.auditStatusValue = auditStatusValue;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public String getFundsChangeType() {
		return fundsChangeType;
	}

	public void setFundsChangeType(String fundsChangeType) {
		this.fundsChangeType = fundsChangeType;
	}

	public String getAssignedAmountNotZERO() {
		return assignedAmountNotZERO;
	}

	public void setAssignedAmountNotZERO(String assignedAmountNotZERO) {
		this.assignedAmountNotZERO = assignedAmountNotZERO;
	}
}
