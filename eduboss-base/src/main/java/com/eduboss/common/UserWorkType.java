package com.eduboss.common;


/**
 * 工作类型 2014-7-31
 */
public enum UserWorkType {
	
	DUMMY("DUMMY", "虚拟"),
	FULL_TIME("FULL_TIME", "全职"),
	SPECIAL_DUTY("SPECIAL_DUTY", "专职"),
	PRACTICE_TIME("PRACTICE_TIME", "实习"),
	PART_TIME("PART_TIME", "兼职"),
	OUT_SOURCE("OUT_SOURCE", "外协"),
	GRADUATE("GRADUATE", "应届生");
	
	private String value;
	private String name;
	
	private UserWorkType(String value, String name) {
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
