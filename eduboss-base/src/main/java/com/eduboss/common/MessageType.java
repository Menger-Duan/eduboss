package com.eduboss.common;

public enum MessageType {
	
	SYSTEM_MSG("SYSTEM_MSG", "系统消息"),//系统消息
	NOTIFY("NOTIFY", "通知"),//通知
	CONVERSATION("CONVERSATION", "员工对话消息");//员工对话消息
	
	private String value;
	private String name;
	
	private MessageType(String value, String name) {
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
