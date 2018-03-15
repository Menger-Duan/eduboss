package com.eduboss.common;

public enum UpdateType {
	
	NEW("NEW", "新建"),//新建
	UPDATE("UPDATE", "修改"),//修改
	CHARGE("CHARGE", "收费"),//收费
	UNVALID("UNVALID", "关闭");//关闭
	
	private String value;
	private String name;
	
	private UpdateType(String value, String name) {
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
