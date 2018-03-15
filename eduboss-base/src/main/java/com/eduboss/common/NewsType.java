package com.eduboss.common;

public enum NewsType {
	NEWSBANNER("NEWSBANNER","新闻banner"),
	GROUNPINFO("GROUNPINFO","集团信息"),
	BRENCHINFO("BRENCHINFO","分公司信息"),
	GROUNPMANNER("GROUNPMANNER","集团风采"),
	XINGHUOESSENCE("XINGHUOESSENCE","星火书苑");
	
	private String name;
	private String value;
	
	private NewsType(String value, String name) {
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
