package com.eduboss.common;

public enum FundsPayType {
	
	RECEIPT("RECEIPT", "收款"),//收款
	WASH("WASH", "冲销");//冲销
	
	private String value;
	private String name;
	
	private FundsPayType(String value, String name) {
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
