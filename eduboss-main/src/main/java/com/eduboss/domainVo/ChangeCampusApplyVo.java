package com.eduboss.domainVo;

import java.io.Serializable;

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;

public class ChangeCampusApplyVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键 外部生成
	private String id;

	private String customerId;
	// 客户姓名
	private String customerName;
	// 客户联系方式
	private String contact;
	//意向校区id
	private String intentionCampusId;	
	// 原来的校区id
	private String oldCampusId;
	// 原来的校区等级
	private String oldOrgLevel;
	// 目标校区id
	private String newCampusId;
	// 目标校区等级
	private String newOrgLevel;
	// 转校申请理由
	private String applyReason;
	// 转校审核备注
	private String remark;
	// 创建时间
	private String createTime;
	// 创建用户id
	private String createUserId;
	// 修改时间
	private String modifyTime;
	// 修改者id
	private String modifyUserId;
	// 审核状态
	private CustomerAuditStatus auditStatus;
	// 客户状态
	private CustomerDealStatus customerStatus;
	// 资源入口
	private String resEntranceId;
	private String resEntranceName;
	// 客户原来的跟进人
	private String deliverTarget;
	private String deliverTargetName;

	// 用于条件查询
	private String createBeginDate;
	private String createEndDate;
	private String intentionCampusName;
	private String auditStatusName;

	/**
	 * 
	 */
	public ChangeCampusApplyVo() {
		super();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOldCampusId() {
		return oldCampusId;
	}

	public void setOldCampusId(String oldCampusId) {
		this.oldCampusId = oldCampusId;
	}

	public String getOldOrgLevel() {
		return oldOrgLevel;
	}

	public void setOldOrgLevel(String oldOrgLevel) {
		this.oldOrgLevel = oldOrgLevel;
	}

	public String getNewCampusId() {
		return newCampusId;
	}

	public void setNewCampusId(String newCampusId) {
		this.newCampusId = newCampusId;
	}

	public String getNewOrgLevel() {
		return newOrgLevel;
	}

	public void setNewOrgLevel(String newOrgLevel) {
		this.newOrgLevel = newOrgLevel;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public CustomerAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(CustomerAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public CustomerDealStatus getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(CustomerDealStatus customerStatus) {
		this.customerStatus = customerStatus;
	}

	public String getResEntranceId() {
		return resEntranceId;
	}

	public void setResEntranceId(String resEntranceId) {
		this.resEntranceId = resEntranceId;
	}

	public String getResEntranceName() {
		return resEntranceName;
	}

	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}

	public String getDeliverTarget() {
		return deliverTarget;
	}

	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}

	public String getDeliverTargetName() {
		return deliverTargetName;
	}

	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}

	public String getCreateBeginDate() {
		return createBeginDate;
	}

	public void setCreateBeginDate(String createBeginDate) {
		this.createBeginDate = createBeginDate;
	}

	public String getCreateEndDate() {
		return createEndDate;
	}

	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}

	public String getIntentionCampusId() {
		return intentionCampusId;
	}

	public void setIntentionCampusId(String intentionCampusId) {
		this.intentionCampusId = intentionCampusId;
	}

	public String getIntentionCampusName() {
		return intentionCampusName;
	}

	public void setIntentionCampusName(String intentionCampusName) {
		this.intentionCampusName = intentionCampusName;
	}

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}

}
