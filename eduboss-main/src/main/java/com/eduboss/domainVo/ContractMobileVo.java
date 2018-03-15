package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ContractMobileVo {

	private String contractId;
	private String cusPhone;
	private String cusName;
	
	private String stuGrade;
	private String stuName;
	
	private String signByWho;
	private String signTime;
	
	private BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
	private BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
	private BigDecimal otmClassTotalAmount = BigDecimal.ZERO;
	private BigDecimal otherTotalAmount = BigDecimal.ZERO;
	
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal paidAmount = BigDecimal.ZERO;
	private BigDecimal pendingAmount = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private BigDecimal promotionAmount = BigDecimal.ZERO;
	
	private String contractTypeName;
	private String contractStatusName;
	private String paidStatusName;
	
	private BigDecimal remainingAmount = BigDecimal.ZERO;
	private BigDecimal oneOnOneUnitPrice = BigDecimal.ZERO;
	
    private String blCampusName;
	
	private Set<ContractProductMobileVo> contractProductVos =  new HashSet<ContractProductMobileVo>(); 
	
	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCusPhone() {
		return cusPhone;
	}

	public void setCusPhone(String cusPhone) {
		this.cusPhone = cusPhone;
	}

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public String getStuGrade() {
		return stuGrade;
	}

	public void setStuGrade(String stuGrade) {
		this.stuGrade = stuGrade;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getSignByWho() {
		return signByWho;
	}

	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public BigDecimal getOneOnOneTotalAmount() {
		return oneOnOneTotalAmount;
	}

	public void setOneOnOneTotalAmount(BigDecimal oneOnOneTotalAmount) {
		this.oneOnOneTotalAmount = oneOnOneTotalAmount;
	}

	public BigDecimal getMiniClassTotalAmount() {
		return miniClassTotalAmount;
	}

	public void setMiniClassTotalAmount(BigDecimal miniClassTotalAmount) {
		this.miniClassTotalAmount = miniClassTotalAmount;
	}

	public BigDecimal getOtmClassTotalAmount() {
		return otmClassTotalAmount;
	}

	public void setOtmClassTotalAmount(BigDecimal otmClassTotalAmount) {
		this.otmClassTotalAmount = otmClassTotalAmount;
	}

	public BigDecimal getOtherTotalAmount() {
		return otherTotalAmount;
	}

	public void setOtherTotalAmount(BigDecimal otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
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

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
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

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public BigDecimal getOneOnOneUnitPrice() {
		return oneOnOneUnitPrice;
	}

	public void setOneOnOneUnitPrice(BigDecimal oneOnOneUnitPrice) {
		this.oneOnOneUnitPrice = oneOnOneUnitPrice;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public Set<ContractProductMobileVo> getContractProductVos() {
		return contractProductVos;
	}

	public void setContractProductVos(
			Set<ContractProductMobileVo> contractProductVos) {
		this.contractProductVos = contractProductVos;
	}


}
