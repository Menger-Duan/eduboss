package com.eduboss.domainVo;


import java.util.ArrayList;
import java.util.List;


public class CustomerLessVo {
	private String id;
	private String name;
	private String contact;
	private String resEntranceId; // 资源入口
	private String resEntranceName;
	private String cusType;
	private String cusOrg;
	private String cusTypeName; //资源来源
	private String cusOrgName;   //来源细分



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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getResEntranceId() {
		return resEntranceId;
	}

	public void setResEntranceId(String resEntranceId) {
		this.resEntranceId = resEntranceId;
	}

	public String getResEntranceName() {
		return resEntranceName;
	}

	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}

	public String getCusType() {
		return cusType;
	}

	public void setCusType(String cusType) {
		this.cusType = cusType;
	}

	public String getCusOrg() {
		return cusOrg;
	}

	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}

	public String getCusTypeName() {
		return cusTypeName;
	}

	public void setCusTypeName(String cusTypeName) {
		this.cusTypeName = cusTypeName;
	}

	public String getCusOrgName() {
		return cusOrgName;
	}

	public void setCusOrgName(String cusOrgName) {
		this.cusOrgName = cusOrgName;
	}

}
