package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "transfer_customer")
public class TransferCustomerCampus {
	private String id;
	private String cusId;
	private String transferCampus; //转出校区
	private String transferTime; //转出时间
	private String receiveCampus; //接收校区
	private String receiveTime; //接收时间
	private String deliverTargetFrom;//原来的分配目标
	private String delivetTargetTo;//将要分配到的分配目标
	
	private String createuser; 
	private String createTime;
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	@Column(name = "TRANSFER_CAMPUS", length = 32)
	public String getTransferCampus() {
		return transferCampus;
	}
	public void setTransferCampus(String transferCampus) {
		this.transferCampus = transferCampus;
	}
	
	@Column(name = "TRANSFER_TIME", length = 20)
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	
	@Column(name = "RECEIVE_CAMPUS", length = 32)
	public String getReceiveCampus() {
		return receiveCampus;
	}
	public void setReceiveCampus(String receiveCampus) {
		this.receiveCampus = receiveCampus;
	}
	
	@Column(name = "RECEIVE_TIME", length = 20)
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	@Column(name = "CREATE_USER", length = 32)
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	@Column(name = "DELIVER_TARGET_FROM", length = 32)
	public String getDeliverTargetFrom() {
		return deliverTargetFrom;
	}
	public void setDeliverTargetFrom(String deliverTargetFrom) {
		this.deliverTargetFrom = deliverTargetFrom;
	}
	@Column(name = "DELIVER_TARGET_TO", length = 32)
	public String getDelivetTargetTo() {
		return delivetTargetTo;
	}
	public void setDelivetTargetTo(String delivetTargetTo) {
		this.delivetTargetTo = delivetTargetTo;
	}
	public TransferCustomerCampus(String cusId, String transferCampus, String receiveCampus){
		this.cusId = cusId;
		this.transferCampus = transferCampus;
		this.receiveCampus = receiveCampus;
	}
	
	public TransferCustomerCampus(){}
	

}

