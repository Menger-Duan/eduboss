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

/**
 * StudentOrganization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "mini_class_product")
public class MiniClassProduct implements java.io.Serializable {

	// Fields
	private String id;
	private MiniClass miniClass;
	private Product product;
	private String isMainProduct; // 是否为主产品(1：是；0：否)
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;

	// Constructors

	/** default constructor */
	public MiniClassProduct() {
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_ID")
	public MiniClass getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "IS_MAIN_PRODUCT", length = 1)
	public String getIsMainProduct() {
		return isMainProduct;
	}

	public void setIsMainProduct(String isMainProduct) {
		this.isMainProduct = isMainProduct;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

}