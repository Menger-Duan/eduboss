package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "yee_pay_info")
public class YeePayInfo implements java.io.Serializable {

	private String id;
	private BigDecimal money;
	private BigDecimal realMoney;
	private String payCode;
	private String posCode;
	private String busCode;
	private String cardNo;
	private String result;
	private String status;// 0 成功，1失败，2过期，3修改支付方式支付，4修改支付方式支付成功，5修改支付方式支付失败
	private String failReason;
	private FundsChangeHistory fundChargeId;
	private Contract contract;
	private String remark;
	private BigDecimal counterFee;//手续费
	private String transationTime;

	private String createTime;
	private String createUser;
	private String modifyTime;
	private String modifyUser;


	/** default constructor */
	public YeePayInfo() {
	}

	public YeePayInfo(String Id) {
		this.id = Id;
	}


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

	@Column(name = "PAY_CODE", length = 32)
	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	@Column(name = "MONEY", precision = 10)
	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	@Column(name = "POS_CODE", length = 32)
	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	@Column(name = "RESULT", length = 32)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "STATUS", length = 32)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "FAILD_REASON", length = 32)
	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FUND_CHARGE_ID")
	public FundsChangeHistory getFundChargeId() {
		return fundChargeId;
	}

	public void setFundChargeId(FundsChangeHistory fundChargeId) {
		this.fundChargeId = fundChargeId;
	}

	@Column(name = "CREATE_TIME", length = 32)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	
	@Column(name = "BUS_CODE", length = 50)
	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	@Column(name = "TRANSACTION_TIME", length = 32)
	public String getTransationTime() {
		return transationTime;
	}

	public void setTransationTime(String transationTime) {
		this.transationTime = transationTime;
	}

	@Column(name = "CARD_NO", length = 50)
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	@Column(name = "REAL_MONEY", precision = 10)
	public BigDecimal getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(BigDecimal realMoney) {
		this.realMoney = realMoney;
	}

	@Column(name = "REMARK", length = 500)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "COUNTER_FEE", precision = 10)
	public BigDecimal getCounterFee() {
		return counterFee;
	}

	public void setCounterFee(BigDecimal counterFee) {
		this.counterFee = counterFee;
	}

	@Column(name = "MODIFY_TIME", length = 50)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER", length = 50)
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
}