package com.eduboss.common;

public enum ChargePayType {
	
	CHARGE("CHARGE", "消耗"),//收款
	WASH("WASH", "冲销");//冲销
	
	private String value;
	private String name;
	
	private ChargePayType(String value, String name) {
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
