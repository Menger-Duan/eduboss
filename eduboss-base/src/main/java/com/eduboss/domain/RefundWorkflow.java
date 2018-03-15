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

import com.eduboss.common.BonusDistributeType;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.RefundAuditStatus;
import com.eduboss.common.RefundFormType;

/**
 * 
 * @author lixuejun 2016-06-13
 *
 */
@Entity
@Table(name = "refund_workflow")
public class RefundWorkflow {

	private String id;
	private RefundFormType formType; // 表单类型
	private Organization refundCampus; // 退费校区
	private User applicant; // 申请人
	private Organization applicantCampus; // 申请人部门
	private DataDict refundCause; // 退费原因
	private String refundDetailReason; // 退费详细原因
	private Student refundStudent; // 退费学生
	private ContractProduct refundContractProduct; // 退费合同产品
	private Organization intoCampus; // 转入校区
	private Student intoStudent; // 转入学生
	private String refundAccount; // 退费账号
	private String refundAccName; // 退费账号名
	private String bankInfo; // 银行支行信息
	private BigDecimal basicOperateAmount; // 基本账户操作金额
	private BigDecimal promotionOperateAmount; // 赠送账户操作金额
	private BigDecimal specialOperateAmount; // 超额退费金额

	private Organization firstRefundDutyCampus; // 退费责任校区1
	private BigDecimal firstRefundDutyAmountCampus; // 退费责任校区金额1
	private BonusDistributeType firstSubBonusDistributeTypeCampus;//校区分配类型1

	private Organization secondRefundDutyCampus; // 退费责任校区2
	private BigDecimal secondRefundDutyAmountCampus; // 退费责任校区金额2
	private BonusDistributeType secondSubBonusDistributeTypeCampus;//校区分配类型2

	private Organization thirdRefundDutyCampus; // 退费责任校区3
	private BigDecimal thirdRefundDutyAmountCampus; // 退费责任校区金额3
	private BonusDistributeType thirdSubBonusDistributeTypeCampus;//校区分配类型3

	private Organization fourthRefundDutyCampus; // 退费责任校区4
	private BigDecimal fourthRefundDutyAmountCampus; // 退费责任校区金额4
	private BonusDistributeType fourthSubBonusDistributeTypeCampus;//校区分配类型4

	private Organization fifthRefundDutyCampus; // 退费责任校区5
	private BigDecimal fifthRefundDutyAmountCampus; // 退费责任校区金额5
	private BonusDistributeType fifthSubBonusDistributeTypeCampus;//校区分配类型5

	private String firstRefundDutyPerson; // 退费责任人1
	private BigDecimal firstRefundDutyAmountPerson; // 退费责任人金额1
	private BonusDistributeType firstSubBonusDistributeTypePerson; //责任人分配类型1

	private String secondRefundDutyPerson; // 退费责任人2
	private BigDecimal secondRefundDutyAmountPerson; // 退费责任人金额2
	private BonusDistributeType secondSubBonusDistributeTypePerson; //责任人分配类型2

	private String thirdRefundDutyPerson; // 退费责任人3
	private BigDecimal thirdRefundDutyAmountPerson; // 退费责任人金额3
	private BonusDistributeType thirdSubBonusDistributeTypePerson; //责任人分配类型3

	private String fourthRefundDutyPerson; // 退费责任人4
	private BigDecimal fourthRefundDutyAmountPerson; // 退费责任人金额4
	private BonusDistributeType fourthSubBonusDistributeTypePerson; //责任人分配类型4

	private String fifthRefundDutyPerson; // 退费责任人5
	private BigDecimal fifthRefundDutyAmountPerson; // 退费责任人金额5
	private BonusDistributeType fifthSubBonusDistributeTypePerson; //责任人分配类型5

	private RefundAuditStatus auditStatus; // 审批状态
	private User auditUser; // 审批人
	private Organization auditCampus; // 审批人部门
	private long flowId; // 工作流ID
	private String refundTitle; // 标题
	private String initiateTime; // 发起时间
	private String acceptTime; // 接收时间
	private int stepId;  // 当前步骤id
	
