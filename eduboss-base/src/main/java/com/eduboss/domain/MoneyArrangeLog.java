package com.eduboss.domain;

import java.math.BigDecimal;

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

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ArrangeType;
import com.eduboss.common.PayType;

/**
 * ElectronicAccountChangeLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MONEY_ARRANGE_LOG")
public class MoneyArrangeLog implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private ContractProduct contractProduct;
	private BigDecimal changeAmount;
	private BigDecimal afterAmount;
	private String changeTime;
	private User changeUser;
	private String remark;
	private PayType payType;
	private ArrangeType arrangeType;

	// Constructors

	/** default constructor */
	public MoneyArrangeLog() {
		changeAmount = BigDecimal.ZERO;
		afterAmount = BigDecimal.ZERO;
	}

	/** minimal constructor */
	public MoneyArrangeLog(String id) {
		this.id = id;
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
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}

	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PAY_TYPE", length = 32)
	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ARRANGE_TYPE", length = 32)
	public ArrangeType getArrangeType() {
		return arrangeType;
	}

	public void setArrangeType(ArrangeType arrangeType) {
		this.arrangeType = arrangeType;
	}
	
}