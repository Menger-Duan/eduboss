package com.eduboss.common;


/**
 * 产品单科全科
 */
public enum ProductSingleSubject {

	ONE_SUBJECT("ONE_SUBJECT", "单科"),
	ALL_SUBJECT("ALL_SUBJECT", "全科");

	private String value;
	private String name;

	private ProductSingleSubject(String value, String name) {
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
