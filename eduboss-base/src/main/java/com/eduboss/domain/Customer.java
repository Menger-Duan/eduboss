package com.eduboss.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CustomerActive;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.VisitType;
import com.eduboss.dto.Response;

/**
 * Customer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer")
public class Customer extends Response implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private DataDict cusType;
	private DataDict cusOrg;
	private String contact;
	private String recordDate;//登记日期
	private User recordUserId;
	private String log;
	private String lat;   //现用作标示此客户是否已被接收 
	private String markterAssistentId;//市场专员ID
//	private String blBranch;
	private String blSchool;
	private String blConsultor;
	private String isPublic;
	private CustomerDealStatus dealStatus;
	private String remark;
	private String createTime;
	private User createUser;
	private String modifyTime;
	private String modifyUserId;
	private CustomerDeliverType deliverType;//BRENCH-分公司市场部，BRENCH_PUBLIC_POOL-分公司公共资源，CAMPUS-校区主任，CAMPUS_PUBLIC_POOL-校区公共资源，5-资询师
	private String deliverTarget;//对象deliverType＝BRENCH-分公司ID，BRENCH_PUBLIC_POOL-分公司ID，CAMPUS-校区ID，CAMPUS_PUBLIC_POOL-校区ID，SONSULTOR-咨询师ID
	private String introducer;
	private String appointmentDate;//预约日期，从预约记录表中关联查找，有多条记录时显示最后一条，没有时留空
	private User confirmReceptionist;//确认上门
	
	private String deliverTime; 
	private String lastDeliverName; // 最后分配人名称
	
	private String lastFollowUpTime; // 最后一次跟进时间
	private User lastFollowUpUser; // 最后一次跟进人
	
	private Organization blCampusId;
	//for 前端显示的字段，需要关联查询填充值
//	private boolean isDeal;//是否成交，查看此客户是否有已签的合同
	private String createUserName;//创建者名字，由createUserId关联得出
	private String deliverTargetName;//跟进对象名称，根据deliverType从不同的表根据deliverTarget查找对象名称

	private DataDict intentionOfTheCustomer; // 客户意向度分级 add by Yao  2015-04-22
	private DataDict purchasingPower;     //购买力分级 add by Yao  2015-04-22
	
	private VisitType visitType;  // 是否上门  add by tangyuping 2015-11-12
	private DataDict resEntrance; //资源入口
	private DataDict customerProject;  //项目
	
	private DataDict preEntrance;//准资源入口 add by xiaojinwang 2016-09-21

	private CustomerActive customerActive = CustomerActive.NEW_CUSTOMER;  //客户活跃程度 六个月是否跟进 默认是新客户
	
	private Set<Student> students = new HashSet<Student>();
	
	// Constructors
	
	private String nextFollowupTime;
	private String signNextFollowupTime;
	private String meetingConfirmTime;
	private String beforeDeliverTarget;//前工序跟进人
	private String beforeDeliverTime; //前工序跟进时间
	
	private String lastAppointId;//现在用作资源来源 暂存用途 xiaojinwang
	private String lastFollowId;//现用作来源细分 暂存用途   xiaojinwang
	
	private String transferFrom; //by tangyuping  前台转校来源
	private String transferStatus;  //转校接收状态
	private String receiveTime; //最新接收学生时间
	private String transferTime; //最新转出时间
	
	private String invalidReason; //设置客户无效原因
	
	//增加客户获取时间
	private String getCustomerTime;
	
	//新增需求 20170319 
	//意向校区
	private Organization intentionCampus;
	
	//新增上门时间 用于根据时间来清除客户的上门状态 xiaojinwang 20170811
	private Date visitComeDate;
	//#702956 首次分配校区 20180116
	private String firstCampus;
	
	private String isImport = "0"; //是否是导入资源 0否  1是
	
	/** default constructor */
	public Customer() {
	}

	/** minimal constructor */
	public Customer(String id) {
		this.id = id;
	}


	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name = "is_import", length = 2)
	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUS_TYPE")
	public DataDict getCusType() {
		return this.cusType;
	}

	public void setCusType(DataDict cusType) {
		this.cusType = cusType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUS_ORG")
	public DataDict getCusOrg() {
		return this.cusOrg;
	}

	public void setCusOrg(DataDict cusOrg) {
		this.cusOrg = cusOrg;
	}

	@Column(name = "CONTACT", length = 32,unique = true)
	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "RECORD_DATE", length = 20)
	public String getRecordDate() {
		return this.recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	@Column(name = "LOG", length = 16)
	public String getLog() {
		return this.log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Column(name = "LAT", length = 16)
	public String getLat() {
		return this.lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@Column(name = "MARKTER_ASSISTENT_ID", length = 32)
	public String getMarkterAssistentId() {
		return this.markterAssistentId;
	}

	public void setMarkterAssistentId(String markterAssistentId) {
		this.markterAssistentId = markterAssistentId;
	}

//	@Column(name = "BL_BRANCH", length = 32)
//	public String getBlBranch() {
//		return this.blBranch;
//	}
//
//	public void setBlBranch(String blBranch) {
//		this.blBranch = blBranch;
//	}
//
	@Column(name = "BL_SCHOOL", length = 32)
	public String getBlSchool() {
		return this.blSchool;
	}

	public void setBlSchool(String blSchool) {
		this.blSchool = blSchool;
	}

	@Column(name = "BL_CONSULTOR", length = 32)
	public String getBlConsultor() {
		return this.blConsultor;
	}

	public void setBlConsultor(String blConsultor) {
		this.blConsultor = blConsultor;
	}

	@Column(name = "IS_PUBLIC", length = 32)
	public String getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DEAL_STATUS")
	public CustomerDealStatus getDealStatus() {
		return this.dealStatus;
	}

	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	

	/**
	 * @return the deliverType
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "DELIVER_TYPE", length = 32)
	public CustomerDeliverType getDeliverType() {
		return this.deliverType;
	}

	/**
	 * @param deliverType the deliverType to set
	 */
	public void setDeliverType(CustomerDeliverType deliverType) {
		this.deliverType = deliverType;
	}

	/**
	 * @return the deliverTarget
	 */
	@Column(name = "DELEVER_TARGET", length = 32)
	public String getDeliverTarget() {
		return deliverTarget;
	}

	/**
	 * @param deliverTarget the deliverTarget to set
	 */
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}

//	/**
//	 * @return the isDeal
//	 */
//	@Transient
//	public boolean isDeal() {
//		return isDeal;
//	}
//
//	/**
//	 * @param isDeal the isDeal to set
//	 */
//	public void setDeal(boolean isDeal) {
//		this.isDeal = isDeal;
//	}

	/**
	 * @return the createUserName
	 */
	@Transient
	public String getCreateUserName() {
		return createUserName;
	}

	/**
	 * @param createUserName the createUserName to set
	 */
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	/**
	 * @return the introducer
	 */
	@Column(name = "INTRODUCER", length = 32)
	public String getIntroducer() {
		return introducer;
	}

	/**
	 * @param introducer the introducer to set
	 */
	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	/**
	 * @return the appointmentDate
	 */
	@Column(name = "APPOINTMENT_DATE", length = 32)
	public String getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @param appointmentDate the appointmentDate to set
	 */
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @return the deliverTargetName
	 */
	@Transient
	public String getDeliverTargetName() {
		return deliverTargetName;
	}

	/**
	 * @param deliverTargetName the deliverTargetName to set
	 */
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	
	@ManyToMany
	@JoinTable(name="customer_student_relation",
		joinColumns = {@JoinColumn(name = "CUSTOMER_ID")},
		inverseJoinColumns = {@JoinColumn(name = "STUDENT_ID")}
	)
	public Set<Student> getStudents() {
		return students;
	}

	public void setStudents(Set<Student> students) {
		this.students = students;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONFIRM_RECEPTIONIST_ID")
	public User getConfirmReceptionist() {
		return confirmReceptionist;
	}

	public void setConfirmReceptionist(User confirmReceptionist) {
		this.confirmReceptionist = confirmReceptionist;
	}

	@Column(name = "DELIVER_TIME", length = 20)
	public String getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	@Column(name = "LAST_DELIVER_NAME", length = 50)
	public String getLastDeliverName() {
		return lastDeliverName;
	}

	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}

	@Column(name = "LAST_FOLLOW_UP_TIME", length = 20)
	public String getLastFollowUpTime() {
		return lastFollowUpTime;
	}

	public void setLastFollowUpTime(String lastFollowUpTime) {
		this.lastFollowUpTime = lastFollowUpTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LAST_FOLLOW_UP_USER_ID")
	public User getLastFollowUpUser() {
		return lastFollowUpUser;
	}

	public void setLastFollowUpUser(User lastFollowUpUser) {
		this.lastFollowUpUser = lastFollowUpUser;
	}

	@Column(name = "NEXT_FOLLOWUP_TIME", length = 20)
	public String getNextFollowupTime() {
		return nextFollowupTime;
	}

	public void setNextFollowupTime(String nextFollowupTime) {
		this.nextFollowupTime = nextFollowupTime;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RECORD_USER_ID")
	public User getRecordUserId() {
		return recordUserId;
	}

	public void setRecordUserId(User recordUserId) {
		this.recordUserId = recordUserId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BL_CAMPUS_ID")
	public Organization getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(Organization blCampusId) {
		this.blCampusId = blCampusId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTENTION_OF_THE_CUSTOMER")
	public DataDict getIntentionOfTheCustomer() {
		return intentionOfTheCustomer;
	}

	public void setIntentionOfTheCustomer(DataDict intentionOfTheCustomer) {
		this.intentionOfTheCustomer = intentionOfTheCustomer;
	}

	@Column(name = "MEETING_CONFIRM_TIME", length = 20)
	public String getMeetingConfirmTime() {
		return meetingConfirmTime;
	}

	public void setMeetingConfirmTime(String meetingConfirmTime) {
		this.meetingConfirmTime = meetingConfirmTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PURCHASING_POWER")
	public DataDict getPurchasingPower() {
		return purchasingPower;
	}

	public void setPurchasingPower(DataDict purchasingPower) {
		this.purchasingPower = purchasingPower;
	}

	@Column(name = "SIGN_NEXT_FOLLOWUP_TIME", length = 20)
	public String getSignNextFollowupTime() {
		return signNextFollowupTime;
	}

	public void setSignNextFollowupTime(String signNextFollowupTime) {
		this.signNextFollowupTime = signNextFollowupTime;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "VISIT_TYPE", length = 32)
	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RES_ENTRANCE")
	public DataDict getResEntrance() {
		return resEntrance;
	}

	public void setResEntrance(DataDict resEntrance) {
		this.resEntrance = resEntrance;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_PROJECT")
	public DataDict getCustomerProject() {
		return customerProject;
	}

	public void setCustomerProject(DataDict customerProject) {
		this.customerProject = customerProject;
	}

	@Column(name = "BEFOR_DELIVER_TARGET", length = 32)
	public String getBeforeDeliverTarget() {
		return beforeDeliverTarget;
	}

	public void setBeforeDeliverTarget(String beforeDeliverTarget) {
		this.beforeDeliverTarget = beforeDeliverTarget;
	}

	@Column(name = "BEFOR_DELIVER_TIME", length = 20)
	public String getBeforeDeliverTime() {
		return beforeDeliverTime;
	}

	public void setBeforeDeliverTime(String beforeDeliverTime) {
		this.beforeDeliverTime = beforeDeliverTime;
	}

	@Column(name = "LAST_APPOINT_ID", length = 32)
	public String getLastAppointId() {
		return lastAppointId;
	}

	public void setLastAppointId(String lastAppointId) {
		this.lastAppointId = lastAppointId;
	}

	@Column(name = "LAST_FOLLOW_ID", length = 32)
	public String getLastFollowId() {
		return lastFollowId;
	}

	public void setLastFollowId(String lastFollowId) {
		this.lastFollowId = lastFollowId;
	}

	@Column(name = "TRANSFER_FROM", length = 32)
	public String getTransferFrom() {
		return transferFrom;
	}

	public void setTransferFrom(String transferFrom) {
		this.transferFrom = transferFrom;
	}

	@Column(name = "TRANSFER_STATUS", length = 10)
	public String getTransferStatus() {
		return transferStatus;
	}

	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	@Column(name = "invalid_reason", length = 250)
	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	@Column(name = "RECEIVE_TIME", length = 20)	
	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	@Column(name = "TRANSFER_TIME", length = 20)
	public String getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRE_ENTRANCE")
	public DataDict getPreEntrance() {
		return preEntrance;
	}

	public void setPreEntrance(DataDict preEntrance) {
		this.preEntrance = preEntrance;
	}

	@Column(name = "GET_CUSTOMER_TIME", length = 20)	
	public String getGetCustomerTime() {
		return getCustomerTime;
	}

	public void setGetCustomerTime(String getCustomerTime) {
		this.getCustomerTime = getCustomerTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTENTION_CAMPUS_ID")
	public Organization getIntentionCampus() {
		return intentionCampus;
	}

	public void setIntentionCampus(Organization intentionCampus) {
		this.intentionCampus = intentionCampus;
	}
	

	@Enumerated(EnumType.STRING)
	@Column(name = "CUSTOMER_ACTIVE", nullable = false, columnDefinition = "varchar(32) default 'NEW_CUSTOMER' ")
	public CustomerActive getCustomerActive() {
		return customerActive;
	}

	public void setCustomerActive(CustomerActive customerActive) {
		this.customerActive = customerActive;
	}

	@Column(name = "VISIT_COME_DATE")
	public Date getVisitComeDate() {
		return visitComeDate;
	}

	public void setVisitComeDate(Date visitComeDate) {
		this.visitComeDate = visitComeDate;
	}
	@Column(name = "FIRST_CAMPUS", length = 32)
	public String getFirstCampus() {
		return firstCampus;
	}

	public void setFirstCampus(String firstCampus) {
		this.firstCampus = firstCampus;
	}
	
	

}