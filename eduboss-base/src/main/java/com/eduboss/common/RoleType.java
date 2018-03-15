package com.eduboss.common;
/**
 * 角色类型
 * @author 段门润
 *
 */
public enum RoleType {
	
	NORMAL("NORMAL", "普通角色"),
	MANAGER("MANAGER", "管理角色");
	
	private String value;
	private String name;
	
	private RoleType(String value, String name) {
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
