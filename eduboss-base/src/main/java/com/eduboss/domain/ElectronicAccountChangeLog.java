package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ElectronicAccChangeType;

import javax.persistence.*;

import java.math.BigDecimal;

/**
 * ElectronicAccountChangeLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ELECTRONIC_ACCOUNT_CHANGE_LOG")
public class ElectronicAccountChangeLog implements java.io.Serializable {

	// Fields

	private String id;
	private StudnetAccMv studentAccMv;
	private ElectronicAccChangeType type;
	private String changeType;
	private BigDecimal changeAmount;
	private BigDecimal afterAmount;
	private String changeTime;
	private User changeUser;
	private String remark;

	// Constructors

	/** default constructor */
	public ElectronicAccountChangeLog() {
	}

	/** minimal constructor */
	public ElectronicAccountChangeLog(String id) {
		this.id = id;
	}

	/** full constructor */
	public ElectronicAccountChangeLog(String id, StudnetAccMv studentAccMv, ElectronicAccChangeType type,
			String changeType, BigDecimal changeAmount, BigDecimal afterAmount,
			String changeTime, User changeUser, String remark) {
		this.id = id;
		this.studentAccMv = studentAccMv;
		this.type = type;
		this.changeType = changeType;
		this.changeAmount = changeAmount;
		this.afterAmount = afterAmount;
		this.changeTime = changeTime;
		this.changeUser = changeUser;
		this.remark = remark;
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

	@Column(name = "CHANGE_TYPE", length = 1)
	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	@Column(name = "CHANGE_AMOUNT", precision = 10)
	public BigDecimal getChangeAmount() {
		return this.changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	@Column(name = "AFTER_AMOUNT", precision = 10)
	public BigDecimal getAfterAmount() {
		return this.afterAmount;
	}

	public void setAfterAmount(BigDecimal afterAmount) {
		this.afterAmount = afterAmount;
	}

	@Column(name = "CHANGE_TIME", length = 20)
	public String getChangeTime() {
		return this.changeTime;
	}

	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHANGE_USER_ID")
	public User getChangeUser() {
		return this.changeUser;
	}

	public void setChangeUser(User changeUser) {
		this.changeUser = changeUser;
	}

	@Column(name = "REMARK", length = 20)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_MV_ID", insertable=true, updatable=false)
	public StudnetAccMv getStudentAccMv() {
		return studentAccMv;
	}

	public void setStudentAccMv(StudnetAccMv studentAccMv) {
		this.studentAccMv = studentAccMv;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", length = 32)
	public ElectronicAccChangeType getType() {
		return type;
	}

	public void setType(ElectronicAccChangeType type) {
		this.type = type;
	}
	
}