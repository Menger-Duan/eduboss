package com.eduboss.common;

public enum LiveContactType {
	
	NEW("NEW", "新签"),
	RENEW("RENEW", "续费");
	
	private String value;
	private String name;
	
	private LiveContactType(String value, String name) {
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
