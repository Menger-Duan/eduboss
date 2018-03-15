package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * StudnetAccMv entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDNET_ACC_MV")
public class StudnetAccMv implements java.io.Serializable {

	// Fields

	private String studentId;
	private int version;//加版本号，用来做乐观锁，防止重复操作锁表
	// private Student student;
	private BigDecimal totalAmount;//总金额
	private BigDecimal paidAmount;//已付总金额
	private BigDecimal remainingAmount;//剩余总金额
	private BigDecimal consumeAmount;//消费总金额
	private BigDecimal oneOnOnePaidAmount;//一对一已分配金额
	private BigDecimal oneOnOneConsumeAmount;//一对一消费金额
	private BigDecimal oneOnOneRemainingAmount;//一对一剩余金额
	private BigDecimal oneOnOneRemainingHour;//一对一剩余课时数
	private BigDecimal oneOnOneArrangedHour;//一对一可排课时
	private BigDecimal oneOnOneCompletedHour;//一对一已上课时   暂时不要  vo计算出来
	private BigDecimal oneOnOneFreePaidHour;//一对一赠送剩余课时
	private BigDecimal oneOnOneFreeCompletedHour;//一对一已上赠送课时
	private BigDecimal miniPaidAmount;//小班已分配金额
	private BigDecimal miniConsumeAmount;//小班消费金额
	private BigDecimal miniRemainingAmount;//小班剩余金额
	private BigDecimal otherPaidAmount;//其他已分配金额
	private BigDecimal otherConsumeAmount;//其他消费金额
	private BigDecimal otherRemainingAmount;//其他剩余金额

	private BigDecimal electronicAccount;//学生电子账户
	
	private BigDecimal otmPaidAmount;//一对多已分配金额
	private BigDecimal otmConsumeAmount;//一对多消费金额
	private BigDecimal otmRemainingAmount;//一对多剩余金额
	
	private BigDecimal ecsPaidAmount;//目标班已分配金额
	private BigDecimal ecsConsumeAmount;//目标班消费金额
	private BigDecimal ecsRemainingAmount;//目标班剩余金额
	
	private BigDecimal frozenAmount; //冻结的金额
	

	// Constructors

	/** default constructor */
	public StudnetAccMv() {
	}

	/** minimal constructor */
	public StudnetAccMv(Student student) {
//		this.student = student;
		this.studentId = student.getId();
		this.totalAmount = BigDecimal.ZERO;
		this.paidAmount = BigDecimal.ZERO;
		this.remainingAmount = BigDecimal.ZERO;
		this.consumeAmount = BigDecimal.ZERO;
		this.oneOnOnePaidAmount = BigDecimal.ZERO;
		this.oneOnOneConsumeAmount = BigDecimal.ZERO;
		this.oneOnOneRemainingAmount = BigDecimal.ZERO;
		this.oneOnOneRemainingHour = BigDecimal.ZERO;
		this.oneOnOneArrangedHour = BigDecimal.ZERO;
		this.oneOnOneCompletedHour = BigDecimal.ZERO;
		this.miniPaidAmount = BigDecimal.ZERO;
		this.miniConsumeAmount = BigDecimal.ZERO;
		this.miniRemainingAmount = BigDecimal.ZERO;
		this.otherPaidAmount = BigDecimal.ZERO;
		this.otherConsumeAmount = BigDecimal.ZERO;
		this.otherRemainingAmount = BigDecimal.ZERO;
		this.oneOnOneFreePaidHour = BigDecimal.ZERO;
		this.oneOnOneFreeCompletedHour = BigDecimal.ZERO;
		
		this.otmPaidAmount = BigDecimal.ZERO;
		this.otmConsumeAmount = BigDecimal.ZERO;
		this.otmRemainingAmount = BigDecimal.ZERO;
		this.ecsConsumeAmount = BigDecimal.ZERO;
		this.ecsPaidAmount = BigDecimal.ZERO;
		this.ecsRemainingAmount = BigDecimal.ZERO;
	}

