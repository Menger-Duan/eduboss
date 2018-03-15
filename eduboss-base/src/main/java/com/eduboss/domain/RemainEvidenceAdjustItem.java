package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 剩余资金凭证调整项
 * @author lixuejun
 *
 */
@Entity
@Table(name = "remain_evidence_item")
public class RemainEvidenceAdjustItem {

	private String id;
	private RemainEvidenceAdjustSummary summary;
	private DataDict adjustProject;
	private BigDecimal adjustAmount;
	private String remark;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public RemainEvidenceAdjustItem() {
		super();
	}

	public RemainEvidenceAdjustItem(String id, RemainEvidenceAdjustSummary summary,
			DataDict adjustProject, BigDecimal adjustAmount, String remark,
			String createTime, String createUserId, String modifyTime,
			String modifyUserId) {
		super();
		this.id = id;
		this.summary = summary;
		this.adjustProject = adjustProject;
		this.adjustAmount = adjustAmount;
		this.remark = remark;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUMMARY_ID")
	public RemainEvidenceAdjustSummary getSummary() {
		return summary;
	}
	
	public void setSummary(RemainEvidenceAdjustSummary summary) {
		this.summary = summary;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADJUST_PROJECT")
	public DataDict getAdjustProject() {
		return adjustProject;
	}

	public void setAdjustProject(DataDict adjustProject) {
		this.adjustProject = adjustProject;
	}

	@Column(name = "ADJUST_AMOUNT", precision = 10)
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	@Column(name = "REMARK", precision = 50)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	
}
