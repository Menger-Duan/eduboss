package com.eduboss.domainVo;

/**
 * 
 * @author lixuejun 2016-06-28
 *
 */
public class RefundAuditEvidenceVo {

	private String id;
	private String refundAuditDynamicId;
	private String fileName;
	private String evidencePath;
	private String createUserId;
	private String createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefundAuditDynamicId() {
		return refundAuditDynamicId;
	}
	public void setRefundAuditDynamicId(String refundAuditDynamicId) {
		this.refundAuditDynamicId = refundAuditDynamicId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getEvidencePath() {
		return evidencePath;
	}
	public void setEvidencePath(String evidencePath) {
		this.evidencePath = evidencePath;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
