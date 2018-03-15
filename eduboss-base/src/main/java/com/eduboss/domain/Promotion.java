package com.eduboss.domain;

import java.math.BigDecimal;

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

import com.eduboss.common.PromotionType;

/**
 * Promotion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PROMOTION")
public class Promotion implements java.io.Serializable {

	// Fields
	private String id;
	private String name;
	private Organization organization;
	private PromotionType promotionType;
	private BigDecimal promotionValue;
	private String data;
	private String startTime;
	private String endTime;
	private String isActive;
	private String accessRoles;
	private User createUser;
	private String createTime;
	private User modifyUser;
	private String modifyTime;

	// Constructors

	/** default constructor */
	public Promotion() {
	}

	/** minimal constructor */
	public Promotion(String id) {
		this.id = id;
	}

	/** full constructor */
	public Promotion(String id, String name, PromotionType promotionType,
			BigDecimal promotionValue, String data, String startTime,
			String endTime, User createUserId, String createTime,
			User modifyUserId, String modifyTime) {
		this.id = id;
		this.name = name;
		this.promotionType = promotionType;
		this.promotionValue = promotionValue;
		this.data = data;
		this.startTime = startTime;
		this.endTime = endTime;
		this.createUser = createUserId;
		this.createTime = createTime;
		this.modifyUser = modifyUserId;
		this.modifyTime = modifyTime;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PROMOTION_TYPE", length = 32)
	public PromotionType getPromotionType() {
		return this.promotionType;
	}

	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PROMOTION_VALUE", precision = 10)
	public BigDecimal getPromotionValue() {
		return this.promotionValue;
	}

	public void setPromotionValue(BigDecimal promotionValue) {
		this.promotionValue = promotionValue;
	}

	@Column(name = "DATA", length = 1024)
	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "START_TIME", length = 20)
	public String getStartTime() {
		return this.startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "END_TIME", length = 20)
	public String getEndTime() {
		return this.endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATION_ID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Column(name = "IS_ACTIVE", length = 20)
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "ACCESS_ROLES", length = 20)
	public String getAccessRoles() {
		return accessRoles;
	}

	public void setAccessRoles(String accessRoles) {
		this.accessRoles = accessRoles;
	}

}