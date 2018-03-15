package com.eduboss.common;


/**
 * 产品类型 2014-7-31
 */
public enum ContractProductPaidStatus {
	
	UNPAY("UNPAY", "未付款"),//未付款
	PAYING("PAYING", "付款中"),//付款中
	PAID("PAID", "已付款"),//已付款
	CANCELED("CANCELED", "已取消"); // 已取消
	
	private String value;
	private String name;
	
	private ContractProductPaidStatus(String value, String name) {
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
