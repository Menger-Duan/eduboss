package com.eduboss.domainVo;

import java.math.BigDecimal;

public class PlanManagementVo {
	private String id;
	private String goalType;
	private String goalId;
	private String timeType;
	private String timeTypeName;
	private String yearId;
	private String yearName;
	private String quarterId;
	private String quarterName;
	private String monthId;
	private String monthName;
	private String targetTypeId;
	private String targetTypeName;
	private BigDecimal targetValue;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	private String modifyUserId;
	private String remark;
	private Integer planOrder;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoalType() {
		return goalType;
	}
	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}
	public String getGoalId() {
		return goalId;
	}
	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getTimeTypeName() {
		return timeTypeName;
	}
	public void setTimeTypeName(String timeTypeName) {
		this.timeTypeName = timeTypeName;
	}
	public String getYearId() {
		return yearId;
	}
	public void setYearId(String yearId) {
		this.yearId = yearId;
	}
	public String getYearName() {
		return yearName;
	}
	public void setYearName(String yearName) {
		this.yearName = yearName;
	}
	public String getQuarterId() {
		return quarterId;
	}
	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}
	public String getQuarterName() {
		return quarterName;
	}
	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}
	public String getMonthId() {
		return monthId;
	}
	public void setMonthId(String monthId) {
		this.monthId = monthId;
	}
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public String getTargetTypeId() {
		return targetTypeId;
	}
	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}
	public String getTargetTypeName() {
		return targetTypeName;
	}
	public void setTargetTypeName(String targetTypeName) {
		this.targetTypeName = targetTypeName;
	}
	public BigDecimal getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getPlanOrder() {
		return planOrder;
	}
	public void setPlanOrder(Integer planOrder) {
		this.planOrder = planOrder;
	}
	
}
