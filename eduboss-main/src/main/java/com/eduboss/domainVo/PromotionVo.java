package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.PromotionType;

public class PromotionVo {
	
	// Fields
	private String id;
	private String name;
	private String organizationId;
	private String organizationName;
	private PromotionType promotionType;
	private String promotionTypeName;
	private String promotionTypeValue;
	private String promotionValue;
	private String data;
	private String startTime;
	private String endTime;
	private String isActive;
	private String isOverdue;
	private String accessRoles;
	private String createUserId;
	private String createUserName;
	private String createTime;
	private String modifyUserId;
	private String modifyUserName;
	private String modifyTime;
	
	private String startTimeSearch;
	private String endTimeSearch;
	private String authUserId;
	private String productId;
	private String calculationExpression; //计算公式，用于保存在合同产品中，只作参考
	
	private boolean canChange;
	
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
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public PromotionType getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}
	public String getPromotionValue() {
		return promotionValue;
	}
	public void setPromotionValue(String promotionValue) {
		this.promotionValue = promotionValue;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyUserName() {
		return modifyUserName;
	}
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getIsOverdue() {
		return isOverdue;
	}
	public void setIsOverdue(String isOverdue) {
		this.isOverdue = isOverdue;
	}
	public String getAuthUserId() {
		return authUserId;
	}
	public void setAuthUserId(String authUserId) {
		this.authUserId = authUserId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getAccessRoles() {
		return accessRoles;
	}
	public void setAccessRoles(String accessRoles) {
		this.accessRoles = accessRoles;
	}
	public String getPromotionTypeName() {
		return promotionTypeName;
	}
	public void setPromotionTypeName(String promotionTypeName) {
		this.promotionTypeName = promotionTypeName;
	}
	public String getPromotionTypeValue() {
		return promotionTypeValue;
	}
	public void setPromotionTypeValue(String promotionTypeValue) {
		this.promotionTypeValue = promotionTypeValue;
	}
	public String getStartTimeSearch() {
		return startTimeSearch;
	}
	public void setStartTimeSearch(String startTimeSearch) {
		this.startTimeSearch = startTimeSearch;
	}
	public String getEndTimeSearch() {
		return endTimeSearch;
	}
	public void setEndTimeSearch(String endTimeSearch) {
		this.endTimeSearch = endTimeSearch;
	}
	public String getCalculationExpression() {
		return calculationExpression;
	}
	public void setCalculationExpression(String calculationExpression) {
		this.calculationExpression = calculationExpression;
	}
	public boolean isCanChange() {
		return canChange;
	}
	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}
	
}
