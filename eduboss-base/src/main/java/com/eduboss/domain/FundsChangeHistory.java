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

import com.eduboss.common.*;

/**
 * 资金账号变更记录表
 * FundsChangeHistory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "funds_change_history")
public class FundsChangeHistory implements java.io.Serializable {

	// Fields

	private String id;
	private Contract contract;
	private Student student;
	private String transactionTime;
	private BigDecimal transactionAmount;
	private String remark;
	private PayWay channel;
	private User chargeBy;
	private BigDecimal paidAmount;
	private String POSid;
	private String certificateImage;
	private FundsChangeAuditStatus auditStatus;//审核状态
	private User auditUser;//审批人
	private String auditTime;//审批时间 add by typ
	private String isTurnPosPay;//现金转POS
	
	private String turnPosTime;//记录转换时间
	private String collectCardNo;//支付卡号   用于记录电子收款卡号
	private String collector;//操作人   用于记录电子汇款的收款人
	private String fundBlCampusId;//合同收款校区记录

	private BigDecimal camNotAssignedAmount;  //待分配校区业绩

	private BigDecimal userNotAssignedAmount;  //待分配个人业绩
	
	private FundsPayType fundsPayType; // 付款类型
	private String receiptTime; // 收款时间
	private String transactionId; // 业务关联ID
	
	private String posNumber; // 终端机（pos）编号
	private String posReceiptDate; // pos机的收款时间
	
	private FundsChangeAuditType auditType;
	private String systemAuditRemark;
	private String artificialAuditRemark;
	private String chargeByCampusId;
	private String category;
	private FundsChangeType fundsChangeType;
	private int pubPayContract;//是否公帐 0：否 1：是
	
	private DataDict posMachineType; // pos机类型 
	

	// Constructors

	/** default constructor */
	public FundsChangeHistory() {
	}

	/** full constructor */
	public FundsChangeHistory(Student student, Contract contract, String transactionTime, BigDecimal transactionAmount, PayWay category,
			String channel, String remark) {
		this.contract = contract;
		this.transactionTime = transactionTime;
		this.transactionAmount = transactionAmount;
		this.remark = remark;
	}

	// Property accessors
	@Id
