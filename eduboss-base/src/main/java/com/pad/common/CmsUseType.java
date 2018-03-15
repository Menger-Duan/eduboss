package com.pad.common;
/**
 * 成绩类型
 * @author arvin
 *
 */
public enum CmsUseType {
	VISITOR("VISITOR", "浏览模式"),
	LOGIN_USER("LOGIN_USER","工作台模式");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private CmsUseType(String value, String name) {
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
