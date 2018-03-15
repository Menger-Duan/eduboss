package com.eduboss.common;

public enum CourseTemplateType {
	
	PERSONAL("PERSONAL", "个人"),//个人
	PUBLIC("PUBLIC", "公共");//公共
	
	private String value;
	private String name;
	
	private CourseTemplateType(String value, String name) {
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
