package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;

/**
 * 
 * @author xiaojinwang
 * 无效客户审核记录
 *
 */
@Entity
@Table(name="invalid_customer_audit")
public class InvalidCustomerRecord implements java.io.Serializable{

	private static final long serialVersionUID = -999581850204421482L;
	private Long id;
	private Long parentId;
	private String customerId;
	//审核状态
	private CustomerAuditStatus auditStatus;
	//客户审核之前的状态
	private CustomerDealStatus previousStatus;
	//客户审核之后的状态
	private CustomerDealStatus currentStatus;
	//备注
	private String remark;
	//创建时间
	private String createTime;
	//创建用户id
	private String createUserId;
	//修改时间
	private String modifyTime;
	//修改者id
	private String modifyUserId;
	
	//无效标记理由
	private String invalidRemark;
	
	/** default constructor */
	public InvalidCustomerRecord() {
	}

	/** minimal constructor */
	public InvalidCustomerRecord(Long id) {
		this.id = id;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "parent_id")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "customer_id", length = 32)
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "audit_status")
	public CustomerAuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(CustomerAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "previous_status")
	public CustomerDealStatus getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(CustomerDealStatus previousStatus) {
		this.previousStatus = previousStatus;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "current_status")
	public CustomerDealStatus getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(CustomerDealStatus currentStatus) {
		this.currentStatus = currentStatus;
	}
	@Column(name = "remark", length = 512)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name = "create_time", length = 32)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "create_user_id", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "modify_time", length = 32)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Column(name = "modify_user_id", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	@Column(name = "invalid_remark", length = 512)
	public String getInvalidRemark() {
		return invalidRemark;
	}

	public void setInvalidRemark(String invalidRemark) {
		this.invalidRemark = invalidRemark;
	}
	
	


}
