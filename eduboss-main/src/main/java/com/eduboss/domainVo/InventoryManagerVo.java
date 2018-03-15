package com.eduboss.domainVo;

import com.eduboss.common.InventoryOperateType;

public class InventoryManagerVo {
	
	private String id;
	
	private String inventoryProductId;
	private String inventoryProductName;
	
	//private String targetInventoryManagerId;
	
	//private double price;
	
	private String resourceInventoryId;
	private String resourceInventoryOrgId;
	private String resourceInventoryOrgName;
	private double resourceInventoryPrice;
	private String resourceInventoryProductId;
	private String resourceInventoryProductName;
	
	private String targetInventoryId;
	private String targetInventoryOrgId;
	private String targetInventoryOrgName;
	private double targetInventoryPrice;
	private String targetInventoryProductId;
	private String targetInventoryProductName;
	
	private double number;
	
	//private InventoryOperateType operationType;
	
	private String operationTypeName;
	private String operationTypeValue;
	
	private String consumeUserId;
	private String consumeUserName;
	
	private String studentId;
	private String studentName;
	
	private String createUserId;
	private String createUserName;
	
	private String createTime;
	
	private String startDate;
	private String endDate;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInventoryProductId() {
		if(resourceInventoryProductId!=null){
			return resourceInventoryProductId;
		}else if(targetInventoryProductId!=null){
			return targetInventoryProductId;
		}else{
			return inventoryProductId;
		}
		
	}

	public void setInventoryProductId(String inventoryProductId) {
		this.inventoryProductId = inventoryProductId;
	}

	public String getInventoryProductName() {
		if(resourceInventoryProductName!=null){
			return resourceInventoryProductName;
		}else if(targetInventoryProductName!=null){
			return targetInventoryProductName;
		}else{
			return inventoryProductName;
		}
		
	}

	public void setInventoryProductName(String inventoryProductName) {
		this.inventoryProductName = inventoryProductName;
	}

	public String getResourceInventoryId() {
		return resourceInventoryId;
	}

	public void setResourceInventoryId(String resourceInventoryId) {
		this.resourceInventoryId = resourceInventoryId;
	}

	public String getResourceInventoryOrgId() {
		return resourceInventoryOrgId;
	}

	public void setResourceInventoryOrgId(String resourceInventoryOrgId) {
		this.resourceInventoryOrgId = resourceInventoryOrgId;
	}

	public String getResourceInventoryOrgName() {
		return resourceInventoryOrgName;
	}

	public void setResourceInventoryOrgName(String resourceInventoryOrgName) {
		this.resourceInventoryOrgName = resourceInventoryOrgName;
	}

	public double getResourceInventoryPrice() {
		return resourceInventoryPrice;
	}

	public void setResourceInventoryPrice(double resourceInventoryPrice) {
		this.resourceInventoryPrice = resourceInventoryPrice;
	}

	public String getResourceInventoryProductId() {
		return resourceInventoryProductId;
	}

	public void setResourceInventoryProductId(String resourceInventoryProductId) {
		this.resourceInventoryProductId = resourceInventoryProductId;
	}

	public String getResourceInventoryProductName() {
		return resourceInventoryProductName;
	}

	public void setResourceInventoryProductName(String resourceInventoryProductName) {
		this.resourceInventoryProductName = resourceInventoryProductName;
	}

	public String getTargetInventoryId() {
		return targetInventoryId;
	}

	public void setTargetInventoryId(String targetInventoryId) {
		this.targetInventoryId = targetInventoryId;
	}

	public String getTargetInventoryOrgId() {
		return targetInventoryOrgId;
	}

	public void setTargetInventoryOrgId(String targetInventoryOrgId) {
		this.targetInventoryOrgId = targetInventoryOrgId;
	}

	public String getTargetInventoryOrgName() {
		return targetInventoryOrgName;
	}

	public void setTargetInventoryOrgName(String targetInventoryOrgName) {
		this.targetInventoryOrgName = targetInventoryOrgName;
	}

	public double getTargetInventoryPrice() {
		return targetInventoryPrice;
	}

	public void setTargetInventoryPrice(double targetInventoryPrice) {
		this.targetInventoryPrice = targetInventoryPrice;
	}

	public String getTargetInventoryProductId() {
		return targetInventoryProductId;
	}

	public void setTargetInventoryProductId(String targetInventoryProductId) {
		this.targetInventoryProductId = targetInventoryProductId;
	}

	public String getTargetInventoryProductName() {
		return targetInventoryProductName;
	}

	public void setTargetInventoryProductName(String targetInventoryProductName) {
		this.targetInventoryProductName = targetInventoryProductName;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

	public String getOperationTypeName() {
		return operationTypeName;
	}

	public void setOperationTypeName(String operationTypeName) {
		this.operationTypeName = operationTypeName;
	}

	public String getOperationTypeValue() {
		return operationTypeValue;
	}

	public void setOperationTypeValue(String operationTypeValue) {
		this.operationTypeValue = operationTypeValue;
	}

	public String getConsumeUserId() {
		return consumeUserId;
	}

	public void setConsumeUserId(String consumeUserId) {
		this.consumeUserId = consumeUserId;
	}

	public String getConsumeUserName() {
		return consumeUserName;
	}

	public void setConsumeUserName(String consumeUserName) {
		this.consumeUserName = consumeUserName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public double getInventoryPrice() {
		if(resourceInventoryPrice>0){
			return resourceInventoryPrice;
		}else{
			return targetInventoryPrice;
		}
	}
	

}
