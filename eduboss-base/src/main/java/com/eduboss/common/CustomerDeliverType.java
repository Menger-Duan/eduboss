package com.eduboss.common;

public enum CustomerDeliverType {
	
//	BRENCH("BRENCH", "本公司市场经理"),//分公司市场部，2-分公司公共资源，3-校区主任，4-校区公共资源，5-资询师
//	BRENCH_PUBLIC_POOL("BRENCH_PUBLIC_POOL", "分公司公共资源"),//分公司公共资源
//	CAMPUS("CAMPUS", "校区主任"),//校区主任
//	CAMPUS_PUBLIC_POOL("CAMPUS_PUBLIC_POOL", "校区公共资源"),//校区公共资源
//	CONSULTOR("CONSULTOR", "资询师"),//资询师
//	CALL_CENTER("CALL_CENTER", "呼叫中心");//呼叫中心
	PERSONAL_POOL("PERSONAL_POOL","个人"),
	CUSTOMER_RESOURCE_POOL("CUSTOMER_RESOURCE_POOL","公共池");
	private String value;
	private String name;
	
	private CustomerDeliverType(String value, String name) {
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
