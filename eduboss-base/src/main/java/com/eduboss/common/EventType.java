package com.eduboss.common;

/**
 * 
 * @author xiaojinwang
 * 用户操作事件类型
 *
 */
public enum EventType {
	COUNSELOR_OUTCALL("COUNSELOR_OUTCALL","咨询师获取外呼客户"),
	COUNSELOR_NETWORK("COUNSELOR_NETWORK","咨询师获取网络客户"),
	COUNSELOR_OTHER("COUNSELOR_OTHER","咨询师获取其他客户"),
	APPOINTMENT_ONLINE("APPOINTMENT_ONLINE","网络分配校区"),
	SIGN_CONTRACT("SIGN_CONTRACT","签合同"),
	PHONECALL("PHONECALL","留电量"),
	GET("GET","获取客户"),
	RELEASE("RELEASE","释放客户");
	private String value;
	private String name;
	
	private EventType(String value, String name) {
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
