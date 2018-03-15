package com.eduboss.common;

public enum GroupCode {
	BOSS(1, "星火"),
	APLUS(0, "培优");
	private Integer value;
	private String name;
	
	private GroupCode(Integer value, String name) {
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

	public Integer getValue() {
		return value;
	}
}
