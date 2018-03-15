package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.eduboss.domain.IncomeEvidenceAdjustItem;

public class IncomeEvidenceAdjustSummaryVo {

	private String id;
	private String evidenceId;
	private BigDecimal adjustTotalAmount;
	private String evidenceRemark;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private Set<IncomeEvidenceAdjustItemVo> adjustItems = new HashSet<IncomeEvidenceAdjustItemVo>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEvidenceId() {
		return evidenceId;
	}
	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	}
	public BigDecimal getAdjustTotalAmount() {
		return adjustTotalAmount;
	}
	public void setAdjustTotalAmount(BigDecimal adjustTotalAmount) {
		this.adjustTotalAmount = adjustTotalAmount;
	}
	public String getEvidenceRemark() {
		return evidenceRemark;
	}
	public void setEvidenceRemark(String evidenceRemark) {
		this.evidenceRemark = evidenceRemark;
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
	public Set<IncomeEvidenceAdjustItemVo> getAdjustItems() {
		return adjustItems;
	}
	public void setAdjustItems(Set<IncomeEvidenceAdjustItemVo> adjustItems) {
		this.adjustItems = adjustItems;
	}
	
}
