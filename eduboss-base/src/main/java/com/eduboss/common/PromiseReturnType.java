package com.eduboss.common;


public enum PromiseReturnType {

	RETURN_TO_ACCOUNT("RETURN_TO_ACCOUNT", "退回电子账户"),//退回电子账户
	RETURN_TO_CUSTOMER("RETURN_TO_CUSTOMER", "直接退款"); // 直接退款

	private String value;
	private String name;

	private PromiseReturnType(String value, String name) {
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
