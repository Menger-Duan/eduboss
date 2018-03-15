package com.eduboss.domainVo;

import com.eduboss.common.CustomerDealStatus;

/**
 * @description 跟进客户管理列表 VO
 * @date 2016-08-22
 * @author xiaojinwang
 *
 */
public class FollowupCustomerVo {
	//客户姓名
	private String cusName;
	//客户ID
	private String cusId;
	//联系方式
	private String contact;
	//登记人
	private String createUserName;
	private String createCustomerUserId;
	//登记人部门
	private String createUserDept;
	//资源入口Id
	private String resEntranceId;
    //资源入口名称
	private String resEntranceName;
	//资源来源
	private String cusType;
	//来源细分
	private String cusOrg;
	//最新分配人
    private String lastDeliverName; 
	//客户处理状态
	private CustomerDealStatus dealStatus;
	private String dealStatusName;
	//是否上门
	private String isAppointCome;
	//意向度Id
	private String intentionOfTheCustomerId;
	//意向度Name
	private String intentionOfTheCustomerName;
	//最新跟进记录
	private String lastRemark;
	//距离上次跟进天数(前端计算)
	private String lastFollowUpTime;
	//总跟进天数
	private String deliverTime;
	//客户跟进时间的开始时间(用于条件查询)
	private String followupBeginDate;
	//客户跟进时间的结束时间(用于条件查询)
	private String followupEndDate;
	//下次跟进时间
	private String nextFollowupDate;
	//预约上门时间
	private String appointmentDate;
	
	//下面两个用于流失客户列表
	//流失时间
	private String lostCustomerTime;
	//流失原因
	private String lostCustomerReason;
	
	//下面三个用于无效客户列表
	//无效标记时间
	private String invalidCreateTime;
	//无效审核状态
	private String invalidAuditStatus;
	//无效审核意见
	private String invalidAuditRemark;
	//加入客户创建时间，createTime ,用于前端计算总跟进天数
	private String createTime;
    //客户获取时间
	private String getCustomerTime;
	
	//下面三个字段用于 网络跟进客户管理  xiaojinwang 20170301
	//是 否  分配 营运经理
	private String isAllocate;
	//分配对象
	private String allocateTargetName;
	//分配时间
	private String allocateTime;
	//意向校区
	private String intentionCampusId;
	private String intentionCampusName;
	//客户的备注
	private String remark;
	//699191 添加学生信息
	private String studentName;
	private String schoolName;
	private String gradeId;
	
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
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
	public String getCreateCustomerUserId() {
		return createCustomerUserId;
	}
	public void setCreateCustomerUserId(String createCustomerUserId) {
		this.createCustomerUserId = createCustomerUserId;
	}
	public String getCreateUserDept() {
		return createUserDept;
	}
	public void setCreateUserDept(String createUserDept) {
		this.createUserDept = createUserDept;
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
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
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
	public String getLastFollowUpTime() {
		return lastFollowUpTime;
	}
	public void setLastFollowUpTime(String lastFollowUpTime) {
		this.lastFollowUpTime = lastFollowUpTime;
	}
	public String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getFollowupBeginDate() {
		return followupBeginDate;
	}
	public void setFollowupBeginDate(String followupBeginDate) {
		this.followupBeginDate = followupBeginDate;
	}
	public String getFollowupEndDate() {
		return followupEndDate;
	}
	public void setFollowupEndDate(String followupEndDate) {
		this.followupEndDate = followupEndDate;
	}
	public String getNextFollowupDate() {
		return nextFollowupDate;
	}
	public void setNextFollowupDate(String nextFollowupDate) {
		this.nextFollowupDate = nextFollowupDate;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getLostCustomerTime() {
		return lostCustomerTime;
	}
	public void setLostCustomerTime(String lostCustomerTime) {
		this.lostCustomerTime = lostCustomerTime;
	}
	public String getLostCustomerReason() {
		return lostCustomerReason;
	}
	public void setLostCustomerReason(String lostCustomerReason) {
		this.lostCustomerReason = lostCustomerReason;
	}
	public String getInvalidCreateTime() {
		return invalidCreateTime;
	}
	public void setInvalidCreateTime(String invalidCreateTime) {
		this.invalidCreateTime = invalidCreateTime;
	}
	public String getInvalidAuditStatus() {
		return invalidAuditStatus;
	}
	public void setInvalidAuditStatus(String invalidAuditStatus) {
		this.invalidAuditStatus = invalidAuditStatus;
	}
    
	public String getInvalidAuditRemark() {
		return invalidAuditRemark;
	}
	public void setInvalidAuditRemark(String invalidAuditRemark) {
		this.invalidAuditRemark = invalidAuditRemark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getGetCustomerTime() {
		return getCustomerTime;
	}
	public void setGetCustomerTime(String getCustomerTime) {
		this.getCustomerTime = getCustomerTime;
	}
	public String getIsAllocate() {
		return isAllocate;
	}
	public void setIsAllocate(String isAllocate) {
		this.isAllocate = isAllocate;
	}
	public String getAllocateTargetName() {
		return allocateTargetName;
	}
	public void setAllocateTargetName(String allocateTargetName) {
		this.allocateTargetName = allocateTargetName;
	}
	public String getAllocateTime() {
		return allocateTime;
	}
	public void setAllocateTime(String allocateTime) {
		this.allocateTime = allocateTime;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
		
}
