package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "INVENTORY_RECORD")
public class InventoryRecord implements java.io.Serializable{
	
	private String id;
	
	private InventoryProduct inventoryProduct;
	
	private Organization organization;
	
	private double price;
	
	private double number;
	
	private String newInventoryTime;
	
	private User createUser;
	
	private String createTime;
	
	private User modifyUser;
	
	private String modifyTime;

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
	@JoinColumn(name = "INVENTORY_PRODUCT_ID")
	public InventoryProduct getInventoryProduct() {
		return inventoryProduct;
	}

	public void setInventoryProduct(InventoryProduct inventoryProduct) {
		this.inventoryProduct = inventoryProduct;
	}

	@Column(name = "PRICE")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Column(name = "NUMBER")
	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATION")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "NEW_INVENTORY_TIME")
	public String getNewInventoryTime() {
		return newInventoryTime;
	}

	public void setNewInventoryTime(String newInventoryTime) {
		this.newInventoryTime = newInventoryTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREAET_USER_ID")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	} 
	
	

}
