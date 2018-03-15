package com.eduboss.common;

public enum DistributeType {
	
	DISTRIBUTION("DISTRIBUTION", "分配"),//分配
	EXTRACT("EXTRACT", "提取"),//提取
	TRANSFER_IN("TRANSFER_IN", "转入"),//转入
	TRANSFER_OUT("TRANSFER_OUT", "转出"),//转出
	CONSUME("CONSUME", "消耗"), //消耗
	WASH("WASH", "冲销"), //冲销
	REFUND("REFUND", "退费"); //退费
	
	
	private String value;
	private String name;
	
	private DistributeType(String value, String name) {
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
