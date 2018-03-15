package com.eduboss.common;


public enum StudentPromiseStatus {

	ING("1", "进行中"),//进行中
	RETURN_ING("2", "退费中"),//退费中
	RETURN("3", "已退费"),//已退费
	END("4", "已结课"),//已结课
	ENDED("0", "已完结"); // 已完结

	private String value;
	private String name;

	private StudentPromiseStatus(String value, String name) {
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
