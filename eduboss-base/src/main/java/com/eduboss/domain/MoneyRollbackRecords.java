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

import com.eduboss.common.ChargeType;
import com.eduboss.common.ProductType;

@Entity
@Table(name = "money_rollback_records")
public class MoneyRollbackRecords implements java.io.Serializable {

	private String id;
	private DataDict cause;
	private String detailReason;
	private String transactionId;
	private String rollbackTime;
	private User rollbackOperator;
	private String modifyTime;
	private User modifyOperator;
	private Organization blCampusId;
	private Student student;
	private ChargeType chargeType;
	private ProductType productType;
	private BigDecimal chargeHourse;
	private BigDecimal chargeAmount;
	private User originalOperateUser;
	private String transactionTime;
	
	
	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	} 
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAUSE")
	public DataDict getCause() {
		return cause;
	}
	public void setCause(DataDict cause) {
		this.cause = cause;
	}
	
	@Column(name = "DETAIL_REASON", length = 500)
	public String getDetailReason() {
		return detailReason;
	}
	public void setDetailReason(String detailReason) {
		this.detailReason = detailReason;
	}
	
	@Column(name = "TRANSACTION_ID", length = 100)
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Column(name = "ROLLBACK_TIME", length = 20)
	public String getRollbackTime() {
		return rollbackTime;
	}
	public void setRollbackTime(String rollbackDate) {
		this.rollbackTime = rollbackDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLLBACK_OPERATOR")
	public User getRollbackOperator() {
		return rollbackOperator;
	}
	public void setRollbackOperator(User rollbackOperator) {
		this.rollbackOperator = rollbackOperator;
	}
	
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyDate) {
		this.modifyTime = modifyDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_OPERATOR")
	public User getModifyOperator() {
		return modifyOperator;
	}
	public void setModifyOperator(User modifyOperator) {
		this.modifyOperator = modifyOperator;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(Organization blCampusId) {
		this.blCampusId = blCampusId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CHARGE_TYPE", length = 32)
	public ChargeType getChargeType() {
		return chargeType;
	}
	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PRODUCT_TYPE", length = 32)
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	@Column(name = "CHARGE_HOURS", precision = 10)
	public BigDecimal getChargeHourse() {
		return chargeHourse;
	}
	public void setChargeHourse(BigDecimal chargeHourse) {
		this.chargeHourse = chargeHourse;
	}
	
	@Column(name = "CHARGE_AMOUNT", precision = 10)
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}
	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORIGINAL_OPERATE_USER_ID")
	public User getOriginalOperateUser() {
		return originalOperateUser;
	}
	public void setOriginalOperateUser(User originalOperateUser) {
		this.originalOperateUser = originalOperateUser;
	}
	
	@Column(name = "TRANSACTION_TIME", length = 20)
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	
}
