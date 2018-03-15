package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 营收凭证调整概要
 * @author lixuejun
 *
 */
@Entity
@Table(name = "income_evidence_adjust_summary")
public class IncomeEvidenceAdjustSummary {

	private String id;
	private String evidenceId;
	private BigDecimal adjustTotalAmount;
	private String evidenceRemark;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private Set<IncomeEvidenceAdjustItem> adjustItems = new HashSet<IncomeEvidenceAdjustItem>();
	public IncomeEvidenceAdjustSummary() {
		super();
	}

	public IncomeEvidenceAdjustSummary(String id, String evidenceId,
			BigDecimal adjustTotalAmount, String evidenceRemark,
			String createTime, String createUserId, String modifyTime,
			String modifyUserId) {
		super();
		this.id = id;
		this.evidenceId = evidenceId;
		this.adjustTotalAmount = adjustTotalAmount;
		this.evidenceRemark = evidenceRemark;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
		adjustItems = new HashSet<IncomeEvidenceAdjustItem>();
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

	@Column(name = "EVIDENCE_ID", length = 32)
	public String getEvidenceId() {
		return evidenceId;
	}

	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	}

	@Column(name = "ADJUST_TOTAL_AMOUNT", precision = 10)
	public BigDecimal getAdjustTotalAmount() {
		return adjustTotalAmount;
	}

	public void setAdjustTotalAmount(BigDecimal adjustTotalAmount) {
		this.adjustTotalAmount = adjustTotalAmount;
	}

	@Column(name = "EDIVENCE_REMARK", precision = 50)
	public String getEvidenceRemark() {
		return evidenceRemark;
	}

	public void setEvidenceRemark(String evidenceRemark) {
		this.evidenceRemark = evidenceRemark;
	}

	@Column(name = "CREATE_TIME", precision = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", precision = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", precision = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", precision = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "summary")
	@OrderBy("id ASC")
	public Set<IncomeEvidenceAdjustItem> getAdjustItems() {
		return adjustItems;
	}

	public void setAdjustItems(Set<IncomeEvidenceAdjustItem> adjustItems) {
		this.adjustItems = adjustItems;
	}
	
}
