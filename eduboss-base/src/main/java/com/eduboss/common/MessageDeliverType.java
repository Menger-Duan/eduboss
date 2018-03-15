package com.eduboss.common;

public enum MessageDeliverType {
	
	SINGLE("SINGLE", "个人"),//个人
	LIST_USER("LIST_USER", "多个人"),//多个人
	DEPARTMENT("DEPARTMENT", "部门"),//部门
	CAMPUS("CAMPUS", "校区"),//校区
	BRENCH("BRENCH", "分公司"),//分公司
	ALL("ALL", "所有人");//所有人
	
	private String value;
	private String name;
	
	private MessageDeliverType(String value, String name) {
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
