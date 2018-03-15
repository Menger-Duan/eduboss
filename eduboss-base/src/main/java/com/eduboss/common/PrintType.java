package com.eduboss.common;

public enum PrintType {

	CONTRACT("CONTRACT", "合同"),// 合同
	RECEIPT("RECEIPT", "收据"),// 收据
	INCOME("INCOME", "营收凭证"),// 营收凭证
	REMAIN("REMAIN", "剩余资金凭证"),// 剩余资金凭证
	PAYMENT("PAYMENT", "现金流凭证");// 现金流凭证
	
	private String value;
	private String name;
	
	private PrintType(String value, String name) {
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
