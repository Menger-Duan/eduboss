package com.eduboss.common;

/**
 * 销售渠道
 * @author lixuejun
 *
 */
public enum SaleChannel {

	OFF_LINE("OFF_LINE", "校区"), // 线下
	ON_LINE("ON_LINE", "在线"); // 线上
	
	private String value;
	private String name;
	
	private SaleChannel(String value, String name) {
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
