package com.eduboss.common;

public enum SessionType {
	

	NOTICE("NOTICE", "公告"),//公告
	REMIND("REMIND", "提醒"),//提醒
	COURSE("COURSE", "课程"),//课程， 包括 一对一 小班
	CONVERSATION("CONVERSATION", "对话");//对话	
	private String value;
	private String name;
	
	private SessionType(String value, String name) {
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
