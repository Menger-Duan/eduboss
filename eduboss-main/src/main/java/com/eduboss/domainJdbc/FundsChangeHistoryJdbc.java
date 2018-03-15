package com.eduboss.domainJdbc;

import java.math.BigDecimal;

import com.eduboss.common.ContractType;
import com.eduboss.common.FundsChangeAuditStatus;
import com.eduboss.common.FundsChangeAuditType;
import com.eduboss.common.FundsPayType;
import com.eduboss.common.PayWay;

/**
 * 
 * @author lixuejun
 * 2016-08-13
 * 
 */
public class FundsChangeHistoryJdbc {

	// Fields

	private String id;
	private String contractId;
	private ContractType contractType;
	private String customerId;
	private String customerName;
	private String studentId;
	private String studentName;
	private String studentGradeName;
	private String transactionTime;
	private BigDecimal transactionAmount;
	private String remark;
	private PayWay channel;
	private String signByWho;
	private String chargeById;
	private String chargeByName;
	private BigDecimal paidAmount;
	private String posId;
	private String certificateImage;
	private FundsChangeAuditStatus auditStatus;//审核状态
	private String auditUserId;//审批人
	private String auditUserName;
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
	
	private FundsChangeAuditType auditType;
	private String systemAuditRemark;
	private String artificialAuditRemark;
	

	// Constructors

	/** default constructor */
	public FundsChangeHistoryJdbc() {
	}

	/** full constructor */
	public FundsChangeHistoryJdbc(String id, String contractId,
			String customerId, ContractType contractType, String customerName, String studentId,
			String studentName, String studentGradeName,
			String transactionTime, BigDecimal transactionAmount,
			String remark, PayWay channel, String signByWho, String chargeById,
			String chargeByName, BigDecimal paidAmount, String posId,
			String certificateImage, FundsChangeAuditStatus auditStatus,
			String auditUserId, String auditUserName, String auditTime,
			String isTurnPosPay, String turnPosTime, String collectCardNo,
			String collector, String fundBlCampusId,
			BigDecimal camNotAssignedAmount, BigDecimal userNotAssignedAmount,
			FundsPayType fundsPayType, String receiptTime, String transactionId) {
		super();
		this.id = id;
		this.contractId = contractId;
		this.contractType = contractType;
		this.customerId = customerId;
		this.customerName = customerName;
		this.studentId = studentId;
		this.studentName = studentName;
		this.studentGradeName = studentGradeName;
		this.transactionTime = transactionTime;
		this.transactionAmount = transactionAmount;
		this.remark = remark;
		this.channel = channel;
		this.signByWho = signByWho;
		this.chargeById = chargeById;
		this.chargeByName = chargeByName;
		this.paidAmount = paidAmount;
		this.posId = posId;
		this.certificateImage = certificateImage;
		this.auditStatus = auditStatus;
		this.auditUserId = auditUserId;
		this.auditUserName = auditUserName;
		this.auditTime = auditTime;
		this.isTurnPosPay = isTurnPosPay;
		this.turnPosTime = turnPosTime;
		this.collectCardNo = collectCardNo;
		this.collector = collector;
		this.fundBlCampusId = fundBlCampusId;
		this.camNotAssignedAmount = camNotAssignedAmount;
		this.userNotAssignedAmount = userNotAssignedAmount;
		this.fundsPayType = fundsPayType;
		this.receiptTime = receiptTime;
		this.transactionId = transactionId;
	}
	
	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractId() {
		return this.contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getTransactionTime() {
		return this.transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}


	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getChargeById() {
		return chargeById;
	}

	public void setChargeById(String chargeById) {
		this.chargeById = chargeById;
	}

	public PayWay getChannel() {
		return channel;
	}

	public void setChannel(PayWay channel) {
		this.channel = channel;
	}

	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}
	
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCertificateImage() {
		return certificateImage;
	}

	public void setCertificateImage(String certificateImage) {
		this.certificateImage = certificateImage;
	}

	public FundsChangeAuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(FundsChangeAuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}
	
	public String getIsTurnPosPay() {
		return isTurnPosPay;
	}

	public void setIsTurnPosPay(String isTurnPosPay) {
		this.isTurnPosPay = isTurnPosPay;
	}

	public String getTurnPosTime() {
		return turnPosTime;
	}

	public void setTurnPosTime(String turnPosTime) {
		this.turnPosTime = turnPosTime;
	}


	public String getCollector() {
		return collector;
	}

	public void setCollector(String collector) {
		this.collector = collector;
	}

	public String getCollectCardNo() {
		return collectCardNo;
	}

	public void setCollectCardNo(String collectCardNo) {
		this.collectCardNo = collectCardNo;
	}
	
	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getFundBlCampusId() {
		return fundBlCampusId;
	}

	public void setFundBlCampusId(String fundBlCampusId) {
		this.fundBlCampusId = fundBlCampusId;
	}

	public BigDecimal getCamNotAssignedAmount() {
		return camNotAssignedAmount;
	}

	public void setCamNotAssignedAmount(BigDecimal camNotAssignedAmount) {
		this.camNotAssignedAmount = camNotAssignedAmount;
	}

	public BigDecimal getUserNotAssignedAmount() {
		return userNotAssignedAmount;
	}

	public void setUserNotAssignedAmount(BigDecimal userNotAssignedAmount) {
		this.userNotAssignedAmount = userNotAssignedAmount;
	}

	public FundsPayType getFundsPayType() {
		return fundsPayType;
	}

	public void setFundsPayType(FundsPayType fundsPayType) {
		this.fundsPayType = fundsPayType;
	}

	public String getReceiptTime() {
		return receiptTime;
	}
	
	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getChargeByName() {
		return chargeByName;
	}

	public void setChargeByName(String chargeByName) {
		this.chargeByName = chargeByName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStudentGradeName() {
		return studentGradeName;
	}

	public void setStudentGradeName(String studentGradeName) {
		this.studentGradeName = studentGradeName;
	}

	public String getSignByWho() {
		return signByWho;
	}

	public void setSignByWho(String singByWho) {
		this.signByWho = singByWho;
	}

	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	public FundsChangeAuditType getAuditType() {
		return auditType;
	}

	public void setAuditType(FundsChangeAuditType auditType) {
		this.auditType = auditType;
	}

	public String getSystemAuditRemark() {
		return systemAuditRemark;
	}

	public void setSystemAuditRemark(String systemAuditRemark) {
		this.systemAuditRemark = systemAuditRemark;
	}

	public String getArtificialAuditRemark() {
		return artificialAuditRemark;
	}

	public void setArtificialAuditRemark(String artificialAuditRemark) {
		this.artificialAuditRemark = artificialAuditRemark;
	}
	
}