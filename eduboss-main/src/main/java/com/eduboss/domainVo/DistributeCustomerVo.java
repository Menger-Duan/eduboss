package com.eduboss.domainVo;


/**
 * @description 分配客户管理列表 VO
 * @date 2016-08-16
 * @author xiaojinwang
 *
 */
public class DistributeCustomerVo {
 
	//客户姓名
	private String cusName;
	//客户ID
	private String cusId;
	//资源入口Id
	private String resEntranceId;
    //资源入口名称
	private String resEntranceName;
	//资源来源
	private String cusType;	
	//最后分配人名称
	private String lastDeliverName; 
    //最后分配时间
	private String deliverTime;
	//意向度Id
	private String intentionOfTheCustomerId;
	//意向度Name
	private String intentionOfTheCustomerName;
	//学生姓名
	private String studentName;
	//学生姓名列表(用于显示)
	private String studentNameList;
	//就读学校id
	private String schoolId;
	//就读学校名称
	private String schoolName;
	//学校名字列表(用于显示)
	private String schoolNameList;
	//跟进记录
	private String followupRemark;
	//备注信息
	private String remark;
	//客户分配开始时间
	private String deliverBeginDate;
	//客户分配结束时间
	private String deliverEndDate;
	
	//跟进状态
	private String dealStatus;
	private String dealStatusName;
	//分配类型
	private String deliverType;
	//跟进人
	private String deliverTarget;
	
	//转校状态  非空则是转校 0 是没接收 1是已接收  
	private String transferStatus;
	
	//增加筛选条件  学生年级Id
	private String gradeId;
	//学生年级名字 
	private String gradeName;
	//学生年级列表
	private String gradeNameList;
	
	//客户是否已经被分配
	private String isDistribute;
	
	//联系方式
	private String contact;
	
	//返回前端记录的id
	private String customerFollowupId;
	
	//分配校区名字
	private String campusName;
	
	//客户资源调配 
	private Boolean releaseable;
	
	//意向校区
	private String intentionCampusId;
	private String intentionCampusName;
	//新增需求 20170321
	//客户跟进时间的开始时间(用于条件查询)
	private String followupBeginDate;
	//客户跟进时间的结束时间(用于条件查询)
	private String followupEndDate;
	// 最后一次跟进时间
	private String lastFollowUpTime; 
	//所属校区id
	private String blCampusId;
	//所属校区名字
	private String blCampusName;
	
	
	
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
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}
	public String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
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
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNameList() {
		return studentNameList;
	}
	public void setStudentNameList(String studentNameList) {
		this.studentNameList = studentNameList;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolNameList() {
		return schoolNameList;
	}
	public void setSchoolNameList(String schoolNameList) {
		this.schoolNameList = schoolNameList;
	}
	public String getFollowupRemark() {
		return followupRemark;
	}
	public void setFollowupRemark(String followupRemark) {
		this.followupRemark = followupRemark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeliverBeginDate() {
		return deliverBeginDate;
	}
	public void setDeliverBeginDate(String deliverBeginDate) {
		this.deliverBeginDate = deliverBeginDate;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	public String getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getIsDistribute() {
		return isDistribute;
	}
	public void setIsDistribute(String isDistribute) {
		this.isDistribute = isDistribute;
	}
	public String getGradeNameList() {
		return gradeNameList;
	}
	public void setGradeNameList(String gradeNameList) {
		this.gradeNameList = gradeNameList;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}
	public String getCustomerFollowupId() {
		return customerFollowupId;
	}
	public void setCustomerFollowupId(String customerFollowupId) {
		this.customerFollowupId = customerFollowupId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public Boolean getReleaseable() {
		return releaseable;
	}
	public void setReleaseable(Boolean releaseable) {
		this.releaseable = releaseable;
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
	public String getLastFollowUpTime() {
		return lastFollowUpTime;
	}
	public void setLastFollowUpTime(String lastFollowUpTime) {
		this.lastFollowUpTime = lastFollowUpTime;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	
	
	
}
