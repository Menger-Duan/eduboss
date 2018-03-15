package com.eduboss.domainVo;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ProductType;


/**
 * ContractProduct entity. 
 */
public class ContractProductStuVo implements java.io.Serializable {


	private String id;
	private String contractId;
	
	
	private String studentId;
	private String studentName;
	private String escClassId;
	private String escClassName;
	
	
	private BigDecimal remainFinance;// 剩余资金
    private BigDecimal remainCourseHour;// 剩余课时
    private Double paidAmount;
    private Double quantity;
    private Double consumeAmount;
	private Double consumeQuanity;
	private BigDecimal realAmount;
	private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private String productionName;
    private Double price;
    
    
    
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
	public String getEscClassId() {
		return escClassId;
	}
	public void setEscClassId(String escClassId) {
		this.escClassId = escClassId;
	}
	public String getEscClassName() {
		return escClassName;
	}
	public void setEscClassName(String escClassName) {
		this.escClassName = escClassName;
	}
	public BigDecimal getRemainFinance() {
		return remainFinance;
	}
	public void setRemainFinance(BigDecimal remainFinance) {
		this.remainFinance = remainFinance;
	}
	public BigDecimal getRemainCourseHour() {
		return remainCourseHour;
	}
	public void setRemainCourseHour(BigDecimal remainCourseHour) {
		this.remainCourseHour = remainCourseHour;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getConsumeAmount() {
		return consumeAmount;
	}
	public void setConsumeAmount(Double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	public Double getConsumeQuanity() {
		return consumeQuanity;
	}
	public void setConsumeQuanity(Double consumeQuanity) {
		this.consumeQuanity = consumeQuanity;
	}
	public BigDecimal getRealAmount() {
		return realAmount;
	}
	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public String getProductionName() {
		return productionName;
	}
	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
}