	private BigDecimal cpBasicAmount;
	private BigDecimal cpBasicMaxAmount;
	private BigDecimal cpPromotionAmount;
	private BigDecimal cpPromotionMaxAmount;
	
	private BigDecimal accRemainAmount;
	private BigDecimal accMaxAmount;
	
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	public RefundWorkflow() {
		super();
	}

	public RefundWorkflow(String id, RefundFormType formType, Organization refundCampus, User applicant, Organization applicantCampus, DataDict refundCause, String refundDetailReason, Student refundStudent, ContractProduct refundContractProduct, Organization intoCampus, Student intoStudent, String refundAccount, String refundAccName, String bankInfo, BigDecimal basicOperateAmount, BigDecimal promotionOperateAmount, BigDecimal specialOperateAmount, Organization firstRefundDutyCampus, BigDecimal firstRefundDutyAmountCampus, BonusDistributeType firstSubBonusDistributeTypeCampus, Organization secondRefundDutyCampus, BigDecimal secondRefundDutyAmountCampus, BonusDistributeType secondSubBonusDistributeTypeCampus, Organization thirdRefundDutyCampus, BigDecimal thirdRefundDutyAmountCampus, BonusDistributeType thirdSubBonusDistributeTypeCampus, Organization fourthRefundDutyCampus, BigDecimal fourthRefundDutyAmountCampus, BonusDistributeType fourthSubBonusDistributeTypeCampus, Organization fifthRefundDutyCampus, BigDecimal fifthRefundDutyAmountCampus, BonusDistributeType fifthSubBonusDistributeTypeCampus, String firstRefundDutyPerson, BigDecimal firstRefundDutyAmountPerson, BonusDistributeType firstSubBonusDistributeTypePerson, String secondRefundDutyPerson, BigDecimal secondRefundDutyAmountPerson, BonusDistributeType secondSubBonusDistributeTypePerson, String thirdRefundDutyPerson, BigDecimal thirdRefundDutyAmountPerson, BonusDistributeType thirdSubBonusDistributeTypePerson, String fourthRefundDutyPerson, BigDecimal fourthRefundDutyAmountPerson, BonusDistributeType fourthSubBonusDistributeTypePerson, String fifthRefundDutyPerson, BigDecimal fifthRefundDutyAmountPerson, BonusDistributeType fifthSubBonusDistributeTypePerson, RefundAuditStatus auditStatus, User auditUser, Organization auditCampus, long flowId, String refundTitle, String initiateTime, String acceptTime, int stepId, BigDecimal cpBasicAmount, BigDecimal cpBasicMaxAmount, BigDecimal cpPromotionAmount, BigDecimal cpPromotionMaxAmount, BigDecimal accRemainAmount, BigDecimal accMaxAmount, String createUserId, String createTime, String modifyUserId, String modifyTime) {
		this.id = id;
		this.formType = formType;
		this.refundCampus = refundCampus;
		this.applicant = applicant;
		this.applicantCampus = applicantCampus;
		this.refundCause = refundCause;
		this.refundDetailReason = refundDetailReason;
		this.refundStudent = refundStudent;
		this.refundContractProduct = refundContractProduct;
		this.intoCampus = intoCampus;
		this.intoStudent = intoStudent;
		this.refundAccount = refundAccount;
		this.refundAccName = refundAccName;
		this.bankInfo = bankInfo;
		this.basicOperateAmount = basicOperateAmount;
		this.promotionOperateAmount = promotionOperateAmount;
		this.specialOperateAmount = specialOperateAmount;
		this.firstRefundDutyCampus = firstRefundDutyCampus;
		this.firstRefundDutyAmountCampus = firstRefundDutyAmountCampus;
		this.firstSubBonusDistributeTypeCampus = firstSubBonusDistributeTypeCampus;
		this.secondRefundDutyCampus = secondRefundDutyCampus;
		this.secondRefundDutyAmountCampus = secondRefundDutyAmountCampus;
		this.secondSubBonusDistributeTypeCampus = secondSubBonusDistributeTypeCampus;
		this.thirdRefundDutyCampus = thirdRefundDutyCampus;
		this.thirdRefundDutyAmountCampus = thirdRefundDutyAmountCampus;
		this.thirdSubBonusDistributeTypeCampus = thirdSubBonusDistributeTypeCampus;
		this.fourthRefundDutyCampus = fourthRefundDutyCampus;
		this.fourthRefundDutyAmountCampus = fourthRefundDutyAmountCampus;
		this.fourthSubBonusDistributeTypeCampus = fourthSubBonusDistributeTypeCampus;
		this.fifthRefundDutyCampus = fifthRefundDutyCampus;
		this.fifthRefundDutyAmountCampus = fifthRefundDutyAmountCampus;
		this.fifthSubBonusDistributeTypeCampus = fifthSubBonusDistributeTypeCampus;
		this.firstRefundDutyPerson = firstRefundDutyPerson;
		this.firstRefundDutyAmountPerson = firstRefundDutyAmountPerson;
		this.firstSubBonusDistributeTypePerson = firstSubBonusDistributeTypePerson;
		this.secondRefundDutyPerson = secondRefundDutyPerson;
		this.secondRefundDutyAmountPerson = secondRefundDutyAmountPerson;
		this.secondSubBonusDistributeTypePerson = secondSubBonusDistributeTypePerson;
		this.thirdRefundDutyPerson = thirdRefundDutyPerson;
		this.thirdRefundDutyAmountPerson = thirdRefundDutyAmountPerson;
		this.thirdSubBonusDistributeTypePerson = thirdSubBonusDistributeTypePerson;
		this.fourthRefundDutyPerson = fourthRefundDutyPerson;
		this.fourthRefundDutyAmountPerson = fourthRefundDutyAmountPerson;
		this.fourthSubBonusDistributeTypePerson = fourthSubBonusDistributeTypePerson;
		this.fifthRefundDutyPerson = fifthRefundDutyPerson;
		this.fifthRefundDutyAmountPerson = fifthRefundDutyAmountPerson;
		this.fifthSubBonusDistributeTypePerson = fifthSubBonusDistributeTypePerson;
		this.auditStatus = auditStatus;
		this.auditUser = auditUser;
		this.auditCampus = auditCampus;
		this.flowId = flowId;
		this.refundTitle = refundTitle;
		this.initiateTime = initiateTime;
		this.acceptTime = acceptTime;
		this.stepId = stepId;
		this.cpBasicAmount = cpBasicAmount;
		this.cpBasicMaxAmount = cpBasicMaxAmount;
		this.cpPromotionAmount = cpPromotionAmount;
		this.cpPromotionMaxAmount = cpPromotionMaxAmount;
		this.accRemainAmount = accRemainAmount;
		this.accMaxAmount = accMaxAmount;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "FROM_TYPE", length = 32)
	public RefundFormType getFormType() {
		return formType;
	}

