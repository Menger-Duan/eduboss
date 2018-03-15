package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * CustomerConsultor entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer_consultor")
public class CustomerConsultor implements java.io.Serializable {

	// Fields

	private String id;
	private String customerId;
	private String consultorId;
	private String deliveryTime;
	private Integer isConfirm;
	private String conformTime;

	// Constructors

	/** default constructor */
	public CustomerConsultor() {
	}

	/** minimal constructor */
	public CustomerConsultor(String id, String customerId, String consultorId) {
		this.id = id;
		this.customerId = customerId;
		this.consultorId = consultorId;
	}

	/** full constructor */
	public CustomerConsultor(String id, String customerId, String consultorId,
			String deliveryTime, Integer isConfirm, String conformTime) {
		this.id = id;
		this.customerId = customerId;
		this.consultorId = consultorId;
		this.deliveryTime = deliveryTime;
		this.isConfirm = isConfirm;
		this.conformTime = conformTime;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "CUSTOMER_ID", nullable = false, length = 32)
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CONSULTOR_ID", nullable = false, length = 32)
	public String getConsultorId() {
		return this.consultorId;
	}

	public void setConsultorId(String consultorId) {
		this.consultorId = consultorId;
	}

	@Column(name = "DELIVERY_TIME", length = 20)
	public String getDeliveryTime() {
		return this.deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	@Column(name = "IS_CONFIRM")
	public Integer getIsConfirm() {
		return this.isConfirm;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}

	@Column(name = "CONFORM_TIME", length = 20)
	public String getConformTime() {
		return this.conformTime;
	}

	public void setConformTime(String conformTime) {
		this.conformTime = conformTime;
	}

}