package com.eduboss.domainVo;

import java.math.BigDecimal;


/**
 * 导出Excel用
 *
 */
public class ContractExcelVo {
	private String contractId;
	private String blCampusName;
	private String stuName;
	private String stuBlCampusName;
	private String contractGrade;
	private String signTime;	
	private BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
	private BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
	private BigDecimal oneOnManyTotalAmount=BigDecimal.ZERO; //一对多收费
	private BigDecimal promiseClassTotalAmount=BigDecimal.ZERO; //目标班收费
	private BigDecimal lectureTotalAmount=BigDecimal.ZERO; //讲座
	private BigDecimal twoTeacherTotalAmount=BigDecimal.ZERO; //双师
	private BigDecimal otherTotalAmount = BigDecimal.ZERO;
//	private BigDecimal freeTotalHour = BigDecimal.ZERO;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal promotionAmount = BigDecimal.ZERO;
	private BigDecimal paidAmount = BigDecimal.ZERO;
	private BigDecimal pendingAmount = BigDecimal.ZERO;
	private BigDecimal availableAmount = BigDecimal.ZERO; // 待分配金额
	private String contractTypeName;
	private String contractStatusName;
	private String paidStatusName;
	private String signByWho;
	private String saleChannelName;



	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuBlCampusName() {
		return stuBlCampusName;
	}
	public void setStuBlCampusName(String stuBlCampusName) {
		this.stuBlCampusName = stuBlCampusName;
	}
	public String getContractGrade() {
		return contractGrade;
	}
	public void setContractGrade(String contractGrade) {
		this.contractGrade = contractGrade;
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
	public BigDecimal getOneOnManyTotalAmount() {
		 return oneOnManyTotalAmount;
	}
	public void setOneOnManyTotalAmount(BigDecimal oneOnManyTotalAmount) {
		this.oneOnManyTotalAmount = oneOnManyTotalAmount;
	}
	public BigDecimal getPromiseClassTotalAmount() {
			return promiseClassTotalAmount;
	}
	public void setPromiseClassTotalAmount(BigDecimal promiseClassTotalAmount) {
		this.promiseClassTotalAmount = promiseClassTotalAmount;
	}
	public BigDecimal getOtherTotalAmount() {
		return otherTotalAmount;
	}
	public void setOtherTotalAmount(BigDecimal otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
	}
//	public BigDecimal getFreeTotalHour() {
//		return freeTotalHour;
//	}
//	public void setFreeTotalHour(BigDecimal freeTotalHour) {
//		this.freeTotalHour = freeTotalHour;
//	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
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
	public String getSignByWho() {
		return signByWho;
	}
	public void setSignByWho(String signByWho) {
		this.signByWho = signByWho;
	}
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getLectureTotalAmount() {
		return lectureTotalAmount;
	}

	public void setLectureTotalAmount(BigDecimal lectureTotalAmount) {
		this.lectureTotalAmount = lectureTotalAmount;
	}

	public BigDecimal getTwoTeacherTotalAmount() {
		return twoTeacherTotalAmount;
	}

	public void setTwoTeacherTotalAmount(BigDecimal twoTeacherTotalAmount) {
		this.twoTeacherTotalAmount = twoTeacherTotalAmount;
	}

    public String getSaleChannelName() {
        return saleChannelName;
    }
    public void setSaleChannelName(String saleChannelName) {
        this.saleChannelName = saleChannelName;
    }

}
