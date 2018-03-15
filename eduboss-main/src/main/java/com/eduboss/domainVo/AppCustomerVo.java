package com.eduboss.domainVo;

public class AppCustomerVo {
	private String id;
	private String name;
	private String cusType; //客户来源
	private String cusOrg;  //来源细分
	private String cusTypeName; 
	private String cusOrgName;
	private String intentionOfTheCustomerId; // 客户意向度分级 
	private String intentionOfTheCustomerName; // 客户意向度分级
	private String remark; //跟进备注
	private String contact;  //联系方式
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private String deliverTime;//分配时间
	
	private String waitingTime;//待获取等待时间 天、时、秒
	
	private String dealStatus;//客户状态 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getCusOrg() {
		return cusOrg;
	}
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}
	public String getCusTypeName() {
		return cusTypeName;
	}
	public void setCusTypeName(String cusTypeName) {
		this.cusTypeName = cusTypeName;
	}
	public String getCusOrgName() {
		return cusOrgName;
	}
	public void setCusOrgName(String cusOrgName) {
		this.cusOrgName = cusOrgName;
	}
	public String getIntentionOfTheCustomerId() {
		return intentionOfTheCustomerId;
	}
	public void setIntentionOfTheCustomerId(String intentionOfTheCustomerId) {
		this.intentionOfTheCustomerId = intentionOfTheCustomerId;
	}
	public String getIntentionOfTheCustomerName() {
		return intentionOfTheCustomerName;
	}
	public void setIntentionOfTheCustomerName(String intentionOfTheCustomerName) {
		this.intentionOfTheCustomerName = intentionOfTheCustomerName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getResEntranceId() {
		return resEntranceId;
	}
	public void setResEntranceId(String resEntranceId) {
		this.resEntranceId = resEntranceId;
	}
	public String getResEntranceName() {
		return resEntranceName;
	}
	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}
	public String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}
	public String getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(String waitingTime) {
		this.waitingTime = waitingTime;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	
	
	
}
