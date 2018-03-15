package com.eduboss.domainVo.customer;


/**
 * 客户资源池列表VO
 * 适用于客户资源池列表
 * @author Yao
 */
public class CustomerResourceVo {
	
	private String id;//客户id
	private String name;//客户名字
	private String resEntranceId; // 资源入口
	private String resEntranceName;//资源入口名字
	private String lastDeliverName; // 最后分配人名称
	private String deliverTime;//最后分配时间
	private String intentionOfTheCustomerId; // 客户意向度
	private String intentionOfTheCustomerName; // 客户意向度
	private String pointialStuName;//学生名字，多个学生用逗号隔开
	private String pointialStuSchool; //我跟进的资源里面潜在学生学校和学生年级
	private String followRemark;//最后跟进备注
	private String remark;//备注
	
	private String isShowMe;//1：自己资源池，！1：其他资源池
	private String resourceId;//其他资源池ID
	
	
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
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}
	public String getDeliverTime() {
		return deliverTime;
	}
	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
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
	public String getPointialStuName() {
		return pointialStuName;
	}
	public void setPointialStuName(String pointialStuName) {
		this.pointialStuName = pointialStuName;
	}
	public String getPointialStuSchool() {
		return pointialStuSchool;
	}
	public void setPointialStuSchool(String pointialStuSchool) {
		this.pointialStuSchool = pointialStuSchool;
	}
	public String getFollowRemark() {
		return followRemark;
	}
	public void setFollowRemark(String followRemark) {
		this.followRemark = followRemark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsShowMe() {
		return isShowMe;
	}
	public void setIsShowMe(String isShowMe) {
		this.isShowMe = isShowMe;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	

}
