package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.InventoryOperateType;


@Entity
@Table(name = "INVENTORY_MANAGER")
public class InventoryManager implements java.io.Serializable{
	
	private String id;
	
	//private InventoryProduct inventoryProduct;
	
	//private double price;
	
	private double number;
	
	//private String targetInventoryManagerId;
	
	private InventoryRecord resourceInventory;
	
	private InventoryRecord targetInventory;
	
	private InventoryOperateType operationType;
	
	private User consumeUser;
	
	private Student student;
	
	private User createUser;
	
	private String createTime;
	

	
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

	@Column(name = "NUMBER")
	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_INVENTORY_ID")
	public InventoryRecord getResourceInventory() {
		return resourceInventory;
	}

	public void setResourceInventory(InventoryRecord resourceInventory) {
		this.resourceInventory = resourceInventory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_INVENTORY_ID")
	public InventoryRecord getTargetInventory() {
		return targetInventory;
	}

	public void setTargetInventory(InventoryRecord targetInventory) {
		this.targetInventory = targetInventory;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "OPERATION_TYPE")
	public InventoryOperateType getOperationType() {
		return operationType;
	}

	public void setOperationType(InventoryOperateType operationType) {
		this.operationType = operationType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONSUME_USER_ID")
	public User getConsumeUser() {
		return consumeUser;
	}

	public void setConsumeUser(User consumeUser) {
		this.consumeUser = consumeUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	

}
