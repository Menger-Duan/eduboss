package com.eduboss.common;


public enum UserType {

	SYS_USER("SYS_USER", "系统用户"), //系统用户
	EMPLOYEE_USER("EMPLOYEE_USER", "员工用户");//员工用户

	private String value;
	private String name;

	private UserType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
