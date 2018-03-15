package com.eduboss.domainVo;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ValidStatus;
import com.eduboss.domain.Region;

public class OrganizationVo {
	
	private String id;
    private String name;
    private String parentId;
    private String regionId;//归属地区
    private String remark;
    private String orgLevel;
    private Integer orgOrder;
    private OrganizationType orgType;
    
//    private String provinceId;
    private String address;//地址
	private String lon; // 经度
	private String lat;  //纬度
    private String contact;//电话
    private String belong;//统筹归属
    
    private String customerPoolName;//CUSTOMER_POOL_NAME
    private String isPublicPool;//IS_PUBLIC_POOL
    private String accessRoles;//ACCESS_ROLES
    
    // 资源池配置 新逻辑
    private String resourcePoolName;// 资源池名称
    private ValidStatus resourcePoolstatus; //状态 (有效，无效)
    
    private String orgSign;//ORG_SIGN 组织标识
    
	private String provinceId;	//省份
	private String cityId;	//城市
	private String areaId;	//地区


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

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	public Integer getOrgOrder() {
		return orgOrder;
	}

	public void setOrgOrder(Integer orgOrder) {
		this.orgOrder = orgOrder;
	}

	public OrganizationType getOrgType() {
		return orgType;
	}

	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getCustomerPoolName() {
		return customerPoolName;
	}

	public void setCustomerPoolName(String customerPoolName) {
		this.customerPoolName = customerPoolName;
	}

	public String getIsPublicPool() {
		return isPublicPool;
	}

	public void setIsPublicPool(String isPublicPool) {
		this.isPublicPool = isPublicPool;
	}

	public String getAccessRoles() {
		return accessRoles;
	}

	public void setAccessRoles(String accessRoles) {
		this.accessRoles = accessRoles;
	}

	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public ValidStatus getResourcePoolstatus() {
		return resourcePoolstatus;
	}

	public void setResourcePoolstatus(ValidStatus resourcePoolstatus) {
		this.resourcePoolstatus = resourcePoolstatus;
	}

	public String getOrgSign() {
		return orgSign;
	}

	public void setOrgSign(String orgSign) {
		this.orgSign = orgSign;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}  
	
}
