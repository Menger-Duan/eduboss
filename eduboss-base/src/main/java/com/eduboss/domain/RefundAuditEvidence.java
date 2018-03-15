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
 * @author lixuejun 2016-06-28
 *
 */
@Entity
@Table(name = "refund_audit_evidence")
public class RefundAuditEvidence {

	private String id;
	private RefundAuditDynamic refundAuditDynamic;
	private String fileName;
	private String evidencePath;
	private String createUserId;
	private String createTime;
	
	public RefundAuditEvidence() {
		super();
	}

	public RefundAuditEvidence(String id, RefundAuditDynamic refundAuditDynamic, String fileName,
			String evidencePath, String createUserId, String createTime) {
		super();
		this.id = id;
		this.refundAuditDynamic = refundAuditDynamic;
		this.fileName = fileName;
		this.evidencePath = evidencePath;
		this.createUserId = createUserId;
		this.createTime = createTime;
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
	@JoinColumn(name="REFUND_AUDIT_DYNAMIC_ID")
	public RefundAuditDynamic getRefundAuditDynamic() {
		return refundAuditDynamic;
	}
	
	public void setRefundAuditDynamic(RefundAuditDynamic refundAuditDynamic) {
		this.refundAuditDynamic = refundAuditDynamic;
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
	
	
}
