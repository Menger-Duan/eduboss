package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eduboss.common.CustomerActive;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.VisitType;

public class CustomerVo {
	private String id;
	private String name;
	private String cusType;
	private String cusOrg;
	private String cusTypeName;
	private String cusOrgName;
	private String contact;
	private String recordDate;
	private String recordUserId;
	private String recordUserName;
	private String log;
	private String lat;
	private String markterAssistentId;
	// private String blBranch;
	private String blSchool;
	private String blSchoolName;
	private String blConsultor;
	private String isPublic;
	private CustomerDealStatus dealStatus;
	private String dealStatusName;
	private String remark;
	private String createTime;
	private String createUserId;
	private String createUserName;
	private String modifyTime;
	private String modifyUserId;
	private CustomerDeliverType deliverType;// BRENCH-分公司市场部，BRENCH_PUBLIC_POOL-分公司公共资源，CAMPUS-校区主任，CAMPUS_PUBLIC_POOL-校区公共资源，5-资询师
	private String deliverTypeName;
	private String deliverTargetName;
	private String deliverTarget;// 对象deliverType＝BRENCH-分公司ID，BRENCH_PUBLIC_POOL-分公司ID，CAMPUS-校区ID，CAMPUS_PUBLIC_POOL-校区ID，SONSULTOR-咨询师ID
	private String introducer;
	private String appointmentDate;// 预约日期，从预约记录表中关联查找，有多条记录时显示最后一条，没有时留空
	private String confirmReceptionistId;
	private String confirmReceptionistName;
	private String deliverTime;
	private String lastDeliverName; // 最后分配人名称

	private Set<StudentVo> studentVos = new HashSet<StudentVo>();
	
	private Set<StudentImportVo> studentImportVos = new HashSet<StudentImportVo>();

	private String startDate;
	private String endDate;

	private String nextFollowupTime;
	private String signNextFollowupTime;// add by Yao 2015-04-22

	private BigDecimal firstContractMoney; // 第一份合同金额

	private String blCampusId;
	private String blCampusName;

	private String firstContractTime; // 第一份合同时间 add by Yao 2015-04-13

	private String intentionOfTheCustomerId; // 客户意向度分级 add by Yao 2015-04-22
	private String intentionOfTheCustomerName; // 客户意向度分级 add by Yao 2015-04-22
	private String purchasingPowerId; // 购买力分级 add by Yao 2015-04-22
	private String purchasingPowerName; // 购买力分级 add by Yao 2015-04-22
	private String lastFollowUpTime; // 最后一次跟进时间

	private String keywork;// APP端关键字搜索

	private String schoolAddress;

	private VisitType visitType;// 是否上门 add by tangyuping 2015-11-12
	private String visitTypeName; 	
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private String projectId;   //项目
	private String projectName;
	private String deliverTargetCampus;
	
	private String recordCampusId;//登记人校区

	private String beforeDeliverTarget;//前工序跟进人
	private String beforeDeliverTime; //前工序跟进时间
	private String showDeliver;
	private String createUserDept;
	

	private String lastAppointId;
	private String lastFollowId;
	
	private String customerList; //区分客户列表和资源调配时用到的getcustomers()
	
	private String onlyShowBefore;
	
	private String isAppointCome;//预约是否到访
	
	private String transferFrom; //by tangyuping  前台转校来源
	private String transferStatus; // 0没有接收；1接收；
	
	private String isSignContract;//是否签过合同
	
	private Float totalAmount;
	private Float paidAmount;
	
	private String invalidReason; //设置客户无效原因
	
	private String brenchName; //跟进校区所属分公司
	private String brenchId;
	private Float fortyPaidAmount;//40天内实收金额
	
	private String workBench; //区分从哪个工作台添加的数据
	
	private List<CusFollowStuVo> cusfollowStudent=new ArrayList<CusFollowStuVo>();//APP 使用的客户关联学生列表
	
	private String transactionUuid;
	
	private String pointialStuSchool; //我跟进的资源里面潜在学生学校和学生年级
	private String pointialStuSchoolId;// 学生学校id 用来签合同的       //用作 客户列表中  学生就读学校的Id xiaojinwang 20160928
	private String pointialStuGrade;                         
	private String pointialStuName;                          //用作 客户列表中  学生姓名                    xiaojinwang 20160928                       
	private String cusId;
	private String deliverTargetJob; //跟进人主职位
	
	private String preEntranceId; // 准资源入口
	private String preEntranceName;
	
	//增加客户获取时间
	private String getCustomerTime;
	
