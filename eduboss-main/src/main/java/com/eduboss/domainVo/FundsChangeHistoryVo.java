package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.FundsChangeAuditType;
import com.eduboss.common.FundsChangeType;
import com.eduboss.common.FundsPayType;
import com.eduboss.common.PayWay;

/**
 * 资金账号变更记录表 VO
 * FundsChangeHistoryVo entity. @author MyEclipse Persistence Tools
 */
public class FundsChangeHistoryVo implements java.io.Serializable {

	// Fields
	private String id;
	private String contractId;
	private String contractTypeName;
	private String contractTypeValue;
	private String stuId;
	private String stuName;
	private String stuGrade;
	private String cusName;
	private Double transactionAmount;
	private String transactionTime;
	private String signDay;
	private String signByWho;
	private String chargeByWho;
	private String remark;
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal pendingAmount;
	private BigDecimal promotionAmount;
	
	private PayWay channel;
	private String channelName;
	private String POSid;
	private String blCampusId;
	private String blCampusName;
	
	private String auditStatusName;
	private String auditStatusValue;
	
	private String auditUserId;
	private String auditUserName;
	private String auditTime;
	
	private String isTurnPosPay;//现金转POS
	
	private String turnPosTime;//记录转换时间
	


	private String collectCardNo;//支付卡号   用于记录电子收款卡号
	private String collector;//操作人   用于记录电子汇款的收款人
	
	private String transactionUuid;
	
	private Double stuElectronicAmount; //学生电子帐户剩余金额

	private BigDecimal camNotAssignedAmount;  //待分配校区业绩

	private BigDecimal userNotAssignedAmount;  //待分配个人业绩
	
	private BigDecimal availableAmount; // 合同未分配金额
	
	private FundsPayType fundsPayType; // 付款类型
	private String receiptTime;
	private String transactionId;
	
	private String posNumber; // 终端机（pos）编号
	private String posReceiptDate; // pos机的收款时间

	private BigDecimal washSumFunds; //每条收款记录对应的冲销金额总和
	
	private FundsChangeAuditType auditType;
	private String systemAuditRemark;
	private String artificialAuditRemark;
	private String category;

	private FundsChangeType fundsChangeType;

	private String fundsChangeTypeName;

	private int pubPayContract;//是否公帐 0：否 1：是

    private String liveReceiptTime;//直播合同收款时间
    
    private String posMochineTypeId; // pos机类型

	public int getPubPayContract() {
		return pubPayContract;
	}

	public void setPubPayContract(int pubPayContract) {
		this.pubPayContract = pubPayContract;
	}


	// Constructors

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	/** default constructor */
	public FundsChangeHistoryVo() {
	}
	
     
	public FundsChangeHistoryVo(String contractId, Double transactionAmount) {
		super();
		this.contractId = contractId;
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getChargeByWho() {
		return chargeByWho;
	}

	public void setChargeByWho(String chargeByWho) {
		this.chargeByWho = chargeByWho;
	}

	public String getSignDay() {
		return signDay;
	}

	public void setSignDay(String signDay) {
		this.signDay = signDay;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getStuGrade() {
		return stuGrade;
	}

	public void setStuGrade(String stuGrade) {
		this.stuGrade = stuGrade;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getContractTypeName() {
		return contractTypeName;
	}

	public String getContractTypeValue() {
		return contractTypeValue;
	}

	public void setContractTypeValue(String contractTypeValue) {
		this.contractTypeValue = contractTypeValue;
	}

	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}

	public PayWay getChannel() {
		return channel;
	}

	public void setChannel(PayWay channel) {
		this.channel = channel;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getPOSid() {
		return POSid;
	}

	public void setPOSid(String POSid) {
		this.POSid = POSid;
	}

	public String getSignByWho() {
		return signByWho;
	}

	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}
	
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}

	public String getAuditStatusValue() {
		return auditStatusValue;
	}

	public void setAuditStatusValue(String auditStatusValue) {
		this.auditStatusValue = auditStatusValue;
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

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}

	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}

	public Double getStuElectronicAmount() {
		return stuElectronicAmount;
	}

	public void setStuElectronicAmount(Double stuElectronicAmount) {
		this.stuElectronicAmount = stuElectronicAmount;
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

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
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

	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}

	public String getPosReceiptDate() {
		return posReceiptDate;
	}

	public void setPosReceiptDate(String posReceiptDate) {
		this.posReceiptDate = posReceiptDate;
	}

	public BigDecimal getWashSumFunds() {return washSumFunds;}

	public void setWashSumFunds(BigDecimal washSumFunds) {this.washSumFunds = washSumFunds;}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public FundsChangeType getFundsChangeType() {
		return fundsChangeType;
	}

	public void setFundsChangeType(FundsChangeType fundsChangeType) {
		this.fundsChangeType = fundsChangeType;
	}

	public String getFundsChangeTypeName() {
		return fundsChangeTypeName;
	}

	public void setFundsChangeTypeName(String fundsChangeTypeName) {
		this.fundsChangeTypeName = fundsChangeTypeName;
	}

	public String getLiveReceiptTime() {
		return liveReceiptTime;
	}

	public void setLiveReceiptTime(String liveReceiptTime) {
		this.liveReceiptTime = liveReceiptTime;
	}

	/**
	 * pos机类型
	 * @return
	 */
    public String getPosMochineTypeId() {
        return posMochineTypeId;
    }

    public void setPosMochineTypeId(String posMochineTypeId) {
        this.posMochineTypeId = posMochineTypeId;
    }
	
}