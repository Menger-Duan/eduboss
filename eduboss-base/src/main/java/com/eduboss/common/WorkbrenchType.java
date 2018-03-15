package com.eduboss.common;

public enum WorkbrenchType {
//	TMK("TMK", "TMK"),
	OUTCALL("OUTCALL", "OUTCALL"),
	CALL_OUT("CALL_OUT","呼出"),
	CALL_IN("CALL_IN","呼入"),
	ON_LINE("ON_LINE","网络"),
	DIRECT_VISIT("DIRECT_VISIT","直访"),
	OUTSTANDING_VISIT("OUTSTANDING_VISIT","拉访"),
	TRANSFER("TRANSFER","转介绍");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private WorkbrenchType(String value, String name) {
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
