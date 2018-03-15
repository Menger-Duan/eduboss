package com.eduboss.common;

public enum AuthFilterLevel {
	
	USER("USER", "用户"),//用户
	ROLE("ROLE", "角色"),//角色
	BRENCH("BRENCH", "分区"),//分区
	CAMPUS("CAMPUS", "校区"),//校区
	DEFAULT("DEFAULT", "默认");//默认
	
	private String value;
	private String name;
	
	private AuthFilterLevel(String value, String name) {
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
