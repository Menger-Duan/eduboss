package com.eduboss.domainVo;

import java.math.BigDecimal;

public class OdsPaymentRecieptModifyVo {
	private String id;
	private String type;
	private BigDecimal amount;
	private String remark;
	private String createUser;
	private String createTime;
	private String modifyUser;
	private String modifyTime;
	private String receiptMainId;
	private String mainRemark;
	private String typeName;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getReceiptMainId() {
		return receiptMainId;
	}
	public void setReceiptMainId(String receiptMainId) {
		this.receiptMainId = receiptMainId;
	}
	public String getMainRemark() {
		return mainRemark;
	}
	public void setMainRemark(String mainRemark) {
		this.mainRemark = mainRemark;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
