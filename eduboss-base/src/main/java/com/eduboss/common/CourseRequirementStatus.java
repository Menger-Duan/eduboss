package com.eduboss.common;

public enum CourseRequirementStatus {
	
	ARRENGED("ARRENGED", "已排课"),//已排课
	WAIT_FOR_ARRENGE("WAIT_FOR_ARRENGE", "未排课");//未排课
	
	private String value;
	private String name;
	
	private CourseRequirementStatus(String value, String name) {
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
