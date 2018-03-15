package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ProductType;

/**
 * ProductCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String parentId;
	private String catLevel;
	private Integer catOrder;
	private ProductType productType;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String remark;

	// Constructors

	/** default constructor */
	public ProductCategory() {
	}

	/** full constructor */
	public ProductCategory(String name, String parentId, String catLevel, Integer catOrder, ProductType productType, String remark) {
		this.name = name;
		this.parentId = parentId;
		this.catLevel = catLevel;
		this.catOrder = catOrder;
		this.productType = productType;
		this.remark = remark;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "PARENT_ID", length = 32)
	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Column(name = "CAT_LEVEL", length = 32)
	public String getCatLevel() {
		return this.catLevel;
	}

	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}

	@Column(name = "CAT_ORDER")
	public Integer getCatOrder() {
		return this.catOrder;
	}

	public void setCatOrder(Integer catOrder) {
		this.catOrder = catOrder;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PRODUCT_TYPE", length = 32)
	public ProductType getProductType() {
		return this.productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}