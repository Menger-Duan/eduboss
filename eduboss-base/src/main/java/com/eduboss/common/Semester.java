package com.eduboss.common;
/**
 * 学期
 * @author arvin
 *
 */
public enum Semester {
	LAST_SEMESTER("LAST_SEMESTER", "上学期"),
	NEXT_SEMESTER("NEXT_SEMESTER","下学期");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private Semester(String value, String name) {
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
