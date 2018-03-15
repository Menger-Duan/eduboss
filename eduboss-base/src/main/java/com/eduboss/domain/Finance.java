package com.eduboss.domain;

import java.math.BigDecimal;

public class Finance implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String countDate;
	private BigDecimal countPaidTotalAmount;
	private BigDecimal countPaidCashAmountNew;
	private BigDecimal countPaidCashAmountPre;
	private int sort;
	private BigDecimal targetValue;
	private BigDecimal returnFee;
	private String createTime;
	private String modifyTime;
	private BigDecimal onlineAmount;
	private BigDecimal lineAmount;

	public Finance() {
		countPaidTotalAmount=BigDecimal.ZERO;
		countPaidCashAmountNew=BigDecimal.ZERO;
		countPaidCashAmountPre=BigDecimal.ZERO;
		onlineAmount=BigDecimal.ZERO;
		lineAmount=BigDecimal.ZERO;
		targetValue=BigDecimal.ZERO;
		returnFee=BigDecimal.ZERO;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	public String getCountDate() {
		return countDate;
	}


	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public BigDecimal getCountPaidTotalAmount() {
		return countPaidTotalAmount;
	}


	public void setCountPaidTotalAmount(BigDecimal countPaidTotalAmount) {
		this.countPaidTotalAmount = countPaidTotalAmount;
	}

	public BigDecimal getCountPaidCashAmountNew() {
		return countPaidCashAmountNew;
	}


	public void setCountPaidCashAmountNew(BigDecimal countPaidCashAmountNew) {
		this.countPaidCashAmountNew = countPaidCashAmountNew;
	}

	public BigDecimal getCountPaidCashAmountPre() {
		return countPaidCashAmountPre;
	}


	public void setCountPaidCashAmountPre(BigDecimal countPaidCashAmountPre) {
		this.countPaidCashAmountPre = countPaidCashAmountPre;
	}

	public int getSort() {
		return sort;
	}


	public void setSort(int sort) {
		this.sort = sort;
	}

	public BigDecimal getTargetValue() {
		return targetValue;
	}


	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	public BigDecimal getReturnFee() {
		return returnFee;
	}


	public void setReturnFee(BigDecimal returnFee) {
		this.returnFee = returnFee;
	}


	public BigDecimal getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(BigDecimal onlineAmount) {
		this.onlineAmount = onlineAmount;
	}

	public BigDecimal getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(BigDecimal lineAmount) {
		this.lineAmount = lineAmount;
	}
}
