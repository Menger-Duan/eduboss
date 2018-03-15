package com.eduboss.common;

public enum CustomerDealStatus {
	
	NEW("NEW", "转校"),//新增
	STAY_FOLLOW("STAY_FOLLOW", "待跟进"),//待跟进
	FOLLOWING("FOLLOWING", "跟进中"),//跟进中
	SIGNEUP("SIGNEUP", "已签合同"),//已签合同
	INVALID("INVALID", "无效客户"),//无效用户
//	TOBEASSIGNED("TOBEASSIGNED","待分配"),//分配的不是咨询师而是其他组织或者主管前台 指待分配给咨询师的意思
	//INVALID_AUDIT("INVALID_AUDIT","无效审核中");//无效审核中
	TOBE_AUDIT("TOBE_AUDIT","待审核");//无效审核 转介绍审核的中转状态

	private String value;
	private String name;
	
	private CustomerDealStatus(String value, String name) {
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
