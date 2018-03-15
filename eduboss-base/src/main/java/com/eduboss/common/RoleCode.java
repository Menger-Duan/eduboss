package com.eduboss.common;

public enum RoleCode {
	
	MARKET_STAFF("MARKET_STAFF", "市场专员"),//市场专员
	RECEPTIONIST("RECEPTIONIST", "前台"),//前台
	TEATCHER("TEATCHER", "老师"),//老师
	CONSULTOR("CONSULTOR", "咨询师"),//咨询师
	EDUCAT_SPEC("EDUCAT_SPEC", "教务专员"),//教务专员
	CAMPUS_DIRECTOR("CAMPUS_DIRECTOR", "校区主任"),//校区主任
	OPERATION_DIRECTOR("OPERATION_DIRECTOR","运营主任"),
	STUDY_MANAGER("STUDY_MANAGER", "学管师"),//学管师
	STUDY_MANAGER_HEAD("STUDY_MANAGER_HEAD", "学管主管"), //学管主管
	BREND_ADMIN("BREND_ADMIN", "分公司管理员"),//分公司管理员
	INTERNET_MARKETER("INTERNET_MARKETER", "网络专员"),//网络专员
	BREND_MENAGER("BREND_MENAGER", "分公司总经理"),//分公司总经理
	BREND_MERKETING_DIRECTOR("BREND_MERKETING_DIRECTOR", "分公司市场经理"),//分公司市场经理
	BREND_MERKETING_MENAGER("BREND_MERKETING_MENAGER", "分公司市场主管"),//分公司市场主管
	CONSULTOR_DIRECTOR("CONSULTOR_DIRECTOR", "咨询主管"),//咨询主管
	SUPER_ADMIN("SUPER_ADMIN", "超级管理员"),//超级管理员
	TRAINING_SUPER_ADMIN("TRAINING_SUPER_ADMIN", "培训系统超级管理员"),//超级管理员
	TRAINING_ADMIN("TRAINING_ADMIN", "培训管理员"),//超级管理员
	TRAINING_SYSTEM_ADMIN("TRAINING_SYSTEM_ADMIN", "培训系统管理员"),//超级管理员
	
	TRAINING_COMMER_USER("TRAINING_COMMER_USER", "培训系统普通用户"),//培训系统普通用户
	
	FEEDBACK_ADMIN("FEEDBACK_ADMIN","反馈管理员"), //反馈管理员

	NETWORK_MANAGER("NETWORK_MANAGER","网络主管"),
	INTERNET_MANAGER("INTERNET_MANAGER","网络经理"),
	//	TMK_MANAGER("TMK_MANAGER","TMK主管"),
	OUTCALL_MANAGER("OUTCALL_MANAGER","外呼主管"),
	TMK_MANAGER("TMK_MANAGER","TMK主管"),
	NETWORK_SPEC("NETWORK_SPEC","网络专员"),
//	TMK_SPEC("TMK_SPEC","TMK专员");
	OPERATION_MANAGER("OPERATION_MANAGER","营运经理"),//新增营运经理
	MARKETING_DIRECTOR("MARKETING_DIRECTOR","集团市场总监"),
	CAMPUS_OPERATION_DIRECTOR("CAMPUS_OPERATION_DIRECTOR","校区营运主任"),
	BRANCH_OUTCALL_MANAGER("BRANCH_OUTCALL_MANAGER","分公司外呼主管"),
	//specialRole
	SPECIALROLE("SPECIALROLE", "特殊角色"),
	OUTCALL_SPEC("OUTCALL_SPEC","外呼专员");
	
	private String value;
	private String name;
	
	private RoleCode(String value, String name) {
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
