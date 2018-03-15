package com.eduboss.common;

/**
 * 销售状态
 * @author lixuejun
 *
 */
public enum SellStatus {
	
	SOLD_OUT("SOLD_OUT", "下架"), // 下架
	PUTAWAY("PUTAWAY", "上架"); // 上架
	
	private String value;
	private String name;
	
	private SellStatus(String value, String name) {
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
