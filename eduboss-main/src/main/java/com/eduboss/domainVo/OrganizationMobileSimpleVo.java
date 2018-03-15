package com.eduboss.domainVo;

public class OrganizationMobileSimpleVo {

	private String orgId;
	private String name;
	private String orgT;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrgT() {
		return orgT;
	}
	public void setOrgT(String orgT) {
		this.orgT = orgT;
	}
	public OrganizationMobileSimpleVo(String orgId, String name,String orgT) {
		this.orgId = orgId;
		this.name = name;
		this.orgT=orgT;
	}
	public OrganizationMobileSimpleVo() {
	}
	
}
