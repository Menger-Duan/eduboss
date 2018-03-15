package com.eduboss.domainVo;

import java.math.BigDecimal;


public class YeePayInfoVo {
	
	private String id;
	private String payCode;
	private BigDecimal money;
	private String posCode;
	private String result;
	private String status;
	private String failReason;
	private String fundChargeId;
	private String createTime;
	private String createUser;
	private BigDecimal counterFee;
	private String contractId;
	private String busCode;
	private String transationTime;
	private String cardNo;
	private BigDecimal realMoney;
	private String remark;
	private String resultCode;//银联接口返回参数
	private String resultMsg;//银联接口返回参数
	private String studentName;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getPosCode() {
		return posCode;
	}
	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getFundChargeId() {
		return fundChargeId;
	}
	public void setFundChargeId(String fundChargeId) {
		this.fundChargeId = fundChargeId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getTransationTime() {
		return transationTime;
	}
	public void setTransationTime(String transationTime) {
		this.transationTime = transationTime;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public BigDecimal getRealMoney() {
		return realMoney;
	}
	public void setRealMoney(BigDecimal realMoney) {
		this.realMoney = realMoney;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public BigDecimal getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(BigDecimal counterFee) {
		this.counterFee = counterFee;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
}