package com.eduboss.common;

public enum LiveFinanceType {
	
	INCOME("INCOME", "现金流"),
	REFUND("REFUND", "退款"),
	REVENUE("REVENUE", "营收"); 
	
	private String value;
	private String name;
	
	private LiveFinanceType(String value, String name) {
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
