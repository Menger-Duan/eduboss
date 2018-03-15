package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.eduboss.common.ProductType;

public class RefundContractProductVo {
	
	private String id;
	private ContractProductVo contractProduct;
	private String contractProductId;
	private RefundContractVo refundContract;
	private ContractVo Contract;
	private BigDecimal price;
	private BigDecimal quantity;
	private BigDecimal dealDiscount;
	private BigDecimal refundAmount;
	private Timestamp createTime;
	private String status;
	private ProductType refundProductType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ContractProductVo getContractProduct() {
		return contractProduct;
	}
	public void setContractProduct(ContractProductVo contractProduct) {
		this.contractProduct = contractProduct;
	}
	public RefundContractVo getRefundContract() {
		return refundContract;
	}
	public void setRefundContract(RefundContractVo refundContract) {
		this.refundContract = refundContract;
	}
	public ContractVo getContract() {
		return Contract;
	}
	public void setContract(ContractVo contract) {
		Contract = contract;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getDealDiscount() {
		return dealDiscount;
	}
	public void setDealDiscount(BigDecimal dealDiscount) {
		this.dealDiscount = dealDiscount;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ProductType getRefundProductType() {
		return refundProductType;
	}
	public void setRefundProductType(ProductType refundProductType) {
		this.refundProductType = refundProductType;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	
}
