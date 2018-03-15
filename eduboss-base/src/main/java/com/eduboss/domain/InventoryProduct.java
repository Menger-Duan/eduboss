package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "INVENTORY_PRODUCT")
public class InventoryProduct implements java.io.Serializable{
	
	private String id;
	
	private String name;
	
	private DataDict inventoryUnit;
	
	private DataDict inventoryType;
	
	private User createUser;
	
	private String createTime;
	
	private User modifyUser;
	
	private String modifyTime;

	
	@Id
//	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INVENTORY_UNIT")
	public DataDict getInventoryUnit() {
		return inventoryUnit;
	}

	public void setInventoryUnit(DataDict inventoryUnit) {
		this.inventoryUnit = inventoryUnit;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INVENTORY_TYPE")
	public DataDict getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(DataDict inventoryType) {
		this.inventoryType = inventoryType;
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
