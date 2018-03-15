package com.eduboss.common;

public enum ResourcePoolJobType {

	VISIBLE("VISIBLE", "可见的"),
	DISTRIBUTABLE("DISTRIBUTABLE", "课分配的");
	
	private String name;
	private String value;
	
	private ResourcePoolJobType(String value, String name) {
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
