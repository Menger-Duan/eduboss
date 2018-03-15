package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.common.AuditOperation;

public class RefundAuditDynamicVo {

	private String id;
	private AuditOperation operation;
	private String operationName;
	private String auditRemark;
	private String operatorId;
	private String operatorName;
	private String operationTime;
	private String refundWorkflowId;
	private String auditUserId;
	private String stepName;
	private int actionId;
	private String auditCampusId;
	private List<RefundAuditEvidenceVo> evidenceVos;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public AuditOperation getOperation() {
		return operation;
	}
	public void setOperation(AuditOperation operation) {
		this.operation = operation;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperationTime() {
		return operationTime;
	}
	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}
	public String getRefundWorkflowId() {
		return refundWorkflowId;
	}
	public void setRefundWorkflowId(String refundWorkflowId) {
		this.refundWorkflowId = refundWorkflowId;
	}
	public String getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public String getOperationName() {
		return operationName;
	}
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	public String getAuditCampusId() {
		return auditCampusId;
	}
	public void setAuditCampusId(String auditCampusId) {
		this.auditCampusId = auditCampusId;
	}
	public List<RefundAuditEvidenceVo> getEvidenceVos() {
		return evidenceVos;
	}
	public void setEvidenceVos(List<RefundAuditEvidenceVo> evidenceVos) {
		this.evidenceVos = evidenceVos;
	}
	
}