	/** full constructor */
	public StudnetAccMv(Student student, BigDecimal totalAmount, BigDecimal paidAmount, BigDecimal remainingAmount, BigDecimal consumeAmount,
			BigDecimal oneOnOnePaidAmount, BigDecimal oneOnOneConsumeAmount, BigDecimal oneOnOneRemainingAmount,
			BigDecimal oneOnOneRemainingHour, BigDecimal oneOnOneArrangedHour, BigDecimal oneOnOneCompletedHour, BigDecimal miniPaidAmount,
			BigDecimal miniConsumeAmount, BigDecimal miniRemainingAmount, BigDecimal otherPaidAmount, BigDecimal otherConsumeAmount,
			BigDecimal otherRemainingAmount, BigDecimal otmPaidAmount, BigDecimal otmConsumeAmount, BigDecimal otmRemainingAmount,
			BigDecimal ecsConsumeAmount, BigDecimal ecsPaidAmount, BigDecimal ecsRemainingAmount, BigDecimal frozenAmount) {
//		this.student = student;
		this.totalAmount = totalAmount;
		this.paidAmount = paidAmount;
		this.remainingAmount = remainingAmount;
		this.consumeAmount = consumeAmount;
		this.oneOnOnePaidAmount = oneOnOnePaidAmount;
		this.oneOnOneConsumeAmount = oneOnOneConsumeAmount;
		this.oneOnOneRemainingAmount = oneOnOneRemainingAmount;
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
		this.oneOnOneArrangedHour = oneOnOneArrangedHour;
		this.oneOnOneCompletedHour = oneOnOneCompletedHour;
		this.miniPaidAmount = miniPaidAmount;
		this.miniConsumeAmount = miniConsumeAmount;
		this.miniRemainingAmount = miniRemainingAmount;
		this.otherPaidAmount = otherPaidAmount;
		this.otherConsumeAmount = otherConsumeAmount;
		this.otherRemainingAmount = otherRemainingAmount;
		
		this.otmConsumeAmount = otmConsumeAmount;
		this.otmPaidAmount = otmPaidAmount;
		this.otmRemainingAmount = otmRemainingAmount;
		
		this.ecsConsumeAmount = ecsConsumeAmount;
		this.ecsPaidAmount = ecsPaidAmount;
		this.ecsRemainingAmount = ecsRemainingAmount;
		this.frozenAmount = frozenAmount;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "assigned")
	@GeneratedValue(generator = "generator")
	@Column(name = "STUDENT_ID", unique = true, nullable = false, length = 32)
	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "STUDENT_ID", unique = true, nullable = false, insertable = false, updatable = false)
