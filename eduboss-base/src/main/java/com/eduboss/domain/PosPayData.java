package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.MatchingStatus;

/**
 * 银联反馈支付数据
 * @author lixuejun
 *
 */
@Entity
@Table(name = "pos_pay_data")
public class PosPayData {

	private int id;
	private String posId; // 系统流水号（交易参考号）
	private String posTime; // 交易时间
	private BigDecimal amount; // 交易金额
	private String posNumber; // 终端编号
	private Organization blCampus;
	private String posAccount; // 交易账号
	private String merchantName; // 商户名称
	private BigDecimal poundage; // 商户手续费
	private MatchingStatus matchingStatus;
	private String remark;
	private User createUser;
	private String createTime;
	
	public PosPayData() {
		super();
	}

	public PosPayData(int id, String posId, String posTime, BigDecimal amount,
			String posNumber, String posAccount, String merchantName,
			BigDecimal poundage, Organization blCampus, User createUser, 
			String createTime) {
		super();
		this.id = id;
		this.posId = posId;
		this.posTime = posTime;
		this.amount = amount;
		this.posNumber = posNumber;
		this.posAccount = posAccount;
		this.merchantName = merchantName;
		this.poundage = poundage;
		this.blCampus = blCampus;
		this.createUser = createUser;
		this.createTime = createTime;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "POS_ID", unique = true, nullable = false, length = 32)
	public String getPosId() {
		return posId;
	}

	public void setPosId(String posId) {
		this.posId = posId;
	}

	@Column(name = "POS_TIME", length = 20)
	public String getPosTime() {
		return posTime;
	}

	public void setPosTime(String posTime) {
		this.posTime = posTime;
	}

	@Column(name = "AMOUNT", precision = 10)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "POS_NUMBER", length = 20)
	public String getPosNumber() {
		return posNumber;
	}

	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Column(name = "POS_ACCOUNT", length = 30)
	public String getPosAccount() {
		return posAccount;
	}

	public void setPosAccount(String posAccount) {
		this.posAccount = posAccount;
	}

	@Column(name = "MERCHANT_NAME", length = 50)
	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	@Column(name = "POUNDAGE", precision = 10)
	public BigDecimal getPoundage() {
		return poundage;
	}

	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MATCHING_STATUS", length = 32)
	public MatchingStatus getMatchingStatus() {
		return matchingStatus;
	}

	public void setMatchingStatus(MatchingStatus matchingStatus) {
		this.matchingStatus = matchingStatus;
	}

	@Column(name = "REMARK", length = 30)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
