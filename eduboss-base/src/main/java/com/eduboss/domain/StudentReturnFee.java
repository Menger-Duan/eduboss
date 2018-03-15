package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENT_RETURN_FEE")
public class StudentReturnFee implements java.io.Serializable {

	// Fields
	private String id;
	private Student student;//学生
	private Contract contract;//合同
	private ContractProduct contractProduct;//合同产品
	private BigDecimal returnNormalAmount;//退费金额 RETURN_NORMAL_AMOUNT RETURN_PROMOTION_AMOUNT
	private BigDecimal returnPromotionAmount;//退费金额 returnNormalAmount returnPromotionAmount
	private BigDecimal returnSpecialAmount; //超额退费
	private BigDecimal returnAmount;//退费金额
	private DataDict returnType;//退费类型
	private String returnReason;//退费原因

	private String accountName; //账户名

	private String account; // 账号

	private User createUser;//
	private String createTime;//
	private User modifyUser;//
	private String modifyTime;//
	private Organization campus;//校区
	private FundsChangeHistory fundsChangeHistory;
	
	// Constructors
	/** default constructor */
	public StudentReturnFee() {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}
	
	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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

	@Column(name = "RETURN_NORMAL_AMOUNT", precision = 10)
	public BigDecimal getReturnNormalAmount() {
		return returnNormalAmount;
	}
	public void setReturnNormalAmount(BigDecimal returnNormalAmount) {
		this.returnNormalAmount = returnNormalAmount;
	}

	@Column(name = "RETURN_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getReturnPromotionAmount() {
		return returnPromotionAmount;
	}
	public void setReturnPromotionAmount(BigDecimal returnPromotionAmount) {
		this.returnPromotionAmount = returnPromotionAmount;
	}
	
	@Column(name = "RETURN_SPECIAL_AMOUNT", precision = 10)
	public BigDecimal getReturnSpecialAmount() {
		return returnSpecialAmount;
	}
	public void setReturnSpecialAmount(BigDecimal returnSpecialAmount) {
		this.returnSpecialAmount = returnSpecialAmount;
	}

	@Column(name = "RETURN_AMOUNT", precision = 10)
	public BigDecimal getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RETURN_TYPE")
	public DataDict getReturnType() {
		return returnType;
	}

	public void setReturnType(DataDict returnType) {
		this.returnType = returnType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "RETURN_REASON", length = 200)
	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "account_name", length = 32)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "account", length = 32)
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "MODIFY_TIME", length = 32)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CAMPUS")
	public Organization getCampus() {
		return campus;
	}

	public void setCampus(Organization campus) {
		this.campus = campus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FUNDS_CHANGE_ID")
	public FundsChangeHistory getFundsChangeHistory() {
		return fundsChangeHistory;
	}

	public void setFundsChangeHistory(FundsChangeHistory fundsChangeHistory) {
		this.fundsChangeHistory = fundsChangeHistory;
	}
	
}