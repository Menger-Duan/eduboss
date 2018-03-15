package com.eduboss.common;

public enum PushMsgType {
	SYSTEM_NOTICE("SYSTEM_NOTICE", "系统通知"),//系统通知
	APP_VERSION("APP_VERSION", "App版本升级"), //App版本吧
	// 这三个类型跟andriod 那边对应好了，目前还没有以下类型的推送
	SYSTEM_EMAIL("SYSTEM_EMAIL", "邮箱"),	
	SYSTEM_MESSAGE("SYSTEM_MESSAGE", "系统消息"),
	SYSTEM_TRAINING("SYSTEM_TRAINING", "培训消息");
	
	private String value;
	private String name;
	
	private PushMsgType(String value, String name) {
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
