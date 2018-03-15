package com.eduboss.domain;

import javax.persistence.*;

import com.eduboss.common.PromiseAuditStatus;
import com.eduboss.common.PromiseReturnType;
import com.eduboss.common.StudentPromiseStatus;
import org.hibernate.annotations.GenericGenerator;

/**
 * 目标班学生管理（班主任）
 * @author laiyongchang
 * */

@Entity
@Table(name = "PROMISE_CLASS_STUDENT")
public class PromiseStudent implements java.io.Serializable{
	
	/**
	 * ID
	 * */
	private String id;
	
	/**
	 * 学生
	 * */
	private Student student;
	
	/**
	 * 目标班
	 * */
	private PromiseClass promiseClass;
	
	/**
	 * 合同产品
	 * */
	//private String contractProductId;
	private ContractProduct contractProduct;
	
	/**
	 * 上课时间
	 * */
	private String courseDate;
	
	/**
	 * 上课状态
	 * */
	private String courseStatus;  //0:已完结；1：进行中；
	
	/**
	 * 完结状态
	 * */
	private String resultStatus;  // 1:成功；0:失败；
	
	/**
	 * 是否退班
	 * */
	private String abortClass;
	
	/**
	 * 记录创建时间
	 * */
	private String createTime;
	
	/**
	 * 记录创建者ID
	 * */
	private String createUserId;
	
	/**
	 * 记录修改时间
	 * */
	private String modifyTime;
	
	/**
	 * 记录修改者ID
	 * */
	private String modifyUserId;

	private PromiseAuditStatus auditStatus;

	private PromiseReturnType returnType;

	private String auditRemark;

	private String oldAuditRemark;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID",unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMISE_CLASS_ID" )
	public PromiseClass getPromiseClass() {
		return promiseClass;
	}

	public void setPromiseClass(PromiseClass promiseClass) {
		this.promiseClass = promiseClass;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}

	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}

	@Column(name = "COURSE_DATE")
	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "COURSE_STATUS", length = 32)
	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}


	@Column(name = "RESULT_STATUS")
	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	@Column(name = "ABORT_CLASS")
	public String getAbortClass() {
		return abortClass;
	}

	public void setAbortClass(String abortClass) {
		this.abortClass = abortClass;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public PromiseAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(PromiseAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "RETURN_TYPE", length = 32)
	public PromiseReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(PromiseReturnType returnType) {
		this.returnType = returnType;
	}

	@Column(name = "audit_remark")
	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	@Column(name = "old_audit_remark")
	public String getOldAuditRemark() {
		return oldAuditRemark;
	}

	public void setOldAuditRemark(String oldAuditRemark) {
		this.oldAuditRemark = oldAuditRemark;
	}
}
