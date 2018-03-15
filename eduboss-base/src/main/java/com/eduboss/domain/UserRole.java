package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user_role")
public class UserRole implements java.io.Serializable {

	// Fields

	private int id;
	private String userId;
	private String roleId;
	private Boolean isMajorRole;


	public UserRole() {
	}

	public UserRole(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "isMajorRole")
	public Boolean getIsMajorRole() {
		return this.isMajorRole;
	}

	public void setIsMajorRole(Boolean isMajorRole) {
		this.isMajorRole = isMajorRole;
	}

	@Column(name = "userID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "roleID", nullable = false, length = 32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}