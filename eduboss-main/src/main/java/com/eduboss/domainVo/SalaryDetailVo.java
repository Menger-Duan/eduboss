package com.eduboss.domainVo;

import java.math.BigDecimal;

public class SalaryDetailVo {
	
	private String id;
	
	/* 员工*/
	private String staffId;
	private String staffName;
	
	/* 组织机构*/
	private String organizationId;
	private String organizationName;
	
	/* 基本工资*/
	private BigDecimal salaryBase;
	
	/* 绩效工资*/
	private BigDecimal salaryBonus;
	
	/* 其他工资*/
	private BigDecimal salaryOther;
	
	/* 其他工资描述*/
	private String otherDescribe;
	
	/* 工资合计*/
	private BigDecimal salaryTotal;	
	
	/* 日期*/	
	private String salaryTime;
	
	/* 修改人*/
	private String modifierId;
	private String modifierName;
	
	/* 修改时间*/	
	private String modifyTime;
	
	/* 是否已归档：0未归档  1已归档*/
	private Integer completeFile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
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

	public BigDecimal getSalaryBase() {
		return salaryBase;
	}

	public void setSalaryBase(BigDecimal salaryBase) {
		this.salaryBase = salaryBase;
	}

	public BigDecimal getSalaryBonus() {
		return salaryBonus;
	}

	public void setSalaryBonus(BigDecimal salaryBonus) {
		this.salaryBonus = salaryBonus;
	}

	public BigDecimal getSalaryOther() {
		return salaryOther;
	}

	public void setSalaryOther(BigDecimal salaryOther) {
		this.salaryOther = salaryOther;
	}

	public String getOtherDescribe() {
		return otherDescribe;
	}

	public void setOtherDescribe(String otherDescribe) {
		this.otherDescribe = otherDescribe;
	}

	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getCompleteFile() {
		return completeFile;
	}

	public void setCompleteFile(Integer completeFile) {
		this.completeFile = completeFile;
	}

	public BigDecimal getSalaryTotal() {
		return salaryTotal;
	}

	public void setSalaryTotal(BigDecimal salaryTotal) {
		this.salaryTotal = salaryTotal;
	}

	public String getSalaryTime() {
		return salaryTime;
	}

	public void setSalaryTime(String salaryTime) {
		this.salaryTime = salaryTime;
	}
	
	
}
