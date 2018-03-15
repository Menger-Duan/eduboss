package com.eduboss.common;

public enum VisitType {
	
	NOT_COME("NOT_COME","未到访"),
	PARENT_COME("PARENT_COME","家长到访"),
	STUDENT_COME("STUDENT_COME","学生到访"),
	PARENT_STUDENT_COME("PARENT_STUDENT_COME","家长学生到访");
	private String value;
	private String name;
	
	private VisitType(String value, String name) {
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
