package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 资金账号变更记录表 VO用于Excel
 */
public class FundsChangeHistoryExcelVo implements java.io.Serializable {

	// Fields
	private String contractId;
	private String cusName;
	private String stuName;
	private String stuGrade;
	private String contractTypeName;
	private String signByWho;
	private String receiptTime;
	private String transactionTime;
	private Double transactionAmount;
	private String channelName;
	private String POSid;
	private String chargeByWho;
	private String blCampusName;
	private BigDecimal totalAmount = BigDecimal.ZERO;;
	private BigDecimal realAmount = BigDecimal.ZERO;;
	private BigDecimal promotionAmount = BigDecimal.ZERO;;
	private BigDecimal paidAmount = BigDecimal.ZERO;;
	private BigDecimal pendingAmount = BigDecimal.ZERO;;
	
	

	
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getStuGrade() {
		return stuGrade;
	}
	public void setStuGrade(String stuGrade) {
		this.stuGrade = stuGrade;
	}
	public String getContractTypeName() {
		return contractTypeName;
	}
	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}
	public String getSignByWho() {
		return signByWho;
	}
	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}
	public String getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(String receiptTime) {
		this.receiptTime = receiptTime;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public Double getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getPOSid() {
		if(POSid==null)
			return "";
		else
			return POSid;
	}
	public void setPOSid(String pOSid) {
		POSid = pOSid;
	}
	public String getChargeByWho() {
		return chargeByWho;
	}
	public void setChargeByWho(String chargeByWho) {
		this.chargeByWho = chargeByWho;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount==null?BigDecimal.ZERO:paidAmount;
	}
	public BigDecimal getPendingAmount() {
		return this.getRealAmount().subtract(paidAmount);
	}
	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}
	public BigDecimal getRealAmount() {
		return this.getTotalAmount().subtract(promotionAmount);
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	
	
}