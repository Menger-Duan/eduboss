package com.eduboss.domain;

import javax.persistence.*;

/**
 * RoleResource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "role_resource")
public class RoleResource implements java.io.Serializable {

	// Fields

	private int id;

	private String roleId;
	private String resourceId;

	// Constructors

	/** default constructor */
	public RoleResource() {
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

	@Column(name = "roleID", nullable = false, length = 32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "resourceID", nullable = false, length = 32)
	public String getResourceId() {
		return this.resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}