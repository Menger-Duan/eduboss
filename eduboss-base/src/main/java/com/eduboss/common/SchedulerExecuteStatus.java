package com.eduboss.common;

/**
 * 批处理执行状态
 * @author lixuejun
 *
 */
public enum SchedulerExecuteStatus {
	SUCCESS("SUCCESS", "成功"), //成功
	FAILURE("FAILURE", "失败"); //失败
	
	private String value;
	private String name;
	
	private SchedulerExecuteStatus(String value, String name) {
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
