package com.eduboss.domainVo;

public class OrganizationInfoVo {
	
	private String id;
	private String name;
	private String parentId;
	private String orgType;
	private String levelNo;
	private String orgOrder;
	private String regionId;
	private String regionName;	
	
    public OrganizationInfoVo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getLevelNo() {
		return levelNo;
	}

	public void setLevelNo(String levelNo) {
		this.levelNo = levelNo;
	}

	public String getOrgOrder() {
		return orgOrder;
	}

	public void setOrgOrder(String orgOrder) {
		this.orgOrder = orgOrder;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}    
}