//	public Student getStudent() {
//		return this.student;
//	}
//
//	public void setStudent(Student student) {
//		this.student = student;
//	}

	@Version
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "TOTAL_AMOUNT", precision = 10)
	public BigDecimal getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "PAID_AMOUNT", precision = 10)
	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Column(name = "REMAINING_AMOUNT", precision = 10)
	public BigDecimal getRemainingAmount() {
//		if (this.paidAmount == null) this.paidAmount = BigDecimal.ZERO;
//		if (this.consumeAmount == null) this.consumeAmount = BigDecimal.ZERO;
//		return this.paidAmount.subtract(this.consumeAmount);
		return this.remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	@Column(name = "CONSUME_AMOUNT", precision = 10)
	public BigDecimal getConsumeAmount() {
		return this.consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	@Column(name = "ONE_ON_ONE_PAID_AMOUNT", precision = 10)
	public BigDecimal getOneOnOnePaidAmount() {
		return this.oneOnOnePaidAmount;
	}

	public void setOneOnOnePaidAmount(BigDecimal oneOnOnePaidAmount) {
		this.oneOnOnePaidAmount = oneOnOnePaidAmount;
	}

	@Column(name = "ONE_ON_ONE_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getOneOnOneConsumeAmount() {
		return this.oneOnOneConsumeAmount;
	}

	public void setOneOnOneConsumeAmount(BigDecimal oneOnOneConsumeAmount) {
		this.oneOnOneConsumeAmount = oneOnOneConsumeAmount;
	}

	@Column(name = "ONE_ON_ONE_REMAINING_AMOUNT", precision = 10)
	public BigDecimal getOneOnOneRemainingAmount() {
		return this.oneOnOneRemainingAmount;
	}

	public void setOneOnOneRemainingAmount(BigDecimal oneOnOneRemainingAmount) {
		this.oneOnOneRemainingAmount = oneOnOneRemainingAmount;
	}

	@Column(name = "ONE_ON_ONE_REMAINING_HOUR", precision = 10)
	public BigDecimal getOneOnOneRemainingHour() {
		return this.oneOnOneRemainingHour;
	}

	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}

	@Column(name = "ONE_ON_ONE_ARRANGED_HOUR", precision = 10)
	public BigDecimal getOneOnOneArrangedHour() {
		return this.oneOnOneArrangedHour;
	}

	public void setOneOnOneArrangedHour(BigDecimal oneOnOneArrangedHour) {
		this.oneOnOneArrangedHour = oneOnOneArrangedHour;
	}

	@Column(name = "ONE_ON_ONE_COMPLETED_HOUR", precision = 10)
	public BigDecimal getOneOnOneCompletedHour() {
		return this.oneOnOneCompletedHour;
	}

	public void setOneOnOneCompletedHour(BigDecimal oneOnOneCompletedHour) {
		this.oneOnOneCompletedHour = oneOnOneCompletedHour;
	}
	
	@Column(name = "ONE_ON_ONE_FREE_PAID_HOUR", precision = 10)
	public BigDecimal getOneOnOneFreePaidHour() {
		return oneOnOneFreePaidHour;
	}

	public void setOneOnOneFreePaidHour(BigDecimal oneOnOneFreePaidHour) {
		this.oneOnOneFreePaidHour = oneOnOneFreePaidHour;
	}

	@Column(name = "ONE_ON_ONE_FREE_COMPLETED_HOUR", precision = 10)
	public BigDecimal getOneOnOneFreeCompletedHour() {
		return oneOnOneFreeCompletedHour;
	}

	public void setOneOnOneFreeCompletedHour(BigDecimal oneOnOneFreeCompletedHour) {
		this.oneOnOneFreeCompletedHour = oneOnOneFreeCompletedHour;
	}
	
	@Column(name = "MINI_PAID_AMOUNT", precision = 10)
	public BigDecimal getMiniPaidAmount() {
		return this.miniPaidAmount;
	}

	public void setMiniPaidAmount(BigDecimal miniPaidAmount) {
		this.miniPaidAmount = miniPaidAmount;
	}

	@Column(name = "MINI_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getMiniConsumeAmount() {
		return this.miniConsumeAmount;
	}

	public void setMiniConsumeAmount(BigDecimal miniConsumeAmount) {
		this.miniConsumeAmount = miniConsumeAmount;
	}

	@Column(name = "MINI_REMAINING_AMOUNT", precision = 10)
	public BigDecimal getMiniRemainingAmount() {
		return this.miniRemainingAmount;
	}

	public void setMiniRemainingAmount(BigDecimal miniRemainingAmount) {
		this.miniRemainingAmount = miniRemainingAmount;
	}

	@Column(name = "OTHER_PAID_AMOUNT", precision = 10)
	public BigDecimal getOtherPaidAmount() {
		return this.otherPaidAmount;
	}

	public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
		this.otherPaidAmount = otherPaidAmount;
	}

	@Column(name = "OTHER_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getOtherConsumeAmount() {
		return this.otherConsumeAmount;
	}

	public void setOtherConsumeAmount(BigDecimal otherConsumeAmount) {
		this.otherConsumeAmount = otherConsumeAmount;
	}

	@Column(name = "OTHER_REMAINING_AMOUNT", precision = 10)
	public BigDecimal getOtherRemainingAmount() {
		return this.otherRemainingAmount;
	}

	public void setOtherRemainingAmount(BigDecimal otherRemainingAmount) {
		this.otherRemainingAmount = otherRemainingAmount;
	}

	@Column(name = "ELECTRONIC_ACCOUNT", precision = 10)
	public BigDecimal getElectronicAccount() {
		return electronicAccount;
	}

	public void setElectronicAccount(BigDecimal electronicAccount) {
		this.electronicAccount = electronicAccount;
	}

	@Column(name = "OTM_PAID_AMOUNT", precision = 10)
	public BigDecimal getOtmPaidAmount() {
		return otmPaidAmount;
	}

	public void setOtmPaidAmount(BigDecimal otmPaidAmount) {
		this.otmPaidAmount = otmPaidAmount;
	}

	@Column(name = "OTM_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getOtmConsumeAmount() {
		return otmConsumeAmount;
	}

	public void setOtmConsumeAmount(BigDecimal otmConsumeAmount) {
		this.otmConsumeAmount = otmConsumeAmount;
	}

	@Column(name = "OTM_REMAINING_AMOUNT", precision = 10)
	public BigDecimal getOtmRemainingAmount() {
		return otmRemainingAmount;
	}

	public void setOtmRemainingAmount(BigDecimal otmRemainingAmount) {
		this.otmRemainingAmount = otmRemainingAmount;
	}

	@Column(name = "ECS_PAID_AMOUNT", precision = 10)
	public BigDecimal getEcsPaidAmount() {
		return ecsPaidAmount;
	}

	public void setEcsPaidAmount(BigDecimal ecsPaidAmount) {
		this.ecsPaidAmount = ecsPaidAmount;
	}

	@Column(name = "ECS_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getEcsConsumeAmount() {
		return ecsConsumeAmount;
	}

	public void setEcsConsumeAmount(BigDecimal ecsConsumeAmount) {
		this.ecsConsumeAmount = ecsConsumeAmount;
	}

	@Column(name = "ECS_REMAINING_AMOUNT", precision = 10)
	public BigDecimal getEcsRemainingAmount() {
		return ecsRemainingAmount;
	}

	public void setEcsRemainingAmount(BigDecimal ecsRemainingAmount) {
		this.ecsRemainingAmount = ecsRemainingAmount;
	}

	@Column(name = "FROZEN_AMOUNT", precision = 10)
	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

}