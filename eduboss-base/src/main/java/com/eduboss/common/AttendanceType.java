package com.eduboss.common;

public enum AttendanceType {


	IC_CARD("IC_CARD", "卡"),//卡
	FINGERPRINT("FINGERPRINT", "指纹"),//指纹
    SYSTEM("SYSTEM","系统"); //系统考勤

	
	private String value;
	private String name;
	
	private AttendanceType(String value, String name) {
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
