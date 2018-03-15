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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.VisitType;

/**
 * CustomerFolowup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer_folowup")
public class CustomerFolowup implements java.io.Serializable {

	// Fields

	private String id;
	private Customer customer;
	private DataDict satisficing;//SATISFICING 满意度
	private String remark;
	private String createTime;
	private User createUser;
	
	private DataDict intentionOfTheCustomer; // 客户意向度分级
	private DataDict intentionSubject;   //意向科目
	private DataDict purchasingPower;     //购买力分级
	
	private String meetingTime;
	private String meetingConfirmTime;
	private User meetingConfirmUser;
	private VisitType visitType;//  add by Yao 2015-04-20
	private AppointmentType appointmentType;//  add by Yao 2015-04-22   跟进 和预约的区分
	private Organization appointCampus;
	
	private Student followStudent; //跟进学生
	
	//for前端显示
	private String customerName;
	
	
	private User agencyUser;//代理人 这里用作存储营运经理 20170220 xiaojinwang
	
	private CustomerDealStatus dealStatus;
	
	private String appointBranch;//新增 分配所属分公司 xiaojinwang 2017-02-28

	// Constructors

	/** default constructor */
	public CustomerFolowup() {
	}

	/** minimal constructor */
	public CustomerFolowup(String id) {
		this.id = id;
	}

	/** full constructor */
	public CustomerFolowup(String id, Customer customer, String remark,
			String createTime, User createUser) {
		this.id = id;
		this.customer = customer;
		this.remark = remark;
		this.createTime = createTime;
		this.createUser = createUser;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "REMARK", length = 1024)
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

	/**
	 * @return the customerName
	 */
	@Transient
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SATISFICING")
	public DataDict getSatisficing() {
		return satisficing;
	}

	public void setSatisficing(DataDict satisficing) {
		this.satisficing = satisficing;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTENTION_OF_THE_CUSTOMER")
	public DataDict getIntentionOfTheCustomer() {
		return intentionOfTheCustomer;
	}

	public void setIntentionOfTheCustomer(DataDict intentionOfTheCustomer) {
		this.intentionOfTheCustomer = intentionOfTheCustomer;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTENTION_SUBJECT")
	public DataDict getIntentionSubject() {
		return intentionSubject;
	}

	public void setIntentionSubject(DataDict intentionSubject) {
		this.intentionSubject = intentionSubject;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PURCHASING_POWER")
	public DataDict getPurchasingPower() {
		return purchasingPower;
	}

	public void setPurchasingPower(DataDict purchasingPower) {
		this.purchasingPower = purchasingPower;
	}
	
	@Column(name = "MEETING_TIME", length = 20)
	public String getMeetingTime() {
		return this.meetingTime;
	}

	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}

	@Column(name = "MEETING_CONFIRM_TIME", length = 20)
	public String getMeetingConfirmTime() {
		return this.meetingConfirmTime;
	}

	public void setMeetingConfirmTime(String meetingConfirmTime) {
		this.meetingConfirmTime = meetingConfirmTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEETING_CONFIRM_USER_ID")
	public User getMeetingConfirmUser() {
		return this.meetingConfirmUser;
	}

	public void setMeetingConfirmUser(User meetingConfirmUser) {
		this.meetingConfirmUser = meetingConfirmUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "VISIT_TYPE", length = 32)
	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "APPOINTMENT_TYPE", length = 32)
	public AppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPOINT_CAMPUS")
	public Organization getAppointCampus() {
		return appointCampus;
	}

	public void setAppointCampus(Organization appointCampus) {
		this.appointCampus = appointCampus;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FOLLOW_STUDENT_ID")
	public Student getFollowStudent() {
		return followStudent;
	}

	public void setFollowStudent(Student followStudent) {
		this.followStudent = followStudent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGENCY_USER_ID")
	public User getAgencyUser() {
		return agencyUser;
	}

	public void setAgencyUser(User agencyUser) {
		this.agencyUser = agencyUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DEAL_STATUS")
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	@Column(name = "APPOINT_BRANCH", length = 32)
	public String getAppointBranch() {
		return appointBranch;
	}

	public void setAppointBranch(String appointBranch) {
		this.appointBranch = appointBranch;
	}
	
	
}