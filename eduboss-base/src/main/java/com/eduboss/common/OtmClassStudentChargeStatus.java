package com.eduboss.common;

public enum OtmClassStudentChargeStatus {
	
	UNCHARGE("UNPAY", "未扣费"),//未扣费
	CHARGED("PAID", "已扣费");//已扣费
	
	private String value;
	private String name;
	
	private OtmClassStudentChargeStatus(String value, String name) {
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
