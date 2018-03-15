package com.eduboss.common;

public enum ContractStatus {
	
	NORMAL("NORMAL", "正常"),//正常
	RETURNS("RETURNS", "退费"),//退费
	FINISHED("FINISHED", "完成"),//完成
	UNVALID("UNVALID", "无效"); //无效
	
	private String value;
	private String name;
	
	private ContractStatus(String value, String name) {
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
