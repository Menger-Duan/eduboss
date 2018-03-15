package com.eduboss.common;

/**
 * 电话时间
 * @author ndd
 *
 */
public enum PhoneEvent {
	
	INCOMING_TEL_RING("INCOMING_TEL_RING", "来电振铃"),
	OFF_HOOK("OFF_HOOK", "摘机"),
	ON_HOOK("ON_HOOK", "挂机");
	
	private String value;
	private String name;
	
	private PhoneEvent(String value, String name) {
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
