package com.eduboss.domainVo;

import com.eduboss.domain.Organization;
import com.eduboss.domain.OrganizationFile;

import java.util.List;

public class OrganizationAppendDto {

	private String id;
	private String provinceId;
	private String cityId;
	private String contact;
	private String contactUser;
	private String address;
	private String lat;
	private String lon;
	private String trafficInfo;
	private Integer hasAppend;
	private String name;
	private String remark;
	private String modifyType;

	private List<OrganizationFile> fileList;
	
	private ResourcePoolVo resourcePoolVo;

	public List<OrganizationFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<OrganizationFile> fileList) {
		this.fileList = fileList;
	}

	public String getModifyType() {
		return modifyType;
	}

	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactUser() {
		return contactUser;
	}

	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getTrafficInfo() {
		return trafficInfo;
	}

	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}

	public Integer getHasAppend() {
		return hasAppend;
	}

	public void setHasAppend(Integer hasAppend) {
		this.hasAppend = hasAppend;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setNormalInfo(Organization o ){
		o.setName(this.name);
		o.setRemark(this.remark);
	}

	public void setAppendInfo(Organization o ){
		o.setProvinceId(this.provinceId);
		o.setCityId(this.cityId);
		o.setContact(this.contact);
		o.setContactUser(this.contactUser);
		o.setAddress(this.address);
		o.setLat(this.lat);
		o.setLon(this.lon);
		o.setTrafficInfo(this.trafficInfo);
		o.setHasAppend(this.hasAppend);
	}

	public ResourcePoolVo getResourcePoolVo() {
		return resourcePoolVo;
	}

	public void setResourcePoolVo(ResourcePoolVo resourcePoolVo) {
		this.resourcePoolVo = resourcePoolVo;
	}
	
	
}
