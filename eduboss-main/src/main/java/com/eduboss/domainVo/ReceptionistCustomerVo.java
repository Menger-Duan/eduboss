package com.eduboss.domainVo;

import com.eduboss.common.CustomerDeliverType;

public class ReceptionistCustomerVo {
	
	private String id;
	private String name;
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private String cusType;  //资源来源
	private String cusOrg;   //来源细分
	private String cusTypeName;
	private String cusOrgName;
	private String contact;
	private String recordDate;
	private String recordUserId;
	private String recordUserName;
	private String recordUserDept;
	private String recordUserDeptName;
	private String dealStatus;
	private String dealStatusName;
	private String remark;
	private CustomerDeliverType deliverType;// 跟进对象类型，公共资源池，或校区资源池
	private String deliverTypeName;
	private String deliverTarget;   //跟进对象
	private String deliverTargetName; 
	private String transferFrom; //by tangyuping  前台转校来源
	private String transferStatus; // 0没有接收；1接收；
	private String blSchool;
	
	//跟进部分
	private String meetingConfirmTime; //登记日期
	private String meetingConfirmUserId; //登记人
	private String meetingConfirmUserName;
	private String meetingConfirmBlCampus;
	
	private String startDate;
	private String endDate;
	
	private String visitType; //标记客户是否上门
	private String receiveTime; //接收学生时间
	private String transferTime; //转出时间
	private String receiveOrTransfer; //区分接收列表还是转出列表
	private String deliverTime;
	private String lastDeliverName; // 最后分配人名称
	private String scusSatisficing; //客户意向度
	
	private String preEntranceId; // 准资源入口
	private String preEntranceName;
	
	private String customerId;//客户Id
	private String transferRecordId;//转校记录Id
	
	private String brenchId;//分公司的id
	private String branchName;//分公司
	
	private String receiveCampus;
	
	private Boolean isVisitCome;
	
	private String studyManagerNames;//用于前台查询客户的显示学生和对应的学管师
	private String studentNames;

	private String customerActive;
	
	//增加字段 对不活跃的客户更加的细分   xiaojinwang  20170517 
	private Boolean localSchoolCustomer;//是否是本校客户  true是本校客户  false非本校客户
	private Boolean following;//是否六个月有跟进  true 是六个月有跟进 false 六个月内没有跟进
	private Boolean courseConsume;//是否六个月有课消  true 是六个月有课消 false 六个月内没有课消
	private Boolean remainAmount;//是否有剩余资金
	
	// #699233   前台查询客户 判断学生结课和停课状态 duanmenrun
	private Boolean isFinishClass;//是否结课 15天以上
	private Boolean isStopClass;//是否停课  90天以上
	private Boolean isEnrolled;//是否有潜在学生
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getCusTypeName() {
		return cusTypeName;
	}
	public void setCusTypeName(String cusTypeName) {
		this.cusTypeName = cusTypeName;
	}
	public String getCusOrgName() {
		return cusOrgName;
	}
	public void setCusOrgName(String cusOrgName) {
		this.cusOrgName = cusOrgName;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getRecordUserId() {
		return recordUserId;
	}
	public void setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
	}
	public String getRecordUserName() {
		return recordUserName;
	}
	public void setRecordUserName(String recordUserName) {
		this.recordUserName = recordUserName;
	}
	public String getRecordUserDept() {
		return recordUserDept;
	}
	public void setRecordUserDept(String recordUserDept) {
		this.recordUserDept = recordUserDept;
	}
	public String getRecordUserDeptName() {
		return recordUserDeptName;
	}
	public void setRecordUserDeptName(String recordUserDeptName) {
		this.recordUserDeptName = recordUserDeptName;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getTransferFrom() {
		return transferFrom;
	}
	public void setTransferFrom(String transferFrom) {
		this.transferFrom = transferFrom;
	}
	public String getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}
	public String getMeetingConfirmTime() {
		return meetingConfirmTime;
	}
	public void setMeetingConfirmTime(String meetingConfirmTime) {
		this.meetingConfirmTime = meetingConfirmTime;
	}
	public String getMeetingConfirmUserId() {
		return meetingConfirmUserId;
	}
	public void setMeetingConfirmUserId(String meetingConfirmUserId) {
		this.meetingConfirmUserId = meetingConfirmUserId;
	}
	public String getMeetingConfirmUserName() {
		return meetingConfirmUserName;
	}
	public void setMeetingConfirmUserName(String meetingConfirmUserName) {
		this.meetingConfirmUserName = meetingConfirmUserName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMeetingConfirmBlCampus() {
		return meetingConfirmBlCampus;
	}
	public void setMeetingConfirmBlCampus(String meetingConfirmBlCampus) {
		this.meetingConfirmBlCampus = meetingConfirmBlCampus;
	}
	public String getBlSchool() {
		return blSchool;
	}
	public void setBlSchool(String blSchool) {
		this.blSchool = blSchool;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	public String getReceiveOrTransfer() {
		return receiveOrTransfer;
	}
	public void setReceiveOrTransfer(String receiveOrTransfer) {
		this.receiveOrTransfer = receiveOrTransfer;
	}
	public String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}
	public String getScusSatisficing() {
		return scusSatisficing;
	}
	public void setScusSatisficing(String scusSatisficing) {
		this.scusSatisficing = scusSatisficing;
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
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getTransferRecordId() {
		return transferRecordId;
	}
	public void setTransferRecordId(String transferRecordId) {
		this.transferRecordId = transferRecordId;
	}
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getReceiveCampus() {
		return receiveCampus;
	}
	public void setReceiveCampus(String receiveCampus) {
		this.receiveCampus = receiveCampus;
	}
	public Boolean getIsVisitCome() {
		return isVisitCome;
	}
	public void setIsVisitCome(Boolean isVisitCome) {
		this.isVisitCome = isVisitCome;
	}
	public String getStudyManagerNames() {
		return studyManagerNames;
	}
	public void setStudyManagerNames(String studyManagerNames) {
		this.studyManagerNames = studyManagerNames;
	}
	public String getStudentNames() {
		return studentNames;
	}
	public void setStudentNames(String studentNames) {
		this.studentNames = studentNames;
	}

	public String getCustomerActive() {
		return customerActive;
	}

	public void setCustomerActive(String customerActive) {
		this.customerActive = customerActive;
	}
	public Boolean getLocalSchoolCustomer() {
		return localSchoolCustomer;
	}
	public void setLocalSchoolCustomer(Boolean localSchoolCustomer) {
		this.localSchoolCustomer = localSchoolCustomer;
	}
	public Boolean getFollowing() {
		return following;
	}
	public void setFollowing(Boolean following) {
		this.following = following;
	}
	public Boolean getCourseConsume() {
		return courseConsume;
	}
	public void setCourseConsume(Boolean courseConsume) {
		this.courseConsume = courseConsume;
	}
	public Boolean getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(Boolean remainAmount) {
		this.remainAmount = remainAmount;
	}
	public Boolean getIsFinishClass() {
		return isFinishClass;
	}
	public void setIsFinishClass(Boolean isFinishClass) {
		this.isFinishClass = isFinishClass;
	}
	public Boolean getIsStopClass() {
		return isStopClass;
	}
	public void setIsStopClass(Boolean isStopClass) {
		this.isStopClass = isStopClass;
	}
	public Boolean getIsEnrolled() {
		return isEnrolled;
	}
	public void setIsEnrolled(Boolean isEnrolled) {
		this.isEnrolled = isEnrolled;
	}
	
}

