package com.eduboss.domainJdbc;

import java.math.BigDecimal;

import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.StudentPromiseClassStatus;
import com.eduboss.common.StudentStatus;

public class PromiseClassStudentJdbc {

	private String studentId;
	private String studentName;
	private String contractProductCreateTime;
	private String schoolId;
	private String schoolName;
	private String sex;
	private String contact;
	private String studentGradeId;
	private String studentGradeName;
	private String brenchName;
	private String blCampusName;
	private String productName;
	private String attanceNo;
	private StudentPromiseClassStatus primiseClassStatus;
	private String studentStatus;
	private StudentStatus status; //学生有效无效 1：有效 0 ：无效
	private String contractProductId;
	private String promiseClassId;
	
	private BigDecimal quantityInProduct;	// 产品内部的数量， 可以是目标班的 预定上课数量等
	private BigDecimal planAmount;
	private BigDecimal paidAmount;
	private BigDecimal consumeAmount;
	private BigDecimal consumeQuanity;
	private BigDecimal realAmount;
	private BigDecimal promotionAmount;
	private ContractProductStatus contractProductStatus;
	
	
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
	public String getContractProductCreateTime() {
		return contractProductCreateTime;
	}
	public void setContractProductCreateTime(String contractProductCreateTime) {
		this.contractProductCreateTime = contractProductCreateTime;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getStudentGradeId() {
		return studentGradeId;
	}
	public void setStudentGradeId(String studentGradeId) {
		this.studentGradeId = studentGradeId;
	}
	public String getStudentGradeName() {
		return studentGradeName;
	}
	public void setStudentGradeName(String studentGradeName) {
		this.studentGradeName = studentGradeName;
	}
	public String getBrenchName() {
		return brenchName;
	}
	public void setBrenchName(String brenchName) {
		this.brenchName = brenchName;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAttanceNo() {
		return attanceNo;
	}
	public void setAttanceNo(String attanceNo) {
		this.attanceNo = attanceNo;
	}
	public StudentPromiseClassStatus getPrimiseClassStatus() {
		return primiseClassStatus;
	}
	public void setPrimiseClassStatus(StudentPromiseClassStatus primiseClassStatus) {
		this.primiseClassStatus = primiseClassStatus;
	}
	public String getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
	public StudentStatus getStatus() {
		return status;
	}
	public void setStatus(StudentStatus status) {
		this.status = status;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public String getPromiseClassId() {
		return promiseClassId;
	}
	public void setPromiseClassId(String promiseClassId) {
		this.promiseClassId = promiseClassId;
	}
	public BigDecimal getQuantityInProduct() {
		return quantityInProduct;
	}
	public void setQuantityInProduct(BigDecimal quantityInProduct) {
		this.quantityInProduct = quantityInProduct;
	}
	public BigDecimal getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}
	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	public BigDecimal getConsumeQuanity() {
		return consumeQuanity;
	}
	public void setConsumeQuanity(BigDecimal consumeQuanity) {
		this.consumeQuanity = consumeQuanity;
	}
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}
	public ContractProductStatus getContractProductStatus() {
		return contractProductStatus;
	}
	public void setContractProductStatus(ContractProductStatus contractProductStatus) {
		this.contractProductStatus = contractProductStatus;
	}
	
}
