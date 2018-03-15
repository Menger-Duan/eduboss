package com.eduboss.domainVo;

/**
 * 
 * @author lixuejun 2016-06-27
 *
 */
public class RefundEvidenceVo {

	private String id;
	private String refundWorkflowId;
	private String fileName;
	private String evidencePath;
	private int rate;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefundWorkflowId() {
		return refundWorkflowId;
	}
	public void setRefundWorkflowId(String refundWorkflowId) {
		this.refundWorkflowId = refundWorkflowId;
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
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
