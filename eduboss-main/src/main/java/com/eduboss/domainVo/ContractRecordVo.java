package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@JsonIgnoreProperties( ignoreUnknown = true)
public class ContractRecordVo {
	/**
	 * 合同修改记录表，记录一些主要字段的变动
	 */
	private String id;
	
	private String contractTypeName;
	
	private String contractStatusName;
	
	private String paidStatusName;
	
	
	private String contractId;
	private String signUserName;
	private String studentName;
	private String grade;//合同年级   
	private String createUser;
	private String createTime;
	private String remark;
	
	private BigDecimal totalAmount;//总额
	private BigDecimal paidAmount;//支付总额
	private BigDecimal oooAmount;//一对一金额
	private BigDecimal miniAmount;//小班金额
	private BigDecimal omAmount;//一对多金额
	private BigDecimal promiseAmount;//目标班金额
	private BigDecimal otherAmount;//其他金额
	private BigDecimal promotionAmount;//优惠金额
	private BigDecimal toPayAmount;//待支付金额
	private String blCampusId;  //合同校区
	private String blCampusName;  //合同校区
	private String updateTypeName;
	private BigDecimal twoTeacherAmount;//双师
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractTypeName() {
		return contractTypeName;
	}
	public void setContractTypeName(String contractTypeName) {
		this.contractTypeName = contractTypeName;
	}
	public String getContractStatusName() {
		return contractStatusName;
	}
	public void setContractStatusName(String contractStatusName) {
		this.contractStatusName = contractStatusName;
	}
	public String getPaidStatusName() {
		return paidStatusName;
	}
	public void setPaidStatusName(String paidStatusName) {
		this.paidStatusName = paidStatusName;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getSignUserName() {
		return signUserName;
	}
	public void setSignUserName(String signUserName) {
		this.signUserName = signUserName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public BigDecimal getOooAmount() {
		return oooAmount;
	}
	public void setOooAmount(BigDecimal oooAmount) {
		this.oooAmount = oooAmount;
	}
	public BigDecimal getMiniAmount() {
		return miniAmount;
	}
	public void setMiniAmount(BigDecimal miniAmount) {
		this.miniAmount = miniAmount;
	}
	public BigDecimal getOmAmount() {
		return omAmount;
	}
	public void setOmAmount(BigDecimal omAmount) {
		this.omAmount = omAmount;
	}
	public BigDecimal getPromiseAmount() {
		return promiseAmount;
	}
	public void setPromiseAmount(BigDecimal promiseAmount) {
		this.promiseAmount = promiseAmount;
	}
	public BigDecimal getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}
	public BigDecimal getToPayAmount() {
		return toPayAmount;
	}
	public void setToPayAmount(BigDecimal toPayAmount) {
		this.toPayAmount = toPayAmount;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getUpdateTypeName() {
		return updateTypeName;
	}
	public void setUpdateTypeName(String updateTypeName) {
		this.updateTypeName = updateTypeName;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public BigDecimal getTwoTeacherAmount() {
		return twoTeacherAmount;
	}

	public void setTwoTeacherAmount(BigDecimal twoTeacherAmount) {
		this.twoTeacherAmount = twoTeacherAmount;
	}
}
