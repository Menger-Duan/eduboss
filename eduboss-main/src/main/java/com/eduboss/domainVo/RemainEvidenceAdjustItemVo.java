package com.eduboss.domainVo;

import java.math.BigDecimal;

public class RemainEvidenceAdjustItemVo {

	private String id;
	private String summaryId;
	private String adjustProjectId;
	private String adjustProjectName;
	private BigDecimal adjustAmount;
	private String remark;
	private String evidenceId;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}
	public String getAdjustProjectId() {
		return adjustProjectId;
	}
	public void setAdjustProjectId(String adjustProjectId) {
		this.adjustProjectId = adjustProjectId;
	}
	public String getAdjustProjectName() {
		return adjustProjectName;
	}
	public void setAdjustProjectName(String adjustProjectName) {
		this.adjustProjectName = adjustProjectName;
	}
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getEvidenceId() {
		return evidenceId;
	}
	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
