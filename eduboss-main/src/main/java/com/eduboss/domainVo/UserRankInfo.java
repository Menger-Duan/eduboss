package com.eduboss.domainVo;

public class UserRankInfo {

	private String userId;
	private String orgName;
	private String orgId;
	private String amount;
	private String userName;
	private String isMe;
	private String randId; //本期排名
	private String balance; //上一名的业绩差 
	private String upDown; //与上期上升还是下降的标志
	
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
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
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
	public String getRandId() {
		return randId;
	}
	public void setRandId(String randId) {
		this.randId = randId;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUpDown() {
		return upDown;
	}
	public void setUpDown(String upDown) {
		this.upDown = upDown;
	}
	
	public UserRankInfo(String userId, String orgName, String orgId,
			String amount, String userName,String rankId) {
		this.userId = userId;
		this.orgName = orgName;
		this.orgId = orgId;
		this.amount = amount;
		this.userName = userName;
		this.randId=rankId;
		isMe="0";
		rankId="0";
		balance="0";
		upDown="0";
	}
	public UserRankInfo(String userId, String orgName, String orgId,
			String amount, String userName, String isMe, String randId,
			String balance, String upDown) {
		this.userId = userId;
		this.orgName = orgName;
		this.orgId = orgId;
		this.amount = amount;
		this.userName = userName;
		this.isMe = isMe;
		this.randId = randId;
		this.balance = balance;
		this.upDown = upDown;
	}
	public UserRankInfo() {

	}
	
}
