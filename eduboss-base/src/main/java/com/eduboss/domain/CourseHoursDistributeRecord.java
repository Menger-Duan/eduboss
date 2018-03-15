package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.DistributeType;

/**
 * 
 * @author lixuejun
 * 2016-11-23
 *
 */
@Entity
@Table(name = "course_hours_distribute_record")
public class CourseHoursDistributeRecord implements java.io.Serializable {

	private static final long serialVersionUID = 3988233807214759890L;
	
	// Fields
	private int id;
	private String contractProductId;
	private DataDict subject;
	private DistributeType type;
	private BigDecimal transactionHours;
	private BigDecimal distributedHours;
	private BigDecimal consumeHours;
	private BigDecimal remainHours;
	private User createUser;
	private String createTime;
	private Organization blCampus;//校区
	
	public CourseHoursDistributeRecord() {
		super();
	}

	public CourseHoursDistributeRecord(String contractProductId,
									   DataDict subject, DistributeType type, BigDecimal transactionHours,
									   BigDecimal distributedHours, BigDecimal consumeHours,
									   BigDecimal remainHours, User createUser, String createTime) {
		super();
		this.contractProductId = contractProductId;
		this.subject = subject;
		this.type = type;
		this.transactionHours = transactionHours;
		this.distributedHours = distributedHours;
		this.consumeHours = consumeHours;
		this.remainHours = remainHours;
		this.createUser = createUser;
		this.createTime = createTime;
	}
	
	public CourseHoursDistributeRecord(String contractProductId,
			DataDict subject, DistributeType type, BigDecimal transactionHours,
			BigDecimal distributedHours, BigDecimal consumeHours,
			BigDecimal remainHours, User createUser, String createTime, Organization blCampus) {
		super();
		this.contractProductId = contractProductId;
		this.subject = subject;
		this.type = type;
		this.transactionHours = transactionHours;
		this.distributedHours = distributedHours;
		this.consumeHours = consumeHours;
		this.remainHours = remainHours;
		this.createUser = createUser;
		this.createTime = createTime;
		this.blCampus = blCampus;
	}


	public CourseHoursDistributeRecord(int id, String contractProductId,
									   DataDict subject, DistributeType type, BigDecimal transactionHours,
									   BigDecimal distributedHours, BigDecimal consumeHours,
									   BigDecimal remainHours, User createUser, String createTime) {
		super();
		this.id = id;
		this.contractProductId = contractProductId;
		this.subject = subject;
		this.type = type;
		this.transactionHours = transactionHours;
		this.distributedHours = distributedHours;
		this.consumeHours = consumeHours;
		this.remainHours = remainHours;
		this.createUser = createUser;
		this.createTime = createTime;
	}


	public CourseHoursDistributeRecord(int id, String contractProductId,
			DataDict subject, DistributeType type, BigDecimal transactionHours,
			BigDecimal distributedHours, BigDecimal consumeHours,
			BigDecimal remainHours, User createUser, String createTime, Organization blCampus) {
		super();
		this.id = id;
		this.contractProductId = contractProductId;
		this.subject = subject;
		this.type = type;
		this.transactionHours = transactionHours;
		this.distributedHours = distributedHours;
		this.consumeHours = consumeHours;
		this.remainHours = remainHours;
		this.createUser = createUser;
		this.createTime = createTime;
		this.blCampus = blCampus;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "CONTRACT_PRODUCT_ID", length=32)
	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", length = 32)
	public DistributeType getType() {
		return type;
	}

	public void setType(DistributeType type) {
		this.type = type;
	}

	@Column(name = "TRANSACTION_HOURS", precision=10)
	public BigDecimal getTransactionHours() {
		return transactionHours;
	}

	public void setTransactionHours(BigDecimal transactionHours) {
		this.transactionHours = transactionHours;
	}

	@Column(name = "DISTRIBUTED_HOURS", precision=10)
	public BigDecimal getDistributedHours() {
		return distributedHours;
	}

	public void setDistributedHours(BigDecimal distributedHours) {
		this.distributedHours = distributedHours;
	}

	@Column(name = "CONSUME_HOURS", precision=10)
	public BigDecimal getConsumeHours() {
		return consumeHours;
	}

	public void setConsumeHours(BigDecimal consumeHours) {
		this.consumeHours = consumeHours;
	}

	@Column(name = "REMAIN_HOURS", precision=10)
	public BigDecimal getRemainHours() {
		return remainHours;
	}

	public void setRemainHours(BigDecimal remainHours) {
		this.remainHours = remainHours;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length=20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}
}