//	@GenericGenerator(name = "generator", strategy = "uuid.hex")
//	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "STUDENT_ID")
//	public Student getStudent() {
//		return this.student;
//	}
//
//	public void setStudent(Student student) {
//		this.student = student;
//	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "CUSTOMER_ID")
//	public Customer getCustomer() {
//		return customer;
//	}
//
//	public void setCustomer(Customer customer) {
//		this.customer = customer;
//	}

	@Column(name = "TRANSACTION_TIME", length = 20)
	public String getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	@Column(name = "TRANSACTION_AMOUNT", precision = 10)
	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}


	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Column(name = "PAID_AMOUNT", precision = 10)
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	
	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHARGE_USER_ID")
	public User getChargeBy() {
		return chargeBy;
	}

	public void setChargeBy(User chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CHANNEL", length = 32)
	public PayWay getChannel() {
		return channel;
	}

	public void setChannel(PayWay channel) {
		this.channel = channel;
	}

	@Column(name = "POS_ID", unique = true)
	public String getPOSid() {
		return POSid;
	}

	public void setPOSid(String pOSid) {
		POSid = pOSid;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Column(name = "CERTIFICATE_IMAGE", length = 100)
	public String getCertificateImage() {
		return certificateImage;
	}

	public void setCertificateImage(String certificateImage) {
		this.certificateImage = certificateImage;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_STATUS", length = 32)
	public FundsChangeAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(FundsChangeAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	@Column(name = "IS_TURN_POS_PAY", length = 1)
	public String getIsTurnPosPay() {
		return isTurnPosPay;
	}

	public void setIsTurnPosPay(String isTurnPosPay) {
		this.isTurnPosPay = isTurnPosPay;
	}

	@Column(name = "TURN_POS_TIME", length = 20)
	public String getTurnPosTime() {
		return turnPosTime;
	}

	public void setTurnPosTime(String turnPosTime) {
		this.turnPosTime = turnPosTime;
	}


	@Column(name = "COLLECTOR", length = 25)
	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	@Column(name = "COLLECT_CARD_NO", length = 20)
	public String getCollectCardNo() {
		return collectCardNo;
	}

	public void setCollectCardNo(String collectCardNo) {
		this.collectCardNo = collectCardNo;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUDIT_USER")
	public User getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(User auditUser) {
		this.auditUser = auditUser;
	}

	@Column(name = "AUDIT_TIME", length = 20)
	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	@Column(name = "fund_campus_id", length = 32)
	public String getFundBlCampusId() {
		return fundBlCampusId;
	}

	public void setFundBlCampusId(String fundBlCampusId) {
		this.fundBlCampusId = fundBlCampusId;
	}

	@Column(name = "cam_not_assigned_amount", precision = 10)
	public BigDecimal getCamNotAssignedAmount() {
		return camNotAssignedAmount;
	}

	public void setCamNotAssignedAmount(BigDecimal camNotAssignedAmount) {
		this.camNotAssignedAmount = camNotAssignedAmount;
	}

	@Column(name = "user_not_assigned_amount", precision = 10)
	public BigDecimal getUserNotAssignedAmount() {
		return userNotAssignedAmount;
	}

	public void setUserNotAssignedAmount(BigDecimal userNotAssignedAmount) {
		this.userNotAssignedAmount = userNotAssignedAmount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FUNDS_PAY_TYPE", length = 32)
	public FundsPayType getFundsPayType() {
		return fundsPayType;
	}

	public void setFundsPayType(FundsPayType fundsPayType) {
		this.fundsPayType = fundsPayType;
	}

	@Column(name = "RECEIPT_TIME", length = 20)
	public String getReceiptTime() {
		return receiptTime;
	}
	
	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	@Column(name = "TRANSACTION_ID", length = 50)
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Column(name = "POS_NUMBER", length = 20)
	public String getPosNumber() {
		return posNumber;
	}
	
	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	@Column(name = "POS_RECEIPT_DATE", length = 10)
	public String getPosReceiptDate() {
		return posReceiptDate;
	}

	public void setPosReceiptDate(String posReceiptDate) {
		this.posReceiptDate = posReceiptDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "AUDIT_TYPE", length = 32)
	public FundsChangeAuditType getAuditType() {
		return auditType;
	}

	public void setAuditType(FundsChangeAuditType auditType) {
		this.auditType = auditType;
	}

	@Column(name = "SYSTEM_AUDIT_REMARK", length = 50)
	public String getSystemAuditRemark() {
		return systemAuditRemark;
	}

	public void setSystemAuditRemark(String systemAuditRemark) {
		this.systemAuditRemark = systemAuditRemark;
	}

	@Column(name = "ARTIFICIAL_AUDIT_REMARK", length = 50)
	public String getArtificialAuditRemark() {
		return artificialAuditRemark;
	}

	public void setArtificialAuditRemark(String artificialAuditRemark) {
		this.artificialAuditRemark = artificialAuditRemark;
	}

	@Column(name = "CHARGE_BY_CAMPUS_ID", length = 32)
	public String getChargeByCampusId() {
		return chargeByCampusId;
	}

	public void setChargeByCampusId(String chargeByCampusId) {
		this.chargeByCampusId = chargeByCampusId;
	}

	@Column(name = "CATEGORY", length = 32)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FUNDS_CHANGE_TYPE", length = 32)
	public FundsChangeType getFundsChangeType() {
		return fundsChangeType;
	}

	public void setFundsChangeType(FundsChangeType fundsChangeType) {
		this.fundsChangeType = fundsChangeType;
	}

	@Column(name = "pub_pay_contract")
	public int getPubPayContract() {
		return pubPayContract;
	}

	public void setPubPayContract(int pubPayContract) {
		this.pubPayContract = pubPayContract;
	}
	
	/**
     * pos机类型
     * @return
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pos_machine_type")
    public DataDict getPosMachineType() {
        return posMachineType;
    }

    public void setPosMachineType(DataDict posMachineType) {
        this.posMachineType = posMachineType;
    }
    
}