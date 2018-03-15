package com.eduboss.domain;

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
 * 
 * @author lixuejun 2016-06-15
 *
 */
@Entity
@Table(name = "refund_evidence")
public class RefundEvidence {

	private String id;
	private RefundWorkflow refundWorkflow;
	private String fileName;
	private String evidencePath;
	private int rate; 
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	public RefundEvidence() {
		super();
	}

	public RefundEvidence(String id, RefundWorkflow refundWorkflow, String fileName,
			String evidencePath, int rate, String createUserId, String createTime, String modifyUserId, String modifyTime) {
		super();
		this.id = id;
		this.refundWorkflow = refundWorkflow;
		this.fileName = fileName;
		this.evidencePath = evidencePath;
		this.rate = rate;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
	}

	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_WORKFLOW_ID")
	public RefundWorkflow getRefundWorkflow() {
		return refundWorkflow;
	}

	public void setRefundWorkflow(RefundWorkflow refundWorkflow) {
		this.refundWorkflow = refundWorkflow;
	}

	@Column(name = "FILE_NAME", length = 50)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "EVIDENCE_PATH", length = 50)
	public String getEvidencePath() {
		return evidencePath;
	}

	public void setEvidencePath(String evidencePath) {
		this.evidencePath = evidencePath;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "RATE", length = 3)
	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
