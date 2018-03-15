package com.eduboss.domainVo;

import java.math.BigDecimal;


/**
 * 目标班学生报名
 * */
public class PromiseClassApplyVo {
	/**
	 * 学生ID
	 * */
	private String studentId;
	
	/**
	 * 学生姓名
	 * */
	private String studentName;
	
	/**
	 * 年级
	 * */
	private String gradeId;
	
	/**
	 * 学生合同完结状态
	 * */
	private String resultStatus;
	
	/**
	 * 已支付金额
	 * */
	private BigDecimal paidAmount;
	
	/**
	 * 剩余资金
	 * */
	private BigDecimal remainningAmount;
	
	private BigDecimal planAmount;
	
	/**
	 * 消费金额
	 * */
	private BigDecimal consumeAmount;
	
	/**
	 * 总课时数
	 * */
	private BigDecimal quantity;
	
	/**
	 * 已消费课时数
	 * */
	private BigDecimal consumeQuanity;;
	
	
	/**
	 * 合同ID
	 * */
	private String contractId;
	
	/**
	 * 合同产品ID
	 * */
	private String contractProductId;
	
	/**
	 * 目标班保底资金比例
	 * */
	private String promiseClassDiscount;
	
	/**
	 * 课程进度中的消费课时数改取月结扣费课时
	 * @return
	 */
	private BigDecimal monthHours;

	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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
	
	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	
	
	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getRemainningAmount() {
		return remainningAmount;
	}

	public void setRemainningAmount(BigDecimal remainningAmount) {
		this.remainningAmount = remainningAmount;
	}

	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}

	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getConsumeQuanity() {
		return consumeQuanity;
	}

	public void setConsumeQuanity(BigDecimal consumeQuanity) {
		this.consumeQuanity = consumeQuanity;
	}

	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getPromiseClassDiscount() {
		return promiseClassDiscount;
	}

	public void setPromiseClassDiscount(String promiseClassDiscount) {
		this.promiseClassDiscount = promiseClassDiscount;
	}

	public BigDecimal getMonthHours() {
		return monthHours;
	}

	public void setMonthHours(BigDecimal monthHours) {
		this.monthHours = monthHours;
	}
	

}
