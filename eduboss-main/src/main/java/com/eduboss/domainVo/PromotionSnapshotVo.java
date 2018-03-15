package com.eduboss.domainVo;

import java.math.BigDecimal;


public class PromotionSnapshotVo {
	
	// Fields
	private String id;
	private String name;
	private String promotionType;
	private String promotionValue;
	private String calExpression; //计算公式，用于保存在合同产品中，只作参考
	private BigDecimal settlePromotionAmount = BigDecimal.ZERO; //本次计算优惠金额结果
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	public String getCalExpression() {
		return calExpression;
	}
	public void setCalExpression(String calculationExpression) {
		this.calExpression = calculationExpression;
	}
	public String getPromotionValue() {
		return promotionValue;
	}
	public void setPromotionValue(String promotionValue) {
		this.promotionValue = promotionValue;
	}
	public BigDecimal getSettlePromotionAmount() {
		return settlePromotionAmount;
	}
	public void setSettlePromotionAmount(BigDecimal settlePromotionAmount) {
		this.settlePromotionAmount = settlePromotionAmount;
	}
	
	
	
}
