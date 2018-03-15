package com.pad.common;
/**
 * 图文类型
 * @author arvin
 *
 */
public enum CmsContentType {
	PICTURE("PICTURE", "图文"),
	DOCUMENT("DOCUMENT","文档"),
	LINK("LINK","链接");

	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private CmsContentType(String value, String name) {
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
