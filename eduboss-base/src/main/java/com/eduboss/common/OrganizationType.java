package com.eduboss.common;

public enum OrganizationType {
	
	OTHER("OTHER", "其他"),//其他
	DEPARTMENT("DEPARTMENT", "部门"),//部门
	CAMPUS("CAMPUS", "校区"),//校区
	BRENCH("BRENCH", "分公司"),//分公司
	GROUNP("GROUNP", "集团"),//集团
	PARGANA("PARGANA", "大区");//大区
	
	private String value;
	private String name;
	
	private OrganizationType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
