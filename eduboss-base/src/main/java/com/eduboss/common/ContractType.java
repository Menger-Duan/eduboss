package com.eduboss.common;

public enum ContractType {
	
//	DEPOSIT("DEPOSIT", "订金"),//订金
	INIT_CONTRACT("INIT_CONTRACT", "初始合同"),//初始合同
	NEW_CONTRACT("NEW_CONTRACT", "新增合同"),//新增合同
	RE_CONTRACT("RE_CONTRACT", "续费合同"),//续费合同
	LIVE_CONTRACT("LIVE_CONTRACT", "直播同步合同"),//LIVE_CONTRACT
	REFUND("REFUND", "退款");//退款
	
	private String value;
	private String name;
	
	private ContractType(String value, String name) {
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
