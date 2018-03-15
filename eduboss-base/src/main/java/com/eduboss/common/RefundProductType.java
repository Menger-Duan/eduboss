package com.eduboss.common;


/**
 * 产品类型 2014-7-31
 */
public enum RefundProductType {
	
	ONE_ON_ONE_COURSE("ONE_ON_ONE_COURSE", "一对一"),//一对一 
	ONE_ON_ONE_COURSE_FREE_HOUR("ONE_ON_ONE_COURSE_FREE_HOUR", "一对一赠送课时"),//一对一赠送课时 
	SMALL_CLASS("SMALL_CLASS", "小班"), //小班
	OTHERS("OTHERS", "其他");//其他
	
	private String value;
	private String name;
	
	private RefundProductType(String value, String name) {
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
