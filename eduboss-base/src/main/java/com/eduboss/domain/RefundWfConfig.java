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
 * RefundContractWf entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REFUND_WF_CONFIG")
public class RefundWfConfig implements java.io.Serializable {

	// Fields
	private String id;
	private int order;
	private String action;
	private String actionLevel;
	
	private User createUser;
	private User modifyUser;
	private String createTime;
	private String modifyTime;
	
	// Constructors

	/** default constructor */
	public RefundWfConfig() {
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

	@Column(name = "ACTION_LEVEL", length = 32)
	public String getActionLevel() {
		return this.actionLevel;
	}

	public void setActionLevel(String actionLevel) {
		this.actionLevel = actionLevel;
	}

	@Column(name = "ACTION", length = 32)
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "ORDER", precision = 10)
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}
	
	@Column(name = "CREATE_TIME", length = 32)
	public String getCreateTime() {
		return this.createTime;
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
	
	@Column(name = "MODIFY_TIME", length = 32)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

}