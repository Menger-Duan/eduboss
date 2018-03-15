package com.eduboss.common;

/**
 * @author Yao
 * 消息队列类型
 *
 */
public enum MessageQueueType {
	
	CONTRACT_BONUS_MONEY("CONTRACT_BONUS_MONEY", "业绩统计"),
	CONTRACT_FINACE("CONTRACT_FINACE", "现金流");
	
	
	private String value;
	private String name;
	
	private MessageQueueType(String value, String name) {
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
