package com.eduboss.common;

public enum ContractPaidStatus {
	
	UNPAY("UNPAY", "未付款"),//未付款
	PAYING("PAYING", "付款中"),//付款中
	PAID("PAID", "付款完成"),//已付款
	CANCELED("CANCELED", "已取消"); // 已取消
	
	private String value;
	private String name;
	
	private ContractPaidStatus(String value, String name) {
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
