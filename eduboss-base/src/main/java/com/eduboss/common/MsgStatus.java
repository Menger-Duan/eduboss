package com.eduboss.common;

public enum MsgStatus {
	
	NEW("NEW", "新建"),//新建
	READ("READ", "已读"),//已读
	COMPLETE("COMPLETE", "完成"), //完成
    NEED_SEND_QUEUE("NEED_SEND_QUEUE","需要发送" ); // 还没有准备好， 要循环发送
	
	private String value;
	private String name;
	
	private MsgStatus(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
