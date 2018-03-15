package com.eduboss.common;

/**
 * POS终端的状态
 * @author lixuejun
 *
 */
public enum PosMachineStatus {

	ACTIVATE("ACTIVATE", "已激活"), // 已激活
	DISABLE("DISABLE", "已禁用"); // 已禁用
	
	private String value;
	private String name;
	
	private PosMachineStatus(String value, String name) {
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
