package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAFF_BONUS_DAY")
public class StaffBonusDay implements java.io.Serializable {

	// Fields

	private int id;
	private String countDate;
	private String userId;
	private String userName;
	private String orgId;
	private String orgName;
	private BigDecimal amount;
	private int rank;
	private BigDecimal balance;
	private int upDown;
	private String createTime;
	private String modifyTime;
	private int brenchRank;
	private BigDecimal brenchBalance;
	private int brenchUpDown;
	private String brenchId;
	private String brenchName;
	private BigDecimal lineAmount;
	private BigDecimal onlineAmount;

	// Constructors

	/** default constructor */
	public StaffBonusDay() {
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "COUNT_DATE", length = 10)
	public String getCountDate() {
		return countDate;
	}


	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}


	@Column(name = "USER_NAME", length = 100)
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "ORG_ID", length = 32)
	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Column(name = "ORG_NAME", length = 100)
	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "RANK")
	public int getRank() {
		return rank;
	}


	public void setRank(int rank) {
		this.rank = rank;
	}

	@Column(name = "BALANCE")
	public BigDecimal getBalance() {
		return balance;
	}


	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Column(name = "UP_DOWN")
	public int getUpDown() {
		return upDown;
	}


	public void setUpDown(int upDown) {
		this.upDown = upDown;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "BRENCH_RANK")
	public int getBrenchRank() {
		return brenchRank;
	}

	public void setBrenchRank(int brenchRank) {
		this.brenchRank = brenchRank;
	}

	@Column(name = "BRENCH_BALANCE")
	public BigDecimal getBrenchBalance() {
		return brenchBalance;
	}

	public void setBrenchBalance(BigDecimal brenchBalance) {
		this.brenchBalance = brenchBalance;
	}

	@Column(name = "BRENCH_UP_DOWN")
	public int getBrenchUpDown() {
		return brenchUpDown;
	}

	public void setBrenchUpDown(int brenchUpDown) {
		this.brenchUpDown = brenchUpDown;
	}

	@Column(name = "BRENCH_ID", length = 32)
	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	@Column(name = "BRENCH_NAME", length = 32)
	public String getBrenchName() {
		return brenchName;
	}

	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
	}

	@Column(name = "LINE_AMOUNT")
	public BigDecimal getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(BigDecimal lineAmount) {
		this.lineAmount = lineAmount;
	}

	@Column(name = "ONLINE_AMOUNT")
	public BigDecimal getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(BigDecimal onlineAmount) {
		this.onlineAmount = onlineAmount;
	}
	
}
