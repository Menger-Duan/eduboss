package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 目标班完结审批
 * */

@Entity
@Table(name = "promise_class_end_audit")
public class PromiseClassEndAudit implements java.io.Serializable{
	
	private int id;
	private String promiseStudentId;
	private int auditStatus;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private  String remark;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "promise_class_student_id")
	public String getPromiseStudentId() {
		return promiseStudentId;
	}

	public void setPromiseStudentId(String promiseStudentId) {
		this.promiseStudentId = promiseStudentId;
	}

	@Column(name = "AUDIT_STATUS")
	public int getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID")
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
