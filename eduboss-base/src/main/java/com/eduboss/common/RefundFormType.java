package com.eduboss.common;

public enum RefundFormType {

	ACCOUNT_TRANSFER("ACCOUNT_TRANSFER", "电子账户转账"),// 电子账户转账
	MONEY_REFUND("MONEY_REFUND", "结课退费"), // 结课退费
	MOENY_ACC_REFUND("MOENY_ACC_REFUND", "电子账户退费"); // 电子账户退费

	
	private String value;
	private String name;
	
	private RefundFormType(String value, String name) {
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
