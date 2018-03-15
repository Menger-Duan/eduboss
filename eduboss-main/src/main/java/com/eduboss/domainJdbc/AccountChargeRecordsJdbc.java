package com.eduboss.domainJdbc;

import java.math.BigDecimal;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;

public class AccountChargeRecordsJdbc {

	// Fields
	private String id;
	private String contractId;
	private BigDecimal amount;
	private String payChannel;
	private String payTime;
	private String courseId;
	private String productId;
	private String productName;
	private String operateUserId;
	private String operateUserName;
	private String studentId;
	private String studentName;
	private String remark;
	private String miniClassId;
	private String miniClassName;
	private String miniClassCourseId;
	private ProductType productType;
	private BigDecimal quantity;
	private String contractProductId;
	private String contractPorudctName;
	private ChargeType chargeType;
	private String promiseClassRecordId; //目标班月结记录
	private String blCampusId;
	
	private String transactionId; 
	private PayType payType; //资金来源（REAL：实收金额，PROMOTION：优惠金额）
	private String transactionTime;
	
	// 一对多
	private String otmClassId;
	private String otmClassName;
	private String otmClassCourseId;
	
	private BigDecimal courseMinutes; // 扣费课程分钟数
	
	private String lectureClassStudentId;
	
	
	//老师
	private String teacherId;
	private String teacherName;
	
	private ChargePayType chargePayType; // 扣费，冲销类型
	
	private String isWashed; // 是否发生过冲销  TRUE: 已冲销， FALSE: 没冲销

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getOperateUserId() {
		return operateUserId;
	}

	public void setOperateUserId(String operateUserId) {
		this.operateUserId = operateUserId;
	}

	public String getOperateUserName() {
		return operateUserName;
	}

	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMiniClassId() {
		return miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	public String getMiniClassName() {
		return miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	public String getMiniClassCourseId() {
		return miniClassCourseId;
	}

	public void setMiniClassCourseId(String miniClassCourseId) {
		this.miniClassCourseId = miniClassCourseId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getContractPorudctName() {
		return contractPorudctName;
	}

	public void setContractPorudctName(String contractPorudctName) {
		this.contractPorudctName = contractPorudctName;
	}

	public ChargeType getChargeType() {
		return chargeType;
	}

	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	public String getPromiseClassRecordId() {
		return promiseClassRecordId;
	}

	public void setPromiseClassRecordId(String promiseClassRecordId) {
		this.promiseClassRecordId = promiseClassRecordId;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getOtmClassId() {
		return otmClassId;
	}

	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
	}

	public String getOtmClassName() {
		return otmClassName;
	}

	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
	}

	public String getOtmClassCourseId() {
		return otmClassCourseId;
	}

	public void setOtmClassCourseId(String otmClassCourseId) {
		this.otmClassCourseId = otmClassCourseId;
	}

	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}

	public String getLectureClassStudentId() {
		return lectureClassStudentId;
	}

	public void setLectureClassStudentId(String lectureClassStudentId) {
		this.lectureClassStudentId = lectureClassStudentId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public ChargePayType getChargePayType() {
		return chargePayType;
	}

	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}

	public String getIsWashed() {
		return isWashed;
	}

	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}
		
}
