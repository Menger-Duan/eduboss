package com.eduboss.common;

public enum CustomerType {
	
	VISIT("VISIT", "上门直访"),//上门直访
	TEL_CONSULTATION("TEL_CONSULTATION", "来电咨询"),//来电咨询
	GROUND_CALLING("GROUND_CALLING", "地推电话"),//地推地话
	OUT_CALLING("OUT_CALLING", "外呼电话"),//外呼电话
	INTERNET_CALLING("INTERNET_CALLING", "网络电话"),//网络电话
	STRANGE_CALL("STRANGE_CALL", "陌拜电话"),//陌拜电话
	INTRODUCE("INTRODUCE", "转介绍"),//转介绍
	INTERNET("INTERNET", "网络客户");//网络客户
	
	private String value;
	private String name;
	
	private CustomerType(String value, String name) {
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
