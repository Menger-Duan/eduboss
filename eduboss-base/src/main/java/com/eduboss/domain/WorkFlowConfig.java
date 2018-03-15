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

import com.eduboss.common.RoleCode;

/**
 * 工作流配置
 */
@Entity
@Table(name = "WORK_FLOW_CONFIG")
public class WorkFlowConfig implements java.io.Serializable {

	// Fields
	private String id; // 逻辑ID
	private int order; // 序号
	private String type; // 工作流类型
	private String action; // 动作
	private String description; // 简介
	private User createUser; // 创建人
	private String createTime; // 创建时间
	private RoleCode belongRole; // 归属角色
	private User belongUser; // 归属用户

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

	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "ORDER")
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Column(name = "BELONG_ROLE_CODE")
	public RoleCode getBelongRole() {
		return belongRole;
	}

	public void setBelongRole(RoleCode belongRole) {
		this.belongRole = belongRole;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BELONG_USER_ID")
	public User getBelongUser() {
		return belongUser;
	}

	public void setBelongUser(User belongUser) {
		this.belongUser = belongUser;
	}

	@Column(name = "ACTION")
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}