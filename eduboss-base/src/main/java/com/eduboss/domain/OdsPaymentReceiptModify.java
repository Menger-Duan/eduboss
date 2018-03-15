package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ods_payment_receipt_modify")
public class OdsPaymentReceiptModify implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private DataDict type;
	private BigDecimal amount;
	private String remark;
	private String createUser;
	private String createTime;
	private String modifyUser;
	private String modifyTime;
	private String receiptMainId;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type")
	public DataDict getType() {
		return type;
	}
	public void setType(DataDict type) {
		this.type = type;
	}
	
	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "create_user")
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "modify_user")
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	@Column(name = "modify_time")
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name = "receipt_main_id")
	public String getReceiptMainId() {
		return receiptMainId;
	}
	public void setReceiptMainId(String receiptMainId) {
		this.receiptMainId = receiptMainId;
	}
	
}
