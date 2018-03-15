package com.eduboss.domainVo;

import java.math.BigDecimal;

public class UserAmountRankInfo {

	private String userId;
	private String orgName;
	private String orgId;
	private BigDecimal amount;
	private String userName;
	private String isMe="0";
	private int randId; //本期排名
	private BigDecimal balance; //上一名的业绩差 
	private int upDown; //与上期上升还是下降的标志
	private String sex="1";//性别
	private BigDecimal onlineAmount;
	private BigDecimal lineAmount;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIsMe() {
		return isMe;
	}
	public void setIsMe(String isMe) {
		this.isMe = isMe;
	}
	
	public UserAmountRankInfo() {

	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getRandId() {
		return randId;
	}
	public void setRandId(int randId) {
		this.randId = randId;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public int getUpDown() {
		return upDown;
	}
	public void setUpDown(int upDown) {
		this.upDown = upDown;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public BigDecimal getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(BigDecimal onlineAmount) {
		this.onlineAmount = onlineAmount;
	}

	public BigDecimal getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(BigDecimal lineAmount) {
		this.lineAmount = lineAmount;
	}
}
