package com.eduboss.domainVo;

import com.eduboss.common.CustomerEventType;

public class CustomerDynamicStatusVo {
	
	private String id;
	private String customerId;
	private String customerName;
	private CustomerEventType dynamicStatusType;
	private String occourTime;
	private String description;
	private String referUrl;
	private String referuserId;
	private String referuserName;
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private Integer statusNum;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * @return the dynamicStatusType
	 */
	public CustomerEventType getDynamicStatusType() {
		return dynamicStatusType;
	}
	/**
	 * @param dynamicStatusType the dynamicStatusType to set
	 */
	public void setDynamicStatusType(CustomerEventType dynamicStatusType) {
		this.dynamicStatusType = dynamicStatusType;
	}
	/**
	 * @return the occourTime
	 */
	public String getOccourTime() {
		return occourTime;
	}
	/**
	 * @param occourTime the occourTime to set
	 */
	public void setOccourTime(String occourTime) {
		this.occourTime = occourTime;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the referUrl
	 */
	public String getReferUrl() {
		return referUrl;
	}
	/**
	 * @param referUrl the referUrl to set
	 */
	public void setReferUrl(String referUrl) {
		this.referUrl = referUrl;
	}
	/**
	 * @return the referuserId
	 */
	public String getReferuserId() {
		return referuserId;
	}
	/**
	 * @param referuserId the referuserId to set
	 */
	public void setReferuserId(String referuserId) {
		this.referuserId = referuserId;
	}
	/**
	 * @return the referuserName
	 */
	public String getReferuserName() {
		return referuserName;
	}
	/**
	 * @param referuserName the referuserName to set
	 */
	public void setReferuserName(String referuserName) {
		this.referuserName = referuserName;
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
	public Integer getStatusNum() {
		return statusNum;
	}
	public void setStatusNum(Integer statusNum) {
		this.statusNum = statusNum;
	}
	
	
	
}
