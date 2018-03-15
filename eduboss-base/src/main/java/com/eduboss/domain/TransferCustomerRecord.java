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

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.dto.Response;

/**
 * 
 * @author xiaojinwang
 * 转介绍客户审核记录
 *
 */
@Entity
@Table(name="transfer_introduce_customer")
public class TransferCustomerRecord implements java.io.Serializable{


	private static final long serialVersionUID = 91932586059324058L;
	
    //主键 外部生成
	private String id;
	
	//父节点
	private String parentId;
	
	private String customerId;
	//客户姓名
	private String customerName;
	//学生id
	private String studentId;
	//学生姓名
	private String studentName;
	//客户联系方式
	private String contact;
	//介绍人姓名
	private String introducer;
	//介绍人电话
	private String introducerContact;
	//客户类型 统一为介绍INTRODUCE
	//private DataDict cusType;
	//客户来源类型 （介绍人类型）
	private DataDict cusOrg;
	//创建时间
	private String createTime;
	//创建用户id
	private String createUserId;
	//修改时间
	private String modifyTime;
	//修改者id
	private String modifyUserId;
	//备注
	private String remark;
	//审核状态
	private CustomerAuditStatus auditStatus;
	//客户状态  1是新客户 0 是老客户
	private Integer customerStatus;
	//资源入口
	private DataDict resEntrance; 
	//审核通过之前的资源入口
	private DataDict preEntrance;
	//权限限制
	private String orgLevel;
	//新增需求 2017-03-25
	//分配对象
	private String distributeTarget;
	private String distributeTargetName;
	
	//意向校区
	private Organization intentionCampus;
	//意向校区orgLevel
	private String campusOrgLevel;

	//审批时间
	private String auditTime;
	
	/** default constructor */
	public TransferCustomerRecord() {
	}

	/** minimal constructor */
	public TransferCustomerRecord(String id) {
		this.id = id;
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
	
	@Column(name = "student_id", length = 32)
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "student_name", length = 32)
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Column(name = "contact", length = 32,unique = true)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	@Column(name = "introducer", length = 32)
	public String getIntroducer() {
		return introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}
	
	@Column(name = "introducer_contact", length = 32)
    public String getIntroducerContact() {
		return introducerContact;
	}

	public void setIntroducerContact(String introducerContact) {
		this.introducerContact = introducerContact;
	}

	//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "cus_type")
//	public DataDict getCusType() {
//		return cusType;
//	}
//
//	public void setCusType(DataDict cusType) {
//		this.cusType = cusType;
//	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "introducer_type")
	public DataDict getCusOrg() {
		return cusOrg;
	}

	public void setCusOrg(DataDict cusOrg) {
		this.cusOrg = cusOrg;
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
	@Column(name = "remark", length = 512)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "audit_status")
	public CustomerAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(CustomerAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Column(name = "customer_status")
	public Integer getCustomerStatus() {
		return customerStatus;
	}

	public void setCustomerStatus(Integer customerStatus) {
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pre_entrance")
	public DataDict getPreEntrance() {
		return preEntrance;
	}

	public void setPreEntrance(DataDict preEntrance) {
		this.preEntrance = preEntrance;
	}

	@Column(name = "parent_id", length = 32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "org_level", length = 32)
	public String getOrgLevel() {
		return orgLevel;
	}
	
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	@Column(name = "distribute_target", length = 32)
	public String getDistributeTarget() {
		return distributeTarget;
	}

	public void setDistributeTarget(String distributeTarget) {
		this.distributeTarget = distributeTarget;
	}

	@Column(name = "distribute_target_name", length = 32)
	public String getDistributeTargetName() {
		return distributeTargetName;
	}

	public void setDistributeTargetName(String distributeTargetName) {
		this.distributeTargetName = distributeTargetName;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="intention_campus_id")
	public Organization getIntentionCampus() {
		return intentionCampus;
	}

	public void setIntentionCampus(Organization intentionCampus) {
		this.intentionCampus = intentionCampus;
	}

	@Column(name = "campus_org_level")
	public String getCampusOrgLevel() {
		return campusOrgLevel;
	}

	public void setCampusOrgLevel(String campusOrgLevel) {
		this.campusOrgLevel = campusOrgLevel;
	}
	@Column(name = "audit_time", length = 32)
	public String getAuditTime() {
		return auditTime;
	}
	
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	
}
