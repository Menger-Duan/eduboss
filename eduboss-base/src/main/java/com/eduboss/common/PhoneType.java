package com.eduboss.common;

/**
 * 电话类型
 * @author ndd
 *
 */
public enum PhoneType {
	
	INCOMING_TEL("INCOMING_TEL", "来电"),//去电
	OUTGOING_CALLS("OUTGOING_CALLS", "去电");//去电
	
	private String value;
	private String name;
	
	private PhoneType(String value, String name) {
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
