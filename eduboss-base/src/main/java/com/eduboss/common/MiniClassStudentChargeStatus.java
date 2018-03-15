package com.eduboss.common;

public enum MiniClassStudentChargeStatus {
	
	UNCHARGE("UNCHARGE", "未扣费"),//未扣费
	CHARGED("CHARGED", "已扣费");//已扣费
	
	private String value;
	private String name;
	
	private MiniClassStudentChargeStatus(String value, String name) {
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
