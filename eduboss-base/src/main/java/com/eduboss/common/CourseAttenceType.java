package com.eduboss.common;

public enum CourseAttenceType {
	
	MANUALLY_FILL_IN("MANUALLY_FILL_IN", "手动填写"),
	AUTO_RECOG("AUTO_RECOG", "自动识别"); 
	
	private String value;
	private String name;
	
	private CourseAttenceType(String value, String name) {
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
