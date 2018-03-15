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

/**
 * 目标班月结记录表
 * @author laiyongchang
 * */
@Entity
@Table(name = "PROMISE_CLASS_RECORD")
public class PromiseClassRecord implements java.io.Serializable{

	/**
	 * 目标班月结记录ID
	 * */
	private String id;
	
	/**
	 * 目标班ID
	 * */
	private PromiseClass promiseClass;
	
	/**
	 * 学生ID
	 * */
	private String studentId;
	
	/**
	 * 上课年份
	 * */
	private String classYear;
	
	/**
	 * 上课月份
	 * */
	private String classMonth;
	
	/**
	 * 扣费总课时
	 * */
	private BigDecimal chargeHours;
	
	/**
	 * 扣费总费用
	 * */
	private BigDecimal chargeAmount;
	
	/**
	 * 状态
	 * */
	private String status;
	
	/**
	 * 记录创建时间
	 * */
	private String createTime;
	
	/**
	 * 记录创建者ID
	 * */
	private String createUserId;
	
	/**
	 * 记录修改时间
	 * */
	private String modifyTime;
	
	/**
	 * 记录修改人ID
	 * */
	private String modifyUserId;
	
	private String classDate; // 扣费月份最后一天
	
	private String contractProductId;
	
	
	private Organization chargeCampus;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROMISE_CLASS_ID")
	public PromiseClass getPromiseClass() {
		return promiseClass;
	}

	public void setPromiseClass(PromiseClass promiseClass) {
		this.promiseClass = promiseClass;
	}

	@Column(name = "STUDENT_ID")
	public String getStudentId() {
		return studentId;
	}

	

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "CLASS_YEAR")
	public String getClassYear() {
		return classYear;
	}

	public void setClassYear(String classYear) {
		this.classYear = classYear;
	}

	@Column(name = "CLASS_MONTH")
	public String getClassMonth() {
		return classMonth;
	}

	public void setClassMonth(String classMonth) {
		this.classMonth = classMonth;
	}

	@Column(name = "CHARGE_HOURS")
	public BigDecimal getChargeHours() {
		return chargeHours;
	}

	public void setChargeHours(BigDecimal chargeHours) {
		this.chargeHours = chargeHours;
	}

	@Column(name = "CHARGE_AMOUNT")
	public BigDecimal getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(BigDecimal chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

	@Column(name = "CLASS_STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID")
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "CLASS_DATE")
	public String getClassDate() {
		return classDate;
	}

	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}

	
	@Column(name = "CONTRACT_PRODUCT_ID")
	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "charge_campus")
	public Organization getChargeCampus() {
		return chargeCampus;
	}

	public void setChargeCampus(Organization chargeCampus) {
		this.chargeCampus = chargeCampus;
	}
	
	
	
}
