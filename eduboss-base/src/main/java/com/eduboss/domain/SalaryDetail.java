package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SALARY_DETAIL")
public class SalaryDetail {

	private String id;
	
	/* 员工*/
	private User staff;
	
	/* 组织机构*/
	private Organization organization;
	
	/* 基本工资*/
	private BigDecimal salaryBase;
	
	/* 绩效工资*/
	private BigDecimal salaryBonus;
	
	/* 其他工资*/
	private BigDecimal salaryOther;
	
	/* 其他工资描述*/
	private String otherDescribe;
	
	/* 修日期*/	
	private String salaryTime;	
	
	/* 修改人*/
	private User modifier;
	
	/* 修改时间*/	
	private String modifyTime;
	
	/* 是否已归档：0未归档  1已归档*/
	private int completeFile;
	/*********************************************************************************************/
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JoinColumn(name = "STAFF_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getStaff() {
		return staff;
	}

	public void setStaff(User staff) {
		this.staff = staff;
	}

	@JoinColumn(name = "ORGANIZATION_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "SALARY_BASE", precision = 10)
	public BigDecimal getSalaryBase() {
		return salaryBase;
	}

	public void setSalaryBase(BigDecimal salaryBase) {
		this.salaryBase = salaryBase;
	}

	@Column(name = "SALARY_BONUS", precision = 10)
	public BigDecimal getSalaryBonus() {
		return salaryBonus;
	}

	public void setSalaryBonus(BigDecimal salaryBonus) {
		this.salaryBonus = salaryBonus;
	}

	@Column(name = "SALARY_OTHER", precision = 10)
	public BigDecimal getSalaryOther() {
		return salaryOther;
	}

	public void setSalaryOther(BigDecimal salaryOther) {
		this.salaryOther = salaryOther;
	}

	@Column(name = "OTHER_DESCRIBE")
	public String getOtherDescribe() {
		return otherDescribe;
	}

	public void setOtherDescribe(String otherDescribe) {
		this.otherDescribe = otherDescribe;
	}

	@JoinColumn(name = "MODIFIER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "COMPLETE_FILE")
	public int getCompleteFile() {
		return completeFile;
	}

	public void setCompleteFile(int completeFile) {
		this.completeFile = completeFile;
	}

	@Column(name = "SALARY_TIME")
	public String getSalaryTime() {
		return salaryTime;
	}

	public void setSalaryTime(String salaryTime) {
		this.salaryTime = salaryTime;
	}
	
	
	
}
