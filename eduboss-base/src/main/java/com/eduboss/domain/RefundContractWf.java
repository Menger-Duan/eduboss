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
@Table(name = "REFUND_CONTRACT_WF")
public class RefundContractWf implements java.io.Serializable {

	// Fields
	private String id;
	private User user;
	private RefundContract refundContract;
	private String actionLevel;
	private String createTime;
	private String action;

	// Constructors

	/** default constructor */
	public RefundContractWf() {
	}

	/** minimal constructor */
	public RefundContractWf(RefundContract refundContract) {
		this.refundContract = refundContract;
	}

	/** full constructor */
	public RefundContractWf(User user, RefundContract refundContract, String actionLevel, String createTime, String action) {
		this.user = user;
		this.refundContract = refundContract;
		this.actionLevel = actionLevel;
		this.createTime = createTime;
		this.action = action;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RERUND_CONTRACT_ID")
	public RefundContract getRefundContract() {
		return this.refundContract;
	}

	public void setRefundContract(RefundContract refundContract) {
		this.refundContract = refundContract;
	}

	@Column(name = "ACTION_LEVEL", length = 32)
	public String getActionLevel() {
		return this.actionLevel;
	}

	public void setActionLevel(String actionLevel) {
		this.actionLevel = actionLevel;
	}

	@Column(name = "CREATE_TIME", length = 32)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ACTION")
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}