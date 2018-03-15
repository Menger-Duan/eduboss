package com.eduboss.common;

public enum RoleResourceType {
	
	MENU("MENU", "菜单"),
	BUTTON("BUTTON", "按钮或功能"),
	ANON_RES("ANON_RES", "无权限"),
	APP("APP", "APP权限");//for the base interface, which don't need the security control, only request login
	
	private String value;
	private String name;
	
	private RoleResourceType(String value, String name) {
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
