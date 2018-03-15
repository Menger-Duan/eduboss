package com.eduboss.domainVo;

public class InternetUseConditionVo {
	private Integer callNumber; //留电量
	private Integer notFollow; //未跟进 =分配校区主任-分配咨询师
	private Integer successNumber; //签单数
	private Float paidAmount; //合同已支付金额
	private String userName; //网络专员
	private String userId;
	private String grounp; // 集团
	private String grounpId;
	private String brench; //分公司
	private String brenchId;
	private Float totalAmount; //合同总金额
	private Integer todayCallNumber;  //当日留电量
	
	public Integer getCallNumber() {
		return callNumber;
	}
	public void setCallNumber(Integer callNumber) {
		this.callNumber = callNumber;
	}
	public Integer getNotFollow() {
		return notFollow;
	}
	public void setNotFollow(Integer notFollow) {
		this.notFollow = notFollow;
	}
	public Integer getSuccessNumber() {
		return successNumber;
	}
	public void setSuccessNumber(Integer successNumber) {
		this.successNumber = successNumber;
	}
	
	public Float getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Float paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGrounp() {
		return grounp;
	}
	public void setGrounp(String grounp) {
		this.grounp = grounp;
	}
	public String getGrounpId() {
		return grounpId;
	}
	public void setGrounpId(String grounpId) {
		this.grounpId = grounpId;
	}
	public String getBrench() {
		return brench;
	}
	public void setBrench(String brench) {
		this.brench = brench;
	}
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	public Float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Integer getTodayCallNumber() {
		return todayCallNumber;
	}
	public void setTodayCallNumber(Integer todayCallNumber) {
		this.todayCallNumber = todayCallNumber;
	}
		

}
