package com.eduboss.common;

public enum ResEntranceType {
	
	CALL_IN("CALL_IN", "呼入"), 
	CALL_OUT("CALL_OUT", "陌拜"), 
	ON_LINE("ON_LINE","网络"),
//	TMK("TMK","外呼"),
	//名字修改   将外呼改为TMK 呼出 改为陌拜  
	//新增地推入口
	GROUNDPROMOTION("GROUNDPROMOTION","地推数据"),
	OUTCALL("OUTCALL","TMK"),
	TRANSFER("TRANSFER","转介绍"),
	OUTSTANDING_VISIT("OUTSTANDING_VISIT","拉访"),
	DIRECT_VISIT("DIRECT_VISIT", "直访"); 
	
	private String value;
	private String name;
	
	private ResEntranceType(String value, String name) {
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
