package com.eduboss.common;

public enum AppointmentType {
	
	APPOINTMENT("APPOINTMENT","预约"),
	APPOINTMENT_ONLINE("APPOINTMENT_ONLINE","网络分配营运经理"),
	APPOINTMENT_ONLINE_OPERATION("APPOINTMENT_ONLINE_OPERATION","营运经理分配校区"),
	APPOINTMENT_REACTIVATE("APPOINTMENT_REACTIVATE","无效客户重新激活"),
	FOLLOW_UP("FOLLOW_UP","跟进");
	private String value;
	private String name;
	
	private AppointmentType(String value, String name) {
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
