package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.BonusType;
import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.common.ProductType;

@Entity
@Table(name = "ods_month_payment_receipt")
public class OdsMonthPaymentReciept implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private Organization group;
	private Organization brench;
	private Organization campus;
	private Organization transactionCampus;
	private String student;
	private String school;
	private String grade;
	private ContractType contractType;
	private String contractId;
	private String contractCreateTime;
	private ContractStatus contractStatus;
	private BigDecimal unDistributionAmount;
	private BigDecimal transactionAmount;
	private String transactionTime;
	private BigDecimal unDistributionBonus;
	private BigDecimal bonusAmount;
	private BonusType bonusType;
	private User bonusStaff;
	private Organization bonusStaffOrg;
	private String receiptMonth;
	private String receiptDate;
	private EvidenceAuditStatus receiptStatus;
	private ContractPaidStatus contractPaidStatus;
	private String fundsChangeId;
	private ProductType productType;
	
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Group_id")
	public Organization getGroup() {
		return group;
	}
	public void setGroup(Organization group) {
		this.group = group;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="Branch_id")
	public Organization getBrench() {
		return brench;
	}
	public void setBrench(Organization brench) {
		this.brench = brench;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="campus_id")
	public Organization getCampus() {
		return campus;
	}
	public void setCampus(Organization campus) {
		this.campus = campus;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="transaction_campus")
	public Organization getTransactionCampus() {
		return transactionCampus;
	}
	public void setTransactionCampus(Organization transactionCampus) {
		this.transactionCampus = transactionCampus;
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "student_id", length = 32)
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "school_id", length = 32)
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "grade_id", length = 32)
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "contract_type", length = 32)
	public ContractType getContractType() {
		return contractType;
	}
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	@Column(name = "contract_id")
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	@Column(name = "CONTRACT_CREATE_TIME")
	public String getContractCreateTime() {
		return contractCreateTime;
	}
	public void setContractCreateTime(String contractCreateTime) {
		this.contractCreateTime = contractCreateTime;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "contract_status", length = 32)
	public ContractStatus getContractStatus() {
		return contractStatus;
	}
	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}
	@Column(name = "UN_DISTRIBUTION_AMOUNT")
	public BigDecimal getUnDistributionAmount() {
		return unDistributionAmount;
	}
	public void setUnDistributionAmount(BigDecimal unDistributionAmount) {
		this.unDistributionAmount = unDistributionAmount;
	}
	@Column(name = "TRANSACTION_AMOUNT")
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	@Column(name = "TRANSACTION_TIME")
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	@Column(name = "UN_DISTRIBUTION_BONUS")
	public BigDecimal getUnDistributionBonus() {
		return unDistributionBonus;
	}
	public void setUnDistributionBonus(BigDecimal unDistributionBonus) {
		this.unDistributionBonus = unDistributionBonus;
	}
	@Column(name = "BONUS_AMOUNT")
	public BigDecimal getBonusAmount() {
		return bonusAmount;
	}
	public void setBonusAmount(BigDecimal bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "bonus_type", length = 32)
	public BonusType getBonusType() {
		return bonusType;
	}
	public void setBonusType(BonusType bonusType) {
		this.bonusType = bonusType;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bonus_staff_id")
	public User getBonusStaff() {
		return bonusStaff;
	}
	public void setBonusStaff(User bonusStaff) {
		this.bonusStaff = bonusStaff;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="bonus_dept_id")
	public Organization getBonusStaffOrg() {
		return bonusStaffOrg;
	}
	public void setBonusStaffOrg(Organization bonusStaffOrg) {
		this.bonusStaffOrg = bonusStaffOrg;
	}
	@Column(name = "RECEIPT_MONTH")
	public String getReceiptMonth() {
		return receiptMonth;
	}
	public void setReceiptMonth(String receiptMonth) {
		this.receiptMonth = receiptMonth;
	}
	@Column(name = "RECEIPT_DATE")
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "contract_paid_status", length = 32)
	public ContractPaidStatus getContractPaidStatus() {
		return contractPaidStatus;
	}
	public void setContractPaidStatus(ContractPaidStatus contractPaidStatus) {
		this.contractPaidStatus = contractPaidStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "RECEIPT_STATUS")
	public EvidenceAuditStatus getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(EvidenceAuditStatus receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	
	
	@Column(name = "funds_change_id")
	public String getFundsChangeId() {
		return fundsChangeId;
	}
	public void setFundsChangeId(String fundsChangeId) {
		this.fundsChangeId = fundsChangeId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "product_type")
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	
	
	
}
