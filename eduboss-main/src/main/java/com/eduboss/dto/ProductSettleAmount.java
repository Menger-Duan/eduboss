package com.eduboss.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.eduboss.domainVo.PromotionSnapshotVo;

public class ProductSettleAmount {
	
	private BigDecimal promotionAmount = BigDecimal.ZERO; //优惠金额
	private BigDecimal realPayAmount = BigDecimal.ZERO; //实收金额
	private BigDecimal totalAmount = BigDecimal.ZERO; //实得总金额
	
	private List<PromotionSnapshotVo> promotionVos = new ArrayList<PromotionSnapshotVo>(); //计算所用的优惠列表
	
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}
	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}
	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<PromotionSnapshotVo> getPromotionVos() {
		return promotionVos;
	}
	public void setPromotionVos(List<PromotionSnapshotVo> promotionVos) {
		this.promotionVos = promotionVos;
	}
	
}
