package com.eduboss.domain;

import java.io.Serializable;

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

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;

/**
 * 转校申请
 * @author xiaojinwang
 *
 */
@Entity
@Table(name="change_campus_apply")
public class ChangeCampusApply implements Serializable{

	   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//主键 外部生成
		private String id;
				
		private String customerId;
		//客户姓名
		private String customerName;
		//客户联系方式
		private String contact;
		//意向校区
		private String intentionCampusId;
		//原来的校区id
		private String oldCampusId;
		//原来的校区等级
		private String oldOrgLevel;
		//目标校区id
		private String newCampusId;
		//目标校区等级
		private String newOrgLevel;
		//转校申请理由
		private String applyReason;
		//转校审核备注
		private String remark;
		//创建时间
		private String createTime;
		//创建用户id
		private String createUserId;
		//修改时间
		private String modifyTime;
		//修改者id
		private String modifyUserId;
		//审核状态
		private CustomerAuditStatus auditStatus;
		//客户状态  
		private CustomerDealStatus customerStatus;
		//资源入口
		private DataDict resEntrance; 
		//客户原来的跟进人
		private String deliverTarget;
		/**
		 * 
		 */
		public ChangeCampusApply() {
			super();
		}
		/**
		 * @param customerId
		 * @param customerName
		 * @param contact
		 * @param oldCampusId
		 * @param oldOrgLevel
		 * @param newCampusId
		 * @param newOrgLevel
		 * @param applyReason
		 * @param remark
		 * @param createTime
		 * @param createUserId
		 * @param modifyTime
		 * @param modifyUserId
		 * @param auditStatus
		 * @param customerStatus
		 * @param resEntrance
		 * @param deliverTarget
		 */
		public ChangeCampusApply(String customerId, String customerName, String contact, String oldCampusId,
				String oldOrgLevel, String newCampusId, String newOrgLevel, String applyReason, String remark,
				String createTime, String createUserId, String modifyTime, String modifyUserId,
				CustomerAuditStatus auditStatus, CustomerDealStatus customerStatus, DataDict resEntrance,
				String deliverTarget) {
			super();
			this.customerId = customerId;
			this.customerName = customerName;
			this.contact = contact;
			this.oldCampusId = oldCampusId;
			this.oldOrgLevel = oldOrgLevel;
			this.newCampusId = newCampusId;
			this.newOrgLevel = newOrgLevel;
			this.applyReason = applyReason;
			this.remark = remark;
			this.createTime = createTime;
			this.createUserId = createUserId;
			this.modifyTime = modifyTime;
			this.modifyUserId = modifyUserId;
			this.auditStatus = auditStatus;
			this.customerStatus = customerStatus;
			this.resEntrance = resEntrance;
			this.deliverTarget = deliverTarget;
		}
		
		@Id
		@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
		@GeneratedValue(generator = "generator")
		@Column(name = "id", unique = true, nullable = false, length = 32)
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		@Column(name = "customer_id", length = 32)
		public String getCustomerId() {
			return customerId;
		}
		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}
		
		@Column(name = "customer_name", length = 32)
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		
		@Column(name = "contact", length = 32)
		public String getContact() {
			return contact;
		}
		public void setContact(String contact) {
			this.contact = contact;
		}
		
		@Column(name = "old_campus_id", length = 32)
		public String getOldCampusId() {
			return oldCampusId;
		}
		public void setOldCampusId(String oldCampusId) {
			this.oldCampusId = oldCampusId;
		}
		
		@Column(name = "old_org_level", length = 32)
		public String getOldOrgLevel() {
			return oldOrgLevel;
		}
		public void setOldOrgLevel(String oldOrgLevel) {
			this.oldOrgLevel = oldOrgLevel;
		}
		
		@Column(name = "new_campus_id", length = 32)
		public String getNewCampusId() {
			return newCampusId;
		}
		public void setNewCampusId(String newCampusId) {
			this.newCampusId = newCampusId;
		}
		
		@Column(name = "new_org_level", length = 32)
		public String getNewOrgLevel() {
			return newOrgLevel;
		}
		public void setNewOrgLevel(String newOrgLevel) {
			this.newOrgLevel = newOrgLevel;
		}
		
		
		@Column(name = "apply_reason", length = 512)
		public String getApplyReason() {
			return applyReason;
		}
		public void setApplyReason(String applyReason) {
			this.applyReason = applyReason;
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
		
		@Enumerated(EnumType.STRING)
		@Column(name = "audit_status")
		public CustomerAuditStatus getAuditStatus() {
			return auditStatus;
		}
		public void setAuditStatus(CustomerAuditStatus auditStatus) {
			this.auditStatus = auditStatus;
		}
		
		@Enumerated(EnumType.STRING)
		@Column(name = "customer_status")
		public CustomerDealStatus getCustomerStatus() {
			return customerStatus;
		}
		public void setCustomerStatus(CustomerDealStatus customerStatus) {
			this.customerStatus = customerStatus;
		}
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "res_entrance")
		public DataDict getResEntrance() {
			return resEntrance;
		}
		public void setResEntrance(DataDict resEntrance) {
			this.resEntrance = resEntrance;
		}
		
		@Column(name = "deilvertarget", length = 32)
		public String getDeliverTarget() {
			return deliverTarget;
		}
		public void setDeliverTarget(String deliverTarget) {
			this.deliverTarget = deliverTarget;
		}
		
		@Column(name = "intention_campus_id", length = 32)
		public String getIntentionCampusId() {
			return intentionCampusId;
		}
		public void setIntentionCampusId(String intentionCampusId) {
			this.intentionCampusId = intentionCampusId;
		}
		
		
		


}