	//新增需求 20170319 xiaojinwang
	private String intentionCampusId;
	private String intentionCampusName;
	//客户跟进时间的开始时间(用于条件查询)
	private String followupBeginDate;
	//客户跟进时间的结束时间(用于条件查询)
	private String followupEndDate;
	//转介绍登记 介绍人电话
	private String introducerContact;
	
	//客户录入时间的开始时间(用于条件查询)
	private String createBeginDate;
	//客户录入时间的结束时间(用于条件查询)
	private String createEndDate;
	
	private String source;//来源渠道 用于区分是录入还是导入

	private CustomerActive customerActive;//客户活跃程度 六个月是否跟进
	
	private String firstContractBeginTime;//第一份合同的签单开始时间 用于前端搜索
	private String firstContractEndTime;//第一份合同的签单结束时间 用户前端搜索

	//用于客户　前台查询的时候设置是否已经上门
	private Boolean visitCome;

	//距离上次跟进天数 20170810
	private Integer toLastFollowupDays;
	
	//新增上门时间 用于根据时间来清除客户的上门状态 xiaojinwang 20170811
	private Date visitComeDate;
	
	private Boolean fromTransfer = false;//是否是来自转介绍
	
	//新增是否超时字段，查询网络分配时间 duanmenrun 20171110
	private Integer judgeTimeOut;
	
	//#702956 首次分配校区 20180116
	private String firstCampus;
	
	private Boolean studentStatusFlag;
	
	private String haveStudent;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the cusType
	 */
	public String getCusType() {
		return cusType;
	}

	/**
	 * @param cusType
	 *            the cusType to set
	 */
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	/**
	 * @return the cusOrg
	 */
	public String getCusOrg() {
		return cusOrg;
	}

	/**
	 * @param cusOrg
	 *            the cusOrg to set
	 */
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the recordDate
	 */
	public String getRecordDate() {
		return recordDate;
	}

	/**
	 * @param recordDate
	 *            the recordDate to set
	 */
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * @return the log
	 */
	public String getLog() {
		return log;
	}

	/**
	 * @param log
	 *            the log to set
	 */
	public void setLog(String log) {
		this.log = log;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the markterAssistentId
	 */
	public String getMarkterAssistentId() {
		return markterAssistentId;
	}

	/**
	 * @param markterAssistentId
	 *            the markterAssistentId to set
	 */
	public void setMarkterAssistentId(String markterAssistentId) {
		this.markterAssistentId = markterAssistentId;
	}

	// /**
	// * @return the blBranch
	// */
	// public String getBlBranch() {
	// return blBranch;
	// }
	//
	// /**
	// * @param blBranch
	// * the blBranch to set
	// */
	// public void setBlBranch(String blBranch) {
	// this.blBranch = blBranch;
	// }

	/**
	 * @return the blSchool
	 */
	public String getBlSchool() {
		return blSchool;
	}

	/**
	 * @param blSchool
	 *            the blSchool to set
	 */
	public void setBlSchool(String blSchool) {
		this.blSchool = blSchool;
	}

	/**
	 * @return the blConsultor
	 */
	public String getBlConsultor() {
		return blConsultor;
	}

	/**
	 * @param blConsultor
	 *            the blConsultor to set
	 */
	public void setBlConsultor(String blConsultor) {
		this.blConsultor = blConsultor;
	}

	/**
	 * @return the isPublic
	 */
	public String getIsPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic
	 *            the isPublic to set
	 */
	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * @return the dealStatus
	 */
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}

	/**
	 * @param dealStatus
	 *            the dealStatus to set
	 */
	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createUserId
	 */
	public String getCreateUserId() {
		return createUserId;
	}

	/**
	 * @param createUserId
	 *            the createUserId to set
	 */
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * @return the modifyTime
	 */
	public String getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            the modifyTime to set
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the modifyUserId
	 */
	public String getModifyUserId() {
		return modifyUserId;
	}

	/**
	 * @param modifyUserId
	 *            the modifyUserId to set
	 */
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	/**
	 * @return the deliverType
	 */
	public CustomerDeliverType getDeliverType() {
		return deliverType;
	}

	/**
	 * @param deliverType
	 *            the deliverType to set
	 */
	public void setDeliverType(CustomerDeliverType deliverType) {
		this.deliverType = deliverType;
	}

	/**
	 * @return the deliverTarget
	 */
	public String getDeliverTarget() {
		return deliverTarget;
	}