	public void setFormType(RefundFormType formType) {
		this.formType = formType;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_CAMPUS_ID")
	public Organization getRefundCampus() {
		return refundCampus;
	}

	public void setRefundCampus(Organization refundCampus) {
		this.refundCampus = refundCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPLICANT")
	public User getApplicant() {
		return applicant;
	}

	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="APPLICANT_CAMPUS")
	public Organization getApplicantCampus() {
		return applicantCampus;
	}
	
	public void setApplicantCampus(Organization applicantCampus) {
		this.applicantCampus = applicantCampus;
	}
	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_CAUSE")
	public DataDict getRefundCause() {
		return refundCause;
	}


	public void setRefundCause(DataDict refundCause) {
		this.refundCause = refundCause;
	}

	@Column(name = "REFUND_DETAIL_REASON", length = 250)
	public String getRefundDetailReason() {
		return refundDetailReason;
	}

	public void setRefundDetailReason(String refundDetailReason) {
		this.refundDetailReason = refundDetailReason;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_STUDENT")
	public Student getRefundStudent() {
		return refundStudent;
	}

	public void setRefundStudent(Student refundStudent) {
		this.refundStudent = refundStudent;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="REFUND_CONTRACT_PRODUCT")
	public ContractProduct getRefundContractProduct() {
		return refundContractProduct;
	}

	public void setRefundContractProduct(ContractProduct refundContractProduct) {
		this.refundContractProduct = refundContractProduct;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTO_CAMPUS")
	public Organization getIntoCampus() {
		return intoCampus;
	}

	public void setIntoCampus(Organization intoCampus) {
		this.intoCampus = intoCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="INTO_STUDENT")
	public Student getIntoStudent() {
		return intoStudent;
	}

	public void setIntoStudent(Student intoStudent) {
		this.intoStudent = intoStudent;
	}

	@Column(name = "REFUND_ACCOUNT", length = 32)
	public String getRefundAccount() {
		return refundAccount;
	}

	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	@Column(name = "REFUND_ACC_NAME", length = 32)
	public String getRefundAccName() {
		return refundAccName;
	}

	public void setRefundAccName(String refundAccName) {
		this.refundAccName = refundAccName;
	}

	@Column(name = "BANK_INFO", length = 35)
	public String getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}

	@Column(name = "BASIC_OPERATE_AMOUNT", precision = 10)
	public BigDecimal getBasicOperateAmount() {
		return basicOperateAmount;
	}

	public void setBasicOperateAmount(BigDecimal basicOperateAmount) {
		this.basicOperateAmount = basicOperateAmount;
	}

	@Column(name = "PROMOTION_OPERATE_AMOUNT", precision = 10)
	public BigDecimal getPromotionOperateAmount() {
		return promotionOperateAmount;
	}

	public void setPromotionOperateAmount(BigDecimal promotionOperateAmount) {
		this.promotionOperateAmount = promotionOperateAmount;
	}
	
	@Column(name = "SPECIAL_OPERATE_AMOUNT", precision = 10)
	public BigDecimal getSpecialOperateAmount() {
		return specialOperateAmount;
	}

	public void setSpecialOperateAmount(BigDecimal specialOperateAmount) {
		this.specialOperateAmount = specialOperateAmount;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FIRST_REFUND_DUTY_CAMPUS")
	public Organization getFirstRefundDutyCampus() {
		return firstRefundDutyCampus;
	}

	public void setFirstRefundDutyCampus(Organization firstRefundDutyCampus) {
		this.firstRefundDutyCampus = firstRefundDutyCampus;
	}

	@Column(name = "FIRST_REFUND_DUTY_AMOUNT_CAMPUS", precision = 10)
	public BigDecimal getFirstRefundDutyAmountCampus() {
		return firstRefundDutyAmountCampus;
	}

	public void setFirstRefundDutyAmountCampus(BigDecimal firstRefundDutyAmountCampus) {
		this.firstRefundDutyAmountCampus = firstRefundDutyAmountCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FIRST_BONUS_TYPE_CAMPUS", length = 32)
	public BonusDistributeType getFirstSubBonusDistributeTypeCampus() {
		return firstSubBonusDistributeTypeCampus;
	}

	public void setFirstSubBonusDistributeTypeCampus(BonusDistributeType firstSubBonusDistributeTypeCampus) {
		this.firstSubBonusDistributeTypeCampus = firstSubBonusDistributeTypeCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SECOND_REFUND_DUTY_CAMPUS")
	public Organization getSecondRefundDutyCampus() {
		return secondRefundDutyCampus;
	}

	public void setSecondRefundDutyCampus(Organization secondRefundDutyCampus) {
		this.secondRefundDutyCampus = secondRefundDutyCampus;
	}

	@Column(name = "SECOND_REFUND_DUTY_AMOUNT_CAMPUS", precision = 10)
	public BigDecimal getSecondRefundDutyAmountCampus() {
		return secondRefundDutyAmountCampus;
	}

	public void setSecondRefundDutyAmountCampus(BigDecimal secondRefundDutyAmountCampus) {
		this.secondRefundDutyAmountCampus = secondRefundDutyAmountCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SECOND_BONUS_TYPE_CAMPUS", length = 32)
	public BonusDistributeType getSecondSubBonusDistributeTypeCampus() {
		return secondSubBonusDistributeTypeCampus;
	}

	public void setSecondSubBonusDistributeTypeCampus(BonusDistributeType secondSubBonusDistributeTypeCampus) {
		this.secondSubBonusDistributeTypeCampus = secondSubBonusDistributeTypeCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="THIRD_REFUND_DUTY_CAMPUS")
	public Organization getThirdRefundDutyCampus() {
		return thirdRefundDutyCampus;
	}

	public void setThirdRefundDutyCampus(Organization thirdRefundDutyCampus) {
		this.thirdRefundDutyCampus = thirdRefundDutyCampus;
	}

	@Column(name = "THIRD_REFUND_DUTY_AMOUNT_CAMPUS", precision = 10)
	public BigDecimal getThirdRefundDutyAmountCampus() {
		return thirdRefundDutyAmountCampus;
	}

	public void setThirdRefundDutyAmountCampus(BigDecimal thirdRefundDutyAmountCampus) {
		this.thirdRefundDutyAmountCampus = thirdRefundDutyAmountCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "THIRD_BONUS_TYPE_CAMPUS", length = 32)
	public BonusDistributeType getThirdSubBonusDistributeTypeCampus() {
		return thirdSubBonusDistributeTypeCampus;
	}

	public void setThirdSubBonusDistributeTypeCampus(BonusDistributeType thirdSubBonusDistributeTypeCampus) {
		this.thirdSubBonusDistributeTypeCampus = thirdSubBonusDistributeTypeCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FOURTH_REFUND_DUTY_CAMPUS")
	public Organization getFourthRefundDutyCampus() {
		return fourthRefundDutyCampus;
	}

	public void setFourthRefundDutyCampus(Organization fourthRefundDutyCampus) {
		this.fourthRefundDutyCampus = fourthRefundDutyCampus;
	}

	@Column(name = "FOURTH_REFUND_DUTY_AMOUNT_CAMPUS", precision = 10)
	public BigDecimal getFourthRefundDutyAmountCampus() {
		return fourthRefundDutyAmountCampus;
	}

	public void setFourthRefundDutyAmountCampus(BigDecimal fourthRefundDutyAmountCampus) {
		this.fourthRefundDutyAmountCampus = fourthRefundDutyAmountCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FOURTH_BONUS_TYPE_CAMPUS", length = 32)
	public BonusDistributeType getFourthSubBonusDistributeTypeCampus() {
		return fourthSubBonusDistributeTypeCampus;
	}

	public void setFourthSubBonusDistributeTypeCampus(BonusDistributeType fourthSubBonusDistributeTypeCampus) {
		this.fourthSubBonusDistributeTypeCampus = fourthSubBonusDistributeTypeCampus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FIFTH_REFUND_DUTY_CAMPUS")
	public Organization getFifthRefundDutyCampus() {
		return fifthRefundDutyCampus;
	}

	public void setFifthRefundDutyCampus(Organization fifthRefundDutyCampus) {
		this.fifthRefundDutyCampus = fifthRefundDutyCampus;
	}

	@Column(name = "FIFTH_REFUND_DUTY_AMOUNT_CAMPUS", precision = 10)
	public BigDecimal getFifthRefundDutyAmountCampus() {
		return fifthRefundDutyAmountCampus;
	}

	public void setFifthRefundDutyAmountCampus(BigDecimal fifthRefundDutyAmountCampus) {
		this.fifthRefundDutyAmountCampus = fifthRefundDutyAmountCampus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FIFTH_BONUS_TYPE_CAMPUS", length = 32)
	public BonusDistributeType getFifthSubBonusDistributeTypeCampus() {
		return fifthSubBonusDistributeTypeCampus;
	}

	public void setFifthSubBonusDistributeTypeCampus(BonusDistributeType fifthSubBonusDistributeTypeCampus) {
		this.fifthSubBonusDistributeTypeCampus = fifthSubBonusDistributeTypeCampus;
	}

	@Column(name = "FIRST_REFUND_DUTY_PERSON")
	public String getFirstRefundDutyPerson() {
		return firstRefundDutyPerson;
	}

	public void setFirstRefundDutyPerson(String firstRefundDutyPerson) {
		this.firstRefundDutyPerson = firstRefundDutyPerson;
	}

	@Column(name = "FIRST_REFUND_DUTY_AMOUNT_PERSON", precision = 10)
	public BigDecimal getFirstRefundDutyAmountPerson() {
		return firstRefundDutyAmountPerson;
	}

	public void setFirstRefundDutyAmountPerson(BigDecimal firstRefundDutyAmountPerson) {
		this.firstRefundDutyAmountPerson = firstRefundDutyAmountPerson;
	}


	@Enumerated(EnumType.STRING)
	@Column(name = "FIRST_BONUS_TYPE_PERSON", length = 32)
	public BonusDistributeType getFirstSubBonusDistributeTypePerson() {
		return firstSubBonusDistributeTypePerson;
	}

	public void setFirstSubBonusDistributeTypePerson(BonusDistributeType firstSubBonusDistributeTypePerson) {
		this.firstSubBonusDistributeTypePerson = firstSubBonusDistributeTypePerson;
	}

	@Column(name = "SECOND_REFUND_DUTY_PERSON")
	public String getSecondRefundDutyPerson() {
		return secondRefundDutyPerson;
	}

	public void setSecondRefundDutyPerson(String secondRefundDutyPerson) {
		this.secondRefundDutyPerson = secondRefundDutyPerson;
	}

	@Column(name = "SECOND_REFUND_DUTY_AMOUNT_PERSON", precision = 10)
	public BigDecimal getSecondRefundDutyAmountPerson() {
		return secondRefundDutyAmountPerson;
	}

	public void setSecondRefundDutyAmountPerson(BigDecimal secondRefundDutyAmountPerson) {
		this.secondRefundDutyAmountPerson = secondRefundDutyAmountPerson;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SECOND_BONUS_TYPE_PERSON", length = 32)
	public BonusDistributeType getSecondSubBonusDistributeTypePerson() {
		return secondSubBonusDistributeTypePerson;
	}

	public void setSecondSubBonusDistributeTypePerson(BonusDistributeType secondSubBonusDistributeTypePerson) {
		this.secondSubBonusDistributeTypePerson = secondSubBonusDistributeTypePerson;
	}

	@Column(name = "THIRD_REFUND_DUTY_PERSON")
	public String getThirdRefundDutyPerson() {
		return thirdRefundDutyPerson;
	}

	public void setThirdRefundDutyPerson(String thirdRefundDutyPerson) {
		this.thirdRefundDutyPerson = thirdRefundDutyPerson;
	}

	@Column(name = "THIRD_REFUND_DUTY_AMOUNT_PERSON", precision = 10)
	public BigDecimal getThirdRefundDutyAmountPerson() {
		return thirdRefundDutyAmountPerson;
	}

	public void setThirdRefundDutyAmountPerson(BigDecimal thirdRefundDutyAmountPerson) {
		this.thirdRefundDutyAmountPerson = thirdRefundDutyAmountPerson;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "THIRD_BONUS_TYPE_PERSON", length = 32)
	public BonusDistributeType getThirdSubBonusDistributeTypePerson() {
		return thirdSubBonusDistributeTypePerson;
	}

	public void setThirdSubBonusDistributeTypePerson(BonusDistributeType thirdSubBonusDistributeTypePerson) {
		this.thirdSubBonusDistributeTypePerson = thirdSubBonusDistributeTypePerson;
	}

	@Column(name = "FOURTH_REFUND_DUTY_PERSON")
	public String getFourthRefundDutyPerson() {
		return fourthRefundDutyPerson;
	}

	public void setFourthRefundDutyPerson(String fourthRefundDutyPerson) {
		this.fourthRefundDutyPerson = fourthRefundDutyPerson;
	}

	@Column(name = "FOURTH_REFUND_DUTY_AMOUNT_PERSON", precision = 10)
	public BigDecimal getFourthRefundDutyAmountPerson() {
		return fourthRefundDutyAmountPerson;
	}

	public void setFourthRefundDutyAmountPerson(BigDecimal fourthRefundDutyAmountPerson) {
		this.fourthRefundDutyAmountPerson = fourthRefundDutyAmountPerson;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FOURTH_BONUS_TYPE_PERSON", length = 32)
	public BonusDistributeType getFourthSubBonusDistributeTypePerson() {
		return fourthSubBonusDistributeTypePerson;
	}

	public void setFourthSubBonusDistributeTypePerson(BonusDistributeType fourthSubBonusDistributeTypePerson) {
		this.fourthSubBonusDistributeTypePerson = fourthSubBonusDistributeTypePerson;
	}

	@Column(name = "FIFTH_REFUND_DUTY_PERSON")
	public String getFifthRefundDutyPerson() {
		return fifthRefundDutyPerson;
	}

	public void setFifthRefundDutyPerson(String fifthRefundDutyPerson) {
		this.fifthRefundDutyPerson = fifthRefundDutyPerson;
	}

	@Column(name = "FIFTH_REFUND_DUTY_AMOUNT_PERSON", precision = 10)
	public BigDecimal getFifthRefundDutyAmountPerson() {
		return fifthRefundDutyAmountPerson;
	}

	public void setFifthRefundDutyAmountPerson(BigDecimal fifthRefundDutyAmountPerson) {
		this.fifthRefundDutyAmountPerson = fifthRefundDutyAmountPerson;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FIFTH_BONUS_TYPE_PERSON", length = 32)
	public BonusDistributeType getFifthSubBonusDistributeTypePerson() {
		return fifthSubBonusDistributeTypePerson;
	}

	public void setFifthSubBonusDistributeTypePerson(BonusDistributeType fifthSubBonusDistributeTypePerson) {
		this.fifthSubBonusDistributeTypePerson = fifthSubBonusDistributeTypePerson;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public RefundAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(RefundAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUDIT_USER")
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AUDIT_CAMPUS")
	public Organization getAuditCampus() {
		return auditCampus;
	}

	public void setAuditCampus(Organization auditCampus) {
		this.auditCampus = auditCampus;
	}

	@Column(name = "FLOW_ID", length=20)
	public long getFlowId() {
		return flowId;
	}

	public void setFlowId(long flowId) {
		this.flowId = flowId;
	}
	
	@Column(name="REFUND_TITLE", length=100)
	public String getRefundTitle() {
		return refundTitle;
	}

	public void setRefundTitle(String refundTitle) {
		this.refundTitle = refundTitle;
	}
	
	@Column(name="INITIATE_TIME", length= 20)
	public String getInitiateTime() {
		return initiateTime;
	}

	public void setInitiateTime(String initiateTime) {
		this.initiateTime = initiateTime;
	}
	
	@Column(name="ACCEPT_TIME", length= 20)
	public String getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	
	@Column(name="STEP_ID", length= 2)
	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
	}

	@Column(name = "CP_BASIC_AMOUNT", precision = 10)
	public BigDecimal getCpBasicAmount() {
		return cpBasicAmount;
	}

	public void setCpBasicAmount(BigDecimal cpBasicAmount) {
		this.cpBasicAmount = cpBasicAmount;
	}

	@Column(name = "CP_BASIC_MAX_AMOUNT", precision = 10)
	public BigDecimal getCpBasicMaxAmount() {
		return cpBasicMaxAmount;
	}

	public void setCpBasicMaxAmount(BigDecimal cpBasicMaxAmount) {
		this.cpBasicMaxAmount = cpBasicMaxAmount;
	}

	@Column(name = "CP_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getCpPromotionAmount() {
		return cpPromotionAmount;
	}

	public void setCpPromotionAmount(BigDecimal cpPromotionAmount) {
		this.cpPromotionAmount = cpPromotionAmount;
	}

	@Column(name = "CP_PROMOTION_MAX_AMOUNT", precision = 10)
	public BigDecimal getCpPromotionMaxAmount() {
		return cpPromotionMaxAmount;
	}

	public void setCpPromotionMaxAmount(BigDecimal cpPromotionMaxAmount) {
		this.cpPromotionMaxAmount = cpPromotionMaxAmount;
	}

	@Column(name = "ACC_REMAINAMOUNT", precision = 10)
	public BigDecimal getAccRemainAmount() {
		return accRemainAmount;
	}

	public void setAccRemainAmount(BigDecimal accRemainAmount) {
		this.accRemainAmount = accRemainAmount;
	}

	@Column(name = "ACC_MAX_AMOUNT", precision = 10)
	public BigDecimal getAccMaxAmount() {
		return accMaxAmount;
	}

	public void setAccMaxAmount(BigDecimal accMaxAmount) {
		this.accMaxAmount = accMaxAmount;
	}

	@Column(name = "CREATE_USER_ID", length=32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length=20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_USER_ID", length=32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_TIME", length=20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
