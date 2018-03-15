package com.eduboss.domainVo;

import com.eduboss.common.CustomerAuditStatus;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;

/**
 * 
 * @author xiaojinwang
 * 返回给页面显示的实体      无效客户审核 记录
 */
public class InvalidCustomerRecordVo{
    
	private Long id;
	private Long parentId;
	private String customerId;
	//客户姓名
	private String name;
	//联系方式
	private String contact;
	//登记人
	private String createUserName;
	private String createCustomerUserId;
	//登记人部门
	private String createUserDept;
	//资源入口
	private String resEntranceName;
	//资源入口
	private String resEntranceId; 
	//资源来源
	private String cusType;
	//来源细分
	private String cusOrg;
	
	private CustomerDeliverType deliverType;// BRENCH-分公司市场部，BRENCH_PUBLIC_POOL-分公司公共资源，CAMPUS-校区主任，CAMPUS_PUBLIC_POOL-校区公共资源，5-资询师
	private String deliverTypeName;
	
	//最新分配人
    private String lastDeliverName; 
	//跟进人
	private String deliverTargetName;  //也用于显示查看审核结果的分配人的名字
	private String deliverTarget;// 对象deliverType＝BRENCH-分公司ID，BRENCH_PUBLIC_POOL-分公司ID，CAMPUS-校区ID，CAMPUS_PUBLIC_POOL-校区ID，SONSULTOR-咨询师ID
	//客户处理状态
	private CustomerDealStatus dealStatus;
	private String dealStatusName;
	//是否上门
	private String isAppointCome;
	//客户意向
	private String intentionOfTheCustomerId; 
	private String intentionOfTheCustomerName;
	//最新跟进记录
	private String lastRemark;
//	//距离上次跟进天数(前端计算)
//	private String lastFollowUpTime;
//	//总跟进天数
//	private String deliverTime;
	// 客户创建时间
	private String customerCreateTime;
	//审核状态
	private CustomerAuditStatus auditStatus;
    
	//客户审核之前的状态
	private CustomerDealStatus previousStatus;
	//客户审核之后的状态
	private CustomerDealStatus currentStatus;
     
	//审核记录的备注
	private String remark;
	
	//审核记录创建时间
	private String createTime;
	//审核记录创建用户id
	private String createUserId;
	//审核记录修改时间
	private String modifyTime;
	//审核记录修改者id
	private String modifyUserId;
	
    //无效理由
	private String invalidReason;
	
	//新增 无效审核记录提交者的职位  外呼 网络 咨询师
	private String jobNames;
	
	//判断之前是否审核
	private Boolean isAuditNotPass;
	
	//无效记录提交人姓名
	private String 	invalidCreateUserName;
		
	public InvalidCustomerRecordVo(){
		
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getParentId() {
		return parentId;
	}


	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}


	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateUserDept() {
		return createUserDept;
	}
	public void setCreateUserDept(String createUserDept) {
		this.createUserDept = createUserDept;
	}
	public String getResEntranceName() {
		return resEntranceName;
	}
	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}
	public String getResEntranceId() {
		return resEntranceId;
	}
	public void setResEntranceId(String resEntranceId) {
		this.resEntranceId = resEntranceId;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getCusOrg() {
		return cusOrg;
	}
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}
	public String getDeliverTargetName() {
		return deliverTargetName;
	}
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	public CustomerDealStatus getPreviousStatus() {
		return previousStatus;
	}
	public void setPreviousStatus(CustomerDealStatus previousStatus) {
		this.previousStatus = previousStatus;
	}
	public String getIsAppointCome() {
		return isAppointCome;
	}
	public void setIsAppointCome(String isAppointCome) {
		this.isAppointCome = isAppointCome;
	}
	public String getIntentionOfTheCustomerId() {
		return intentionOfTheCustomerId;
	}
	public void setIntentionOfTheCustomerId(String intentionOfTheCustomerId) {
		this.intentionOfTheCustomerId = intentionOfTheCustomerId;
	}
	public String getIntentionOfTheCustomerName() {
		return intentionOfTheCustomerName;
	}
	public void setIntentionOfTheCustomerName(String intentionOfTheCustomerName) {
		this.intentionOfTheCustomerName = intentionOfTheCustomerName;
	}
	public String getLastRemark() {
		return lastRemark;
	}
	public void setLastRemark(String lastRemark) {
		this.lastRemark = lastRemark;
	}
	public String getCustomerCreateTime() {
		return customerCreateTime;
	}
	public void setCustomerCreateTime(String customerCreateTime) {
		this.customerCreateTime = customerCreateTime;
	}
	public CustomerAuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(CustomerAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	public CustomerDealStatus getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(CustomerDealStatus currentStatus) {
		this.currentStatus = currentStatus;
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
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getCreateCustomerUserId() {
		return createCustomerUserId;
	}
	public void setCreateCustomerUserId(String createCustomerUserId) {
		this.createCustomerUserId = createCustomerUserId;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}
	public CustomerDeliverType getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(CustomerDeliverType deliverType) {
		this.deliverType = deliverType;
	}
	public String getDeliverTypeName() {
		return deliverTypeName;
	}
	public void setDeliverTypeName(String deliverTypeName) {
		this.deliverTypeName = deliverTypeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}


	public String getInvalidReason() {
		return invalidReason;
	}


	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}


	public String getJobNames() {
		return jobNames;
	}


	public void setJobNames(String jobNames) {
		this.jobNames = jobNames;
	}


	public Boolean getIsAuditNotPass() {
		return isAuditNotPass;
	}


	public void setIsAuditNotPass(Boolean isAuditNotPass) {
		this.isAuditNotPass = isAuditNotPass;
	}


	public String getInvalidCreateUserName() {
		return invalidCreateUserName;
	}


	public void setInvalidCreateUserName(String invalidCreateUserName) {
		this.invalidCreateUserName = invalidCreateUserName;
	}
 
    

    
	
}
