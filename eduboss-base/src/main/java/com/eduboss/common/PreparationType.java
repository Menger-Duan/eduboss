package com.eduboss.common;


/**
 * 2016-12-15 教师所属编制
 * @author lixuejun
 *
 */
public enum PreparationType {
	
	ONE_ON_ONE("ONE_ON_ONE", "一对一"),
	TEN_CLASSROOM("TEN_CLASSROOM", "十人课堂");
	
	private String value;
	private String name;
	
	private PreparationType(String value, String name) {
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
