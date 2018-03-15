package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.AuditOperation;

/**
 * 
 * @author lixuejun 2016-06-13
 *
 */
@Entity
@Table(name = "refund_audit_dynamic")
public class RefundAuditDynamic {

	private String id;
	private AuditOperation operation;
	private String auditRemark;
	private User operator;
	private Organization userDept;
	private String operationTime;
	private RefundWorkflow refundWorkflow;
	private String stepName; // 步骤名称
	
	
	public RefundAuditDynamic() {
		super();
	}

	public RefundAuditDynamic(String id, AuditOperation operation,
			String auditRemark, User operator, Organization userDept,
			String operationTime, RefundWorkflow refundWorkflow) {
		super();
		this.id = id;
		this.operation = operation;
		this.auditRemark = auditRemark;
		this.operator = operator;
		this.userDept = userDept;
		this.operationTime = operationTime;
		this.refundWorkflow = refundWorkflow;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION", length = 32)
	public AuditOperation getOperation() {
		return operation;
	}

	public void setOperation(AuditOperation operation) {
		this.operation = operation;
	}

	@Column(name = "AUDIT_REMARK", length = 1000)
	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="OPERATOR")
	public User getOperator() {
		return operator;
	}

	public void setOperator(User operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_DEPT")
	public Organization getUserDept() {
		return userDept;
	}

	public void setUserDept(Organization userDept) {
		this.userDept = userDept;
	}

	@Column(name = "OPERATION_TIME", length = 20)
	public String getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(String operationTime) {
		this.operationTime = operationTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_WORKFLOW_ID")
	public RefundWorkflow getRefundWorkflow() {
		return refundWorkflow;
	}

	public void setRefundWorkflow(RefundWorkflow refundWorkflow) {
		this.refundWorkflow = refundWorkflow;
	}

	@Column(name = "STEP_NAME", length = 100)
	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	
}
