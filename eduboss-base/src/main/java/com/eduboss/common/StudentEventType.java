package com.eduboss.common;

public enum StudentEventType {
	
	CONTRACT("CONTRACT","合同"),//合同
	SCORE("SCORE", "成绩"),//成绩
	COMMENT("COMMENT", "评价"),//评价
	COURSE("COURSE", "课程")//课程
	;//收款
	
	private String value;
	private String name;
	
	private StudentEventType(String value, String name) {
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
