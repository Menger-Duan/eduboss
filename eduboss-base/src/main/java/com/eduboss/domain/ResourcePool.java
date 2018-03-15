package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.ValidStatus;

/**
 * 
 * @author lixuejun
 *
 */
@Entity
@Table(name="resource_pool")
public class ResourcePool {

	// field
	private String organizationId; //组织架构ID
	private String name; //资源池名称
	private ValidStatus status; //状态
	private String cycleType; // 0:不回收，1：可回收
//	private DataDict transferMode; //流转方式
	private BigDecimal capacity; //容量
	private String returnNote; //回流节点
	private BigDecimal recoveyPeriod; //回收周期
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	public ResourcePool() {
		super();
	}

	public ResourcePool(String organizationId, String name, ValidStatus status,
			String cycleType, BigDecimal capacity, String returnNote,
			BigDecimal recoveyPeriod, String createUserId, String createTime,
			String modifyUserId, String modifyTime) {
		super();
		this.organizationId = organizationId;
		this.name = name;
		this.status = status;
		this.cycleType = cycleType;
		this.capacity = capacity;
		this.returnNote = returnNote;
		this.recoveyPeriod = recoveyPeriod;
		this.createUserId = createUserId;
		this.createTime = createTime;
		this.modifyUserId = modifyUserId;
		this.modifyTime = modifyTime;
	}

	@Id
	@Column(name = "ORGANIZATION_ID", unique = true, nullable = false, length = 32)
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name="name", length=50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="STATUS")
	public ValidStatus getStatus() {
		return status;
	}

	public void setStatus(ValidStatus status) {
		this.status = status;
	}

	@Column(name = "CYCLE_TYPE", length = 32)
	public String getCycleType() {
		return cycleType;
	}

	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}

	@Column(name = "CAPACITY", precision = 4)
	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	@Column(name="RETURN_NOTE", length=32)
	public String getReturnNote() {
		return returnNote;
	}

	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}

	@Column(name = "RECOVEY_PERIOD", precision = 4)
	public BigDecimal getRecoveyPeriod() {
		return recoveyPeriod;
	}

	public void setRecoveyPeriod(BigDecimal recoveyPeriod) {
		this.recoveyPeriod = recoveyPeriod;
	}

	@Column(name="CREATE_USER_ID", length=32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name="CREATE_TIME", length=20)
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

	@Column(name="MODIFY_TIME", length=20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
