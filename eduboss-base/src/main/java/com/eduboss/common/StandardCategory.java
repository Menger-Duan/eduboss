package com.eduboss.common;
/**
 * 进步类别
 * @author arvin
 *
 */
public enum StandardCategory {
    PROGRESS("PROGRESS", "进步"),
	FLAT("FLAT","持平"),
	BACKWARD("BACKWARD","退步");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private StandardCategory(String value, String name) {
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
