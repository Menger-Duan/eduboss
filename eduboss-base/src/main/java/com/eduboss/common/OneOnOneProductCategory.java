package com.eduboss.common;


/**
 * 产品类型 2014-7-31
 */
public enum OneOnOneProductCategory {
	
	DEFAULT("DEFAULT", "默认"),//默认 
	FREE_HOUR("FREE_HOUR", "赠送课时"); //赠送课时
	
	private String value;
	private String name;
	
	private OneOnOneProductCategory(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
