package com.eduboss.common;

public enum SummaryClassType {

	ONE_ON_ONE("ONE_ON_ONE", "一对一"),// 一对一
	MINI_CLASS("MINI_CLASS", "小班"),// 小班
	PROMISE_CLASS("PROMISE_CLASS", "目标班"),// 目标班
	CAMPUS("CAMPUS", "校区总"); // 校区总
	
	private String value;
	private String name;
	
	private SummaryClassType(String value, String name) {
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
