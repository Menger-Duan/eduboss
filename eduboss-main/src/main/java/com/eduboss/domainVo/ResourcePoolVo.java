package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.ValidStatus;
import com.eduboss.domain.ResourcePoolRole;

public class ResourcePoolVo {
	private String organizationId;
	private String name;
	private ValidStatus status; //状态
	private String cycleType; // 0:不回收，1：可回收
	private BigDecimal capacity; //容量
	private String returnNote; //回流节点
	private BigDecimal recoveyPeriod; //回收周期
	
	List<ResourcePoolRoleVo> roleVos;
	
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ValidStatus getStatus() {
		return status;
	}
	public void setStatus(ValidStatus status) {
		this.status = status;
	}
	public String getCycleType() {
		return cycleType;
	}
	public void setCycleType(String cycleType) {
		this.cycleType = cycleType;
	}
	public BigDecimal getCapacity() {
		return capacity;
	}
	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}
	public String getReturnNote() {
		return returnNote;
	}
	public void setReturnNote(String returnNote) {
		this.returnNote = returnNote;
	}
	public BigDecimal getRecoveyPeriod() {
		return recoveyPeriod;
	}
	public void setRecoveyPeriod(BigDecimal recoveyPeriod) {
		this.recoveyPeriod = recoveyPeriod;
	}
	public List<ResourcePoolRoleVo> getRoleVos() {
		return roleVos;
	}
	public void setRoleVos(List<ResourcePoolRoleVo> roleVos) {
		this.roleVos = roleVos;
	}
	
	
	
}