	/**
	 * @param deliverTarget
	 *            the deliverTarget to set
	 */
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}

	/**
	 * @return the introducer
	 */
	public String getIntroducer() {
		return introducer;
	}

	/**
	 * @param introducer
	 *            the introducer to set
	 */
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	public Set<StudentVo> getStudentVos() {
		return studentVos;
	}

	public void setStudentVos(Set<StudentVo> studentVos) {
		this.studentVos = studentVos;
	}

	/**
	 * @return the createUserName
	 */
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName
	 *            the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
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

	public String getDeliverTargetName() {
		return deliverTargetName;
	}

	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getConfirmReceptionistId() {
		return confirmReceptionistId;
	}

	public void setConfirmReceptionistId(String confirmReceptionistId) {
		this.confirmReceptionistId = confirmReceptionistId;
	}

	public String getConfirmReceptionistName() {
		return confirmReceptionistName;
	}

	public void setConfirmReceptionistName(String confirmReceptionistName) {
		this.confirmReceptionistName = confirmReceptionistName;
	}

	public String getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getNextFollowupTime() {
		return nextFollowupTime;
	}

	public void setNextFollowupTime(String nextFollowupTime) {
		this.nextFollowupTime = nextFollowupTime;
	}

	public BigDecimal getFirstContractMoney() {
		return firstContractMoney;
	}

	public void setFirstContractMoney(BigDecimal firstContractMoney) {
		this.firstContractMoney = firstContractMoney;
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

	public String getBlSchoolName() {
		return blSchoolName;
	}

	public void setBlSchoolName(String blSchoolName) {
		this.blSchoolName = blSchoolName;
	}

	public String getFirstContractTime() {
		return firstContractTime;
	}

	public void setFirstContractTime(String firstContractTime) {
		this.firstContractTime = firstContractTime;
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

	public String getPurchasingPowerId() {
		return purchasingPowerId;
	}

	public void setPurchasingPowerId(String purchasingPowerId) {
		this.purchasingPowerId = purchasingPowerId;
	}

	public String getPurchasingPowerName() {
		return purchasingPowerName;
	}

	public void setPurchasingPowerName(String purchasingPowerName) {
		this.purchasingPowerName = purchasingPowerName;
	}

	public String getSignNextFollowupTime() {
		return signNextFollowupTime;
	}

	public void setSignNextFollowupTime(String signNextFollowupTime) {
		this.signNextFollowupTime = signNextFollowupTime;
	}

	public String getLastFollowUpTime() {
		return lastFollowUpTime;
	}

	public void setLastFollowUpTime(String lastFollowUpTime) {
		this.lastFollowUpTime = lastFollowUpTime;
	}

	public String getDeliverTypeName() {
		return deliverTypeName;
	}

	public void setDeliverTypeName(String deliverTypeName) {
		this.deliverTypeName = deliverTypeName;
	}

	public String getDealStatusName() {
		return dealStatusName;
	}

	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}

	public String getKeywork() {
		return keywork;
	}

	public void setKeywork(String keywork) {
		this.keywork = keywork;
	}

	public String getSchoolAddress() {
		return schoolAddress;
	}

	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
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

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDeliverTargetCampus() {
		return deliverTargetCampus;
	}

	public void setDeliverTargetCampus(String deliverTargetCampus) {
		this.deliverTargetCampus = deliverTargetCampus;
	}

	public String getRecordCampusId() {
		return recordCampusId;
	}

	public void setRecordCampusId(String recordCampusId) {
		this.recordCampusId = recordCampusId;
	}


	public String getLastDeliverName() {
		return lastDeliverName;
	}

	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}

	public String getBeforeDeliverTarget() {
		return beforeDeliverTarget;
	}

	public void setBeforeDeliverTarget(String beforeDeliverTarget) {
		this.beforeDeliverTarget = beforeDeliverTarget;
	}

	public String getBeforeDeliverTime() {
		return beforeDeliverTime;
	}

	public void setBeforeDeliverTime(String beforeDeliverTime) {
		this.beforeDeliverTime = beforeDeliverTime;
	}

	public String getShowDeliver() {
		return showDeliver;
	}

	public void setShowDeliver(String showDeliver) {
		this.showDeliver = showDeliver;
	}

	public String getCreateUserDept() {
		return createUserDept;
	}

	public void setCreateUserDept(String createUserDept) {
		this.createUserDept = createUserDept;
	}

	public String getLastAppointId() {
		return lastAppointId;
	}

	public void setLastAppointId(String lastAppointId) {
		this.lastAppointId = lastAppointId;
	}

	public String getLastFollowId() {
		return lastFollowId;
	}

	public void setLastFollowId(String lastFollowId) {
		this.lastFollowId = lastFollowId;
	}

	public String getCustomerList() {
		return customerList;
	}

	public void setCustomerList(String customerList) {
		this.customerList = customerList;
	}

	public String getOnlyShowBefore() {
		return onlyShowBefore;
	}

	public void setOnlyShowBefore(String onlyShowBefore) {
		this.onlyShowBefore = onlyShowBefore;
	}

	public String getIsAppointCome() {
		return isAppointCome;
	}

	public void setIsAppointCome(String isAppointCome) {
		this.isAppointCome = isAppointCome;
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

	public List<CusFollowStuVo> getCusfollowStudent() {
		return cusfollowStudent;
	}

	public void setCusfollowStudent(List<CusFollowStuVo> cusfollowStudent) {
		this.cusfollowStudent = cusfollowStudent;
	}

	public String getIsSignContract() {
		return isSignContract;
	}

	public void setIsSignContract(String isSignContract) {
		this.isSignContract = isSignContract;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Float getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Float paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public String getBrenchName() {
		return brenchName;
	}

	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
	}

	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	public Float getFortyPaidAmount() {
		return fortyPaidAmount;
	}

	public void setFortyPaidAmount(Float fortyPaidAmount) {
		this.fortyPaidAmount = fortyPaidAmount;
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public String getWorkBench() {
		return workBench;
	}

	public void setWorkBench(String workBench) {
		this.workBench = workBench;
	}

	public String getPointialStuSchool() {
		return pointialStuSchool;
	}

	public void setPointialStuSchool(String pointialStuSchool) {
		this.pointialStuSchool = pointialStuSchool;
	}

	public String getPointialStuGrade() {
		return pointialStuGrade;
	}

	public void setPointialStuGrade(String pointialStuGrade) {
		this.pointialStuGrade = pointialStuGrade;
	}

	public String getPointialStuName() {
		return pointialStuName;
	}

	public void setPointialStuName(String pointialStuName) {
		this.pointialStuName = pointialStuName;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public Set<StudentImportVo> getStudentImportVos() {
		return studentImportVos;
	}

	public void setStudentImportVos(Set<StudentImportVo> studentImportVos) {
		this.studentImportVos = studentImportVos;
	}

	public String getDeliverTargetJob() {
		return deliverTargetJob;
	}

	public void setDeliverTargetJob(String deliverTargetJob) {
		this.deliverTargetJob = deliverTargetJob;
	}


	public String getPointialStuSchoolId() {
		return pointialStuSchoolId;
	}

	public void setPointialStuSchoolId(String pointialStuSchoolId) {
		this.pointialStuSchoolId = pointialStuSchoolId;
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

	public String getGetCustomerTime() {
		return getCustomerTime;
	}

	public void setGetCustomerTime(String getCustomerTime) {
		this.getCustomerTime = getCustomerTime;
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

	public String getIntroducerContact() {
		return introducerContact;
	}

	public void setIntroducerContact(String introducerContact) {
		this.introducerContact = introducerContact;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}


	public CustomerActive getCustomerActive() {
		return customerActive;
	}

	public void setCustomerActive(CustomerActive customerActive) {
		this.customerActive = customerActive;
	}

	public String getFirstContractBeginTime() {
		return firstContractBeginTime;
	}

	public void setFirstContractBeginTime(String firstContractBeginTime) {
		this.firstContractBeginTime = firstContractBeginTime;
	}

	public String getFirstContractEndTime() {
		return firstContractEndTime;
	}

	public void setFirstContractEndTime(String firstContractEndTime) {
		this.firstContractEndTime = firstContractEndTime;
	}

	public Boolean getVisitCome() {
		return visitCome;
	}

	public void setVisitCome(Boolean visitCome) {
		this.visitCome = visitCome;
	}

	public Integer getToLastFollowupDays() {
		return toLastFollowupDays;
	}

	public void setToLastFollowupDays(Integer toLastFollowupDays) {
		this.toLastFollowupDays = toLastFollowupDays;
	}

	public Date getVisitComeDate() {
		return visitComeDate;
	}

	public void setVisitComeDate(Date visitComeDate) {
		this.visitComeDate = visitComeDate;
	}

	public Boolean getFromTransfer() {
		return fromTransfer;
	}

	public void setFromTransfer(Boolean fromTransfer) {
		this.fromTransfer = fromTransfer;
	}

	public Integer getJudgeTimeOut() {
		return judgeTimeOut;
	}

	public void setJudgeTimeOut(Integer judgeTimeOut) {
		this.judgeTimeOut = judgeTimeOut;
	}

	public String getFirstCampus() {
		return firstCampus;
	}

	public void setFirstCampus(String firstCampus) {
		this.firstCampus = firstCampus;
	}

	public Boolean getStudentStatusFlag() {
		return studentStatusFlag;
	}

	public void setStudentStatusFlag(Boolean studentStatusFlag) {
		this.studentStatusFlag = studentStatusFlag;
	}

	public String getHaveStudent() {
		return haveStudent;
	}

	public void setHaveStudent(String haveStudent) {
		this.haveStudent = haveStudent;
	}

	
	

}
