package com.eduboss.domainVo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class CustomerPoolDataVo {

	private String assign; //分配方式
	private String deliverTargetName; 
	private String dealStatus; 
	private String deliverType; 
	private String deliverTarget;
	
	public String getAssign() {
		return assign;
	}
	public void setAssign(String assign) {
		this.assign = assign;
	}
	public String getDeliverTargetName() {
		return deliverTargetName;
	}
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	} 
	
}
