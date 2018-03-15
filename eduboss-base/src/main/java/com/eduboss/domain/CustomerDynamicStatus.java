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


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;

/**
 * CustomerDynamicStatus entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer_dynamic_status")
public class CustomerDynamicStatus implements java.io.Serializable {

	// Fields
	private String id;
	private Customer customer;
	private CustomerEventType dynamicStatusType;
	private String occourTime;
	private String description;
	private String referUrl;
	private User referuser;
	private DataDict resEntrance; //资源入口
	private Integer statusNum;//用于统计数据 取值为+1,-1 负数表示统计的时候要减去一条记录
	
	//客户动态事件新增字段 201704020 xiaojinwang
	private String tableName;//事件对应的表名
	private String tableId;//事件对应表的记录id
	private Integer visitFlag;//是否对客户可见 1客户0不可见
	public static class VISITFLAG{
		public static int yes = 1;
		public static int no = 0;
	}
	private String belongCampus;//客户所属校区
	private CustomerDealStatus dealStatus;//客户当时处理状态
	private String deliverTarget;//客户当时的分配对象
	private String userMainJob;//操作人主职位
	private String userMainDeptBelong; //操作人主职位对应部门的统计归属
	private CustomerDeliverType deliverType;
	// Constructors

	/** default constructor */
	public CustomerDynamicStatus() {
	}

	/** minimal constructor */
	public CustomerDynamicStatus(String id) {
		this.id = id;
	}

	/** full constructor */
	public CustomerDynamicStatus(String id, Customer customer,
			CustomerEventType dynamicStatusType, String occourTime, String description,
			String referUrl, User referuser) {
		this.id = id;
		this.customer = customer;
		this.dynamicStatusType = dynamicStatusType;
		this.occourTime = occourTime;
		this.description = description;
		this.referUrl = referUrl;
		this.referuser = referuser;
	}
	
	

	/**
	 * @param customer
	 * @param dynamicStatusType
	 * @param occourTime
	 * @param description
	 * @param referUrl
	 * @param referuser
	 * @param resEntrance
	 * @param statusNum
	 * @param tableName
	 * @param tableId
	 * @param visitFlag
	 * @param belongCampus
	 * @param dealStatus
	 * @param deliverTarget
	 * @param userMainJob
	 * @param userMainDeptBelong
	 */
	public CustomerDynamicStatus(Customer customer, CustomerEventType dynamicStatusType, String occourTime,
			String description, String referUrl, User referuser, DataDict resEntrance, Integer statusNum,
			String tableName, String tableId, Integer visitFlag, String belongCampus, CustomerDealStatus dealStatus,
			String deliverTarget, String userMainJob, String userMainDeptBelong) {
		super();
		this.customer = customer;
		this.dynamicStatusType = dynamicStatusType;
		this.occourTime = occourTime;
		this.description = description;
		this.referUrl = referUrl;
		this.referuser = referuser;
		this.resEntrance = resEntrance;
		this.statusNum = statusNum;
		this.tableName = tableName;
		this.tableId = tableId;
		this.visitFlag = visitFlag;
		this.belongCampus = belongCampus;
		this.dealStatus = dealStatus;
		this.deliverTarget = deliverTarget;
		this.userMainJob = userMainJob;
		this.userMainDeptBelong = userMainDeptBelong;
	}

	// Property accessors
	@Id
//	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
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

	@Enumerated(EnumType.STRING)
	@Column(name = "DYNAMIC_STATUS_TYPE", length = 32)
	public CustomerEventType getDynamicStatusType() {
		return this.dynamicStatusType;
	}

	public void setDynamicStatusType(CustomerEventType dynamicStatusType) {
		this.dynamicStatusType = dynamicStatusType;
	}

	@Column(name = "OCCOUR_TIME", length = 20)
	public String getOccourTime() {
		return this.occourTime;
	}

	public void setOccourTime(String occourTime) {
		this.occourTime = occourTime;
	}

	@Column(name = "DESCRIPTION", length = 4096)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "REFER_URL", length = 1024)
	public String getReferUrl() {
		return this.referUrl;
	}

	public void setReferUrl(String referUrl) {
		this.referUrl = referUrl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REFERUSER_ID")
	public User getReferuser() {
		return this.referuser;
	}

	public void setReferuser(User referuser) {
		this.referuser = referuser;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RES_ENTRANCE")
	public DataDict getResEntrance() {
		return resEntrance;
	}

	public void setResEntrance(DataDict resEntrance) {
		this.resEntrance = resEntrance;
	}
	
	@Column(name = "STATUS_NUM")
	public Integer getStatusNum() {
		return statusNum;
	}
	
	public void setStatusNum(Integer statusNum) {
		this.statusNum = statusNum;
	}

	@Column(name = "TABLE_NAME", length = 32)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "TABLE_ID", length = 32)
	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	@Column(name = "VISIT_FLAG")
	public Integer getVisitFlag() {
		return visitFlag;
	}

	public void setVisitFlag(Integer visitFlag) {
		this.visitFlag = visitFlag;
	}

	@Column(name = "BELONG_CAMPUS", length = 32)
	public String getBelongCampus() {
		return belongCampus;
	}

	public void setBelongCampus(String belongCampus) {
		this.belongCampus = belongCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DEAL_STATUS", length = 32)
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}

	@Column(name = "DELIVER_TARGET", length = 32)
	public String getDeliverTarget() {
		return deliverTarget;
	}

	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}

	@Column(name = "USER_MAIN_JOB", length = 32)
	public String getUserMainJob() {
		return userMainJob;
	}

	public void setUserMainJob(String userMainJob) {
		this.userMainJob = userMainJob;
	}

	@Column(name = "USER_MAIN_DEPT_BELONG", length = 32)
	public String getUserMainDeptBelong() {
		return userMainDeptBelong;
	}

	public void setUserMainDeptBelong(String userMainDeptBelong) {
		this.userMainDeptBelong = userMainDeptBelong;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "DELIVER_TYPE", length = 32)
	public CustomerDeliverType getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(CustomerDeliverType deliverType) {
		this.deliverType = deliverType;
	}
	
	
	

}