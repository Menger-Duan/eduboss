package com.eduboss.domainVo;

import com.eduboss.common.PromiseReturnType;
import com.eduboss.common.StudentPromiseStatus;

import java.math.BigDecimal;

/**
 * 目标班学生客户合同等信息
 * */

public class PromiseStudentCustomerVo {
	
	/**
	 * ID
	 * */
	private String id;
	
	/**
	 * 目标班ID
	 * */
	private String promiseClassId;
	
	/**
	 * 目标班名称
	 * */
	private String promiseClassName;
	
	/**
	 * 学生ID
	 * */
	private String studentId;
	
	/**
	 * 学生名称 
	 * */
	private String studentName;


	private String gradeName;
	private String gradeId;
	private String customerContact;
	private String signStaffName;
	private String sudyManagerName;
	private String customerName;

	private String productId;
	private String productName;
	private String productQuarterId;
	private String productQuarterName;
	
	/**
	 * 合同产品ID
	 * */
	private String contractProductId;
	
	/**
	 * 上课时间
	 * */
	private String courseDate;
	
	/**
	 * 上课状态
	 * */
	private String courseStatus;
	
	/**
	 * 完结状态
	 * */
	private String resultStatus;
	
	/**
	 * 是否退班
	 * */
	private String abortClass;

	/**
	 * 总课时数
	 * */
	private BigDecimal quantity;
	
	/**
	 * 消费课时数
	 * */
	private BigDecimal consumeQuantity;
	
	/**
	 * 保底资金比例
	 * */
	private BigDecimal promiseClassDiscount;
	
	/**
	 * 课程进度中的消费课时数改取月结扣费课时
	 * @return
	 */
	private BigDecimal monthHours;

	private String contractProductStatus;//合同产品状态


	private PromiseReturnType returnType;

	private String auditRemark;

	private String oldAuditRemark;

	private String blcampusId;

	private String contractRemark;


	public String getContractRemark() {

		return contractRemark;
	}

	public void setContractRemark(String contractRemark) {
		this.contractRemark = contractRemark;
	}

	public String getBlcampusId() {
		return blcampusId;
	}

	public void setBlcampusId(String blcampusId) {
		this.blcampusId = blcampusId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPromiseClassId() {
		return promiseClassId;
	}

	public void setPromiseClassId(String promiseClassId) {
		this.promiseClassId = promiseClassId;
	}

	public String getPromiseClassName() {
		return promiseClassName;
	}

	public void setPromiseClassName(String promiseClassName) {
		this.promiseClassName = promiseClassName;
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

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}

	public String getSignStaffName() {
		return signStaffName;
	}

	public void setSignStaffName(String signStaffName) {
		this.signStaffName = signStaffName;
	}

	public String getSudyManagerName() {
		return sudyManagerName;
	}

	public void setSudyManagerName(String sudyManagerName) {
		this.sudyManagerName = sudyManagerName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}

	public String getAbortClass() {
		return abortClass;
	}

	public void setAbortClass(String abortClass) {
		this.abortClass = abortClass;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getConsumeQuantity() {
		return consumeQuantity;
	}

	public void setConsumeQuantity(BigDecimal consumeQuantity) {
		this.consumeQuantity = consumeQuantity;
	}

	public BigDecimal getPromiseClassDiscount() {
		return promiseClassDiscount;
	}

	public void setPromiseClassDiscount(BigDecimal promiseClassDiscount) {
		this.promiseClassDiscount = promiseClassDiscount;
	}

	public BigDecimal getMonthHours() {
		return monthHours;
	}

	public void setMonthHours(BigDecimal monthHours) {
		this.monthHours = monthHours;
	}

	public String getContractProductStatus() {
		return contractProductStatus;
	}

	public void setContractProductStatus(String contractProductStatus) {
		this.contractProductStatus = contractProductStatus;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductQuarterId() {
		return productQuarterId;
	}

	public void setProductQuarterId(String productQuarterId) {
		this.productQuarterId = productQuarterId;
	}

	public String getProductQuarterName() {
		return productQuarterName;
	}

	public void setProductQuarterName(String productQuarterName) {
		this.productQuarterName = productQuarterName;
	}

	public PromiseReturnType getReturnType() {
		return returnType;
	}

	public void setReturnType(PromiseReturnType returnType) {
		this.returnType = returnType;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	public String getOldAuditRemark() {
		return oldAuditRemark;
	}

	public void setOldAuditRemark(String oldAuditRemark) {
		this.oldAuditRemark = oldAuditRemark;
	}
}
