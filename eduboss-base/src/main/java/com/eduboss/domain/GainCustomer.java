package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="gain_customer")
public class GainCustomer {
	private String id;
	private Customer cusId;
	private String deliverFrom;//
	private String deliverTarget; //客户分配目标  就是分配给谁
	private String reason;
	private String createUser;
	private String createTime;
	
	private Boolean visitCome;//是否已经 上门
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUS_ID")
	public Customer getCusId() {
		return cusId;
	}
	public void setCusId(Customer cusId) {
		this.cusId = cusId;
	}
	
	@Column(name = "DELIVERTARGET", length = 32)
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	
	@Column(name = "REASON", length = 1000)
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Column(name = "CREATE_USER", length = 32)
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "DELIVERFROM", length = 32)
	public String getDeliverFrom() {
		return deliverFrom;
	}
	public void setDeliverFrom(String deliverFrom) {
		this.deliverFrom = deliverFrom;
	}
	
	@Transient
	public Boolean getVisitCome() {
		return visitCome;
	}
	public void setVisitCome(Boolean visitCome) {
		this.visitCome = visitCome;
	}
	
	
	

}

