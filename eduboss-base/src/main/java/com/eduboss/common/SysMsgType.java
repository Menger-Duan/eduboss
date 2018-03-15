package com.eduboss.common;

public enum SysMsgType {

	ONE_ON_ONE_COURSE("ONE_ON_ONE_COURSE", "一对一课程"),//一对一课程
	MINI_CLASS("MINI_CLASS", "小班课程"), //小班课程
	OTM_CLASS("OTM_CLASS", "一对多课程"), //一对多课程
	TWO_TEACHER("TWO_TEACHER", "双师辅班课程"),//双师
	REFUND_AUDIT("REFUND_AUDIT", "转账退费审批"), //转账退费审批
	ONLINE_TASK("ONLINE_TASK", "任务"),//佳学任务
	ONLINE_EXAM("ONLINE_EXAM", "测评"), //佳学测评
	OTHERS("OTHERS", "其他"), //其他
	SCHEDULE("SCHEDULE", "日程"),
	ITASSIST("ITASSIST","IT协助"),
	TEAMWORK("TEAMWORK","协同办公"),
	CONTACT("CONTACT","通讯录"),
	CUSTOMER("CUSTOMER", "客户资源调配"),
	NEW_CUSTOMER_RESOURCE("NEW_CUSTOMER_RESOURCE", "新客户资源");
	
	private String value;
	private String name;
	
	private SysMsgType(String value, String name) {
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
