package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.ResourcePoolJobType;

public class ResourcePoolJobVo {
	private int id;
	private String organizationId;
	private String userJobId;
	private String userJobName;
	private ResourcePoolJobType type;
	private BigDecimal oneTimeResource;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getUserJobId() {
		return userJobId;
	}
	public void setUserJobId(String userJobId) {
		this.userJobId = userJobId;
	}
	public String getUserJobName() {
		return userJobName;
	}
	public void setUserJobName(String userJobName) {
		this.userJobName = userJobName;
	}
	public ResourcePoolJobType getType() {
		return type;
	}
	public void setType(ResourcePoolJobType type) {
		this.type = type;
	}
	public BigDecimal getOneTimeResource() {
		return oneTimeResource;
	}
	public void setOneTimeResource(BigDecimal oneTimeResource) {
		this.oneTimeResource = oneTimeResource;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
