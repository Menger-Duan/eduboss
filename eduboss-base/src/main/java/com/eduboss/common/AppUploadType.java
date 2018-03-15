package com.eduboss.common;
/**
 * 上传类型
 * @author xiaojinwang
 *
 */
public enum AppUploadType {
	ONEONONE("ONEONONE", "一对一"),
	TWOTEACHER("TWOTEACHER","双师"),
	OTM("OTM","一对多"),
	MINICLASS("MINICLASS","小班"),
	OTHER("OTHER","其他");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private AppUploadType(String value, String name) {
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
