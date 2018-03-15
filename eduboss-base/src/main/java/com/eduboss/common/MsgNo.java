package com.eduboss.common;

public enum MsgNo {

	M1("M1", "M1"), // M 对应boss的系统信息模块  扣费资金回滚
	M2("M2", "M2"), // M2一对多课程回滚
	M3("M3", "M3"), // M3小班退费回滚
	M4("M4", "M4"), // M4转账退费审批发送通知
	M5("M5", "M5"), // M5转账退费审批回退发送通知
	M6("M6", "M6"), // M6 账号密码： 2.重置密码短信配置
	M7("M7", "M7"), // M7 账号密码： 1.账号开通 .初始密码短信配置
	M8("M8", "M8"), // 一对一扣费资金冲销
	M9("M9", "M9"), // 小班扣费资金冲销
	M10("M10", "M10"), // 一对多扣费资金冲销
	M11("M11", "M11"), // 小班插班，消息发送
	M12("M12", "M12"), // 一对多插班，消息发送
	M13("M13", "M13"), // 客户资源调配
	M14("M14", "M14"),//双师扣费冲销
	M15("M15", "M15"),//营主、咨询师 新客户资源
	O1("O1", "O1"), // O 对应佳学的信息模块  任务
	O2("O2", "O2"), // 测评
	OA("OA", "OA"); // OA

	
	private String name;
	private String value;
	
	private MsgNo(String value, String name) {
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
