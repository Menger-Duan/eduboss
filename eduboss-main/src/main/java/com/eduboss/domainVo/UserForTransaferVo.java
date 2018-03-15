package com.eduboss.domainVo;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class UserForTransaferVo {

	// Fields
	private String userId;
	private String name;
	private String contact;
	private String account;
	private String employeeNo;
	private String campusId;//校区ID
	private String campus;//所在校区
	private String brenchId;//分公司ID
	private String brench;//所在分公司
	private String group;//所在集团
	private String groupId;//所在集团id
	
	private List<Map<String, Object>> campusAreas;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampus() {
		return campus;
	}
	public void setCampus(String campus) {
		this.campus = campus;
	}
	public String getBrenchId() {
		return brenchId;
	}
	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}
	public String getBrench() {
		return brench;
	}
	public void setBrench(String brench) {
		this.brench = brench;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getEmployeeNo() {
		return employeeNo;
	}
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
	public List<Map<String, Object>> getCampusAreas() {
		return campusAreas;
	}
	public void setCampusAreas(List<Map<String, Object>> campusAreas) {
		this.campusAreas = campusAreas;
	}
	
	
}
