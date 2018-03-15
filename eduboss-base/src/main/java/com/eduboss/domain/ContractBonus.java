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

import com.eduboss.common.BonusType;
import com.eduboss.common.ProductType;
import com.eduboss.common.PromotionType;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CONTRACT_BONUS")
public class ContractBonus implements java.io.Serializable {

	// Fields
	private String id;
	//private Contract contract;
	private User bonusStaff;
	private BigDecimal bonusAmount;//提成人金额
	private BigDecimal campusAmount;//校区业绩，业绩分配时选择的校区，第一次默认是合同校区
	private FundsChangeHistory fundsChangeHistory;
	private Organization organization;
	private String createTime;
	private StudentReturnFee studentReturnFee;//退费记录
	private BonusType bonusType;//业绩类型
	
	private Organization bonusStaffCampus;//业绩提成人校区
	private Organization contractCampus;//业绩来源校区，合同校区
	
	private ProductType type;
	
	// Constructors
	/** default constructor */
	public ContractBonus() {
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

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CONTRACT_ID")
//	public Contract getContract() {
//		return this.contract;
//	}
//
//	public void setContract(Contract contract) {
//		this.contract = contract;
//	}
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BONUS_STAFF_ID")
	public User getBonusStaff() {
		return bonusStaff;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FUNDS_CHANGE_ID")
	public FundsChangeHistory getFundsChangeHistory() {
		return fundsChangeHistory;
	}

	public void setFundsChangeHistory(FundsChangeHistory fundsChangeHistory) {
		this.fundsChangeHistory = fundsChangeHistory;
	}

	public void setBonusStaff(User bonusStaff) {
		this.bonusStaff = bonusStaff;
	}
	
	@Column(name = "BONUS_AMOUNT", precision = 10)
	public BigDecimal getBonusAmount() {
		return bonusAmount;
	}

	public void setBonusAmount(BigDecimal bonusAmount) {
		this.bonusAmount = bonusAmount;
	}
	
	@Column(name = "CAMPUS_AMOUNT", precision = 10)
	public BigDecimal getCampusAmount() {
		return campusAmount;
	}

	public void setCampusAmount(BigDecimal campusAmount) {
		this.campusAmount = campusAmount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATIONID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_RETURN_ID")
	public StudentReturnFee getStudentReturnFee() {
		return studentReturnFee;
	}
	
	public void setStudentReturnFee(StudentReturnFee studentReturnFee) {
		this.studentReturnFee = studentReturnFee;
	}
	

	@Enumerated(EnumType.STRING)
	@Column(name = "BONUS_TYPE", length = 32)
	public BonusType getBonusType() {
		return bonusType;
	}

	public void setBonusType(BonusType bonusType) {
		this.bonusType = bonusType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PRODUCT_TYPE", length = 32)
	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BONUS_STAFF_CAMPUS")
	public Organization getBonusStaffCampus() {
		return bonusStaffCampus;
	}

	public void setBonusStaffCampus(Organization bonusStaffCampus) {
		this.bonusStaffCampus = bonusStaffCampus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_CAMPUS")
	public Organization getContractCampus() {
		return contractCampus;
	}

	public void setContractCampus(Organization contractCampus) {
		this.contractCampus = contractCampus;
	}
	
	
	
}