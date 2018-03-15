package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.ProductType;

public class ContractProductMobileVo {

	private Double price;
	private String productionName;
	private ProductType productType;
	
	private Double quantity;
	private Double dealDiscount;
	private Double paidAmount;
	private String statusName;
	private String paidStatusName;
	
	private String promotionJson ;
	private BigDecimal promotionAmount;
	
	private BigDecimal realAmount;
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getProductionName() {
		return productionName;
	}
	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	public Double getDealDiscount() {
		return dealDiscount;
	}
	public void setDealDiscount(Double dealDiscount) {
		this.dealDiscount = dealDiscount;
	}
	public Double getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getPaidStatusName() {
		return paidStatusName;
	}
	public void setPaidStatusName(String paidStatusName) {
		this.paidStatusName = paidStatusName;
	}
	public String getPromotionJson() {
		return promotionJson;
	}
	public void setPromotionJson(String promotionJson) {
		this.promotionJson = promotionJson;
	}
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}
	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
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
	private BigDecimal totalAmount;
    
}
