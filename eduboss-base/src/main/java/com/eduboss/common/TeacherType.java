package com.eduboss.common;


/**
 * 2016-12-15 教师类型
 * @author lixuejun
 *
 */
public enum TeacherType {
	
	NORMAL_TEACHER("NORMAL_TEACHER", "普通教师"),
	FUNCTOINS_TEACHER("FUNCTOINS_TEACHER", "职能教师"),
	TEN_CLASS_TEACHER("TEN_CLASS_TEACHER", "十人课堂教师");
	
	private String value;
	private String name;
	
	private TeacherType(String value, String name) {
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
