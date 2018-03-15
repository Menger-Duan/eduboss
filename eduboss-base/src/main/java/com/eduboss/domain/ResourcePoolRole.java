package com.eduboss.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author duanmenrun
 *
 */
@Entity
@Table(name="resource_pool_role")
public class ResourcePoolRole implements Serializable {

	private int  id;
	private BigDecimal oneTimeResource;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String organizationId;
	private String roleId;
	
	private Role role; 
	
	//Contructors
	public ResourcePoolRole() {

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID", nullable = false, insertable = false, updatable = false)	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Column(name = "ONE_TIME_RESOURCE", precision = 4)
	public BigDecimal getOneTimeResource() {
		return oneTimeResource;
	}
	
	public void setOneTimeResource(BigDecimal oneTimeResource) {
		this.oneTimeResource = oneTimeResource;
	}
	
	@Column(name="CREATE_USER_ID", length=32)
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", columnDefinition="timestamp", insertable = false, updatable = false)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name="MODIFY_USER_ID", length=32)
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_TIME", columnDefinition="timestamp", insertable = false, updatable = false)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "ORGANIZATION_ID", nullable = false, length = 32)
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "ROLE_ID", nullable = false, length = 32)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
