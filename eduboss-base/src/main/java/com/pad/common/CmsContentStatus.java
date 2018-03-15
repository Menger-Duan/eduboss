package com.pad.common;
/**
 * 发布状态
 * @author arvin
 *
 */
public enum CmsContentStatus {
	UN_PUBLISH("UN_PUBLISH", "待发布"),
	TO_PUBLISH("TO_PUBLISH","预约发布"),
	PUBLISH("PUBLISH","已发布");

	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private CmsContentStatus(String value, String name) {
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
