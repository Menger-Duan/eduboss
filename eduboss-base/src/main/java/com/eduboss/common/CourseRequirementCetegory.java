package com.eduboss.common;

public enum CourseRequirementCetegory {
	
	NEW_CONTRACT("NEW_CONTRACT", "新生"),//新合同收款， 新生
	USER_SUBMIT("USER_SUBMIT", "学管提交"),//学管提交
	EDUCATOR_SUBMIT("EDUCATOR_SUBMIT", "教务提交");//教务提交
	
	private String value;
	private String name;
	
	private CourseRequirementCetegory(String value, String name) {
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
