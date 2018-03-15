package com.eduboss.domainVo;

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.dto.Response;
/**
 * 
 * @author xiaojinwang
 * 返回给页面显示的实体      转介绍客户
 */

public class TransferCustomerRecordVo{
   
    //主键 外部生成
	private String id;
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
	//private String cusType;
	//客户来源类型 （介绍人类型）
	private String cusOrg;
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
	//客户状态  1是新客户 0是老客户
	private Integer customerStatus;
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private String preEntranceId; // 审核通过之前的资源入口
	private String preEntranceName;
	//客户分配  目标
	private String deliverTargetName;
	private String deliverTarget;
	
	//private String moreAudit;//1 表示再次审核 0表示第一次审核
	private Boolean isStudyManager;//是否是学管师  xiaojinwang 20170301
	
	//新增需求//
	private String roleCode;
	
	//新增需求 2017-03-25
	//分配对象
	private String distributeTarget;
	private String distributeTargetName;
	//权限限制
	private String orgLevel;
	//提交人
	private String createUserName;
	
	//返回是否和审核日期同一天
	private Boolean isSameDate;
	
	//意向校区
	private String intentionCampusName;
	
	private String intentionCampusId;
    private String campusOrgLevel;
    
    private String beginCreateTime;//开始登记时间 用于查询
    private String endCreateTime;//结束登记时间 用于查询
    
    private String auditTime;
    
	public TransferCustomerRecordVo(){
		
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
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getIntroducer() {
		return introducer;
	}
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}
    
//	public String getCusType() {
//		return cusType;
//	}
//	public void setCusType(String cusType) {
//		this.cusType = cusType;
//	}
	public String getCusOrg() {
		return cusOrg;
	}
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public CustomerAuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(CustomerAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Integer getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(Integer customerStatus) {
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
	public String getDeliverTargetName() {
		return deliverTargetName;
	}
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	public String getPreEntranceId() {
		return preEntranceId;
	}
	public void setPreEntranceId(String preEntranceId) {
		this.preEntranceId = preEntranceId;
	}
	public String getPreEntranceName() {
		return preEntranceName;
	}
	public void setPreEntranceName(String preEntranceName) {
		this.preEntranceName = preEntranceName;
	}
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public Boolean getIsStudyManager() {
		return isStudyManager;
	}
	public void setIsStudyManager(Boolean isStudyManager) {
		this.isStudyManager = isStudyManager;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getDistributeTarget() {
		return distributeTarget;
	}
	public void setDistributeTarget(String distributeTarget){
		this.distributeTarget = distributeTarget;
	}
	public String getDistributeTargetName() {
		return distributeTargetName;
	}
	public void setDistributeTargetName(String distributeTargetName) {
		this.distributeTargetName = distributeTargetName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getIntroducerContact() {
		return introducerContact;
	}
	public void setIntroducerContact(String introducerContact) {
		this.introducerContact = introducerContact;
	}
	public Boolean getIsSameDate() {
		return isSameDate;
	}
	public void setIsSameDate(Boolean isSameDate) {
		this.isSameDate = isSameDate;
	}
	public String getIntentionCampusName() {
		return intentionCampusName;
	}
	public void setIntentionCampusName(String intentionCampusName) {
		this.intentionCampusName = intentionCampusName;
	}
	public String getIntentionCampusId() {
		return intentionCampusId;
	}
	public void setIntentionCampusId(String intentionCampusId) {
		this.intentionCampusId = intentionCampusId;
	}
	public String getCampusOrgLevel() {
		return campusOrgLevel;
	}
	public void setCampusOrgLevel(String campusOrgLevel) {
		this.campusOrgLevel = campusOrgLevel;
	}
	public String getBeginCreateTime() {
		return beginCreateTime;
	}
	public void setBeginCreateTime(String beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}
	public String getEndCreateTime() {
		return endCreateTime;
	}
	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
    
	
}
