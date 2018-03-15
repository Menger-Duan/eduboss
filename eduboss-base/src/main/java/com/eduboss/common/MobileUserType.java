package com.eduboss.common;



public enum MobileUserType {
	
	// 增加 管理 
	MANAGE("MANAGE", "管理"),//管理
	STAFF_USER("STAFF_USER", "员工"),//员工
	STUDENT_USER("STUDENT_USER", "学生");// 学生
	private String value;
	private String name;
	
	private MobileUserType(String value, String name) {
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
