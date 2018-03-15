package com.eduboss.common;


/**
 * 2016-12-15 教师所属编制
 * @author lixuejun
 *
 */
public enum TeacherFunctions {
	
	NORMAL_ONE_ON_ONE("NORMAL_ONE_ON_ONE", "一对一教师"),
	NORMAL_MINI_CLASS("NORMAL_MINI_CLASS", "小班教师"),
	NORMAL_PROMISE_CLASS("NORMAL_PROMISE_CLASS", "目标班教师"),
	FUNCTOINS_DISCIPLINE_LEADER("FUNCTOINS_DISCIPLINE_LEADER", "学科组长"),
	FUNCTOINS_SENIOR_DISCIPLINE_LEADER("FUNCTOINS_SENIOR_DISCIPLINE_LEADER", "高级学科组长"),
	FUNCTOINS_TEACHING_DIRECTOR("FUNCTOINS_TEACHING_DIRECTOR", "教学主任"),
	FUNCTOINS_TEACHING_MANAGER("FUNCTOINS_TEACHING_MANAGER", "教学管理"),
	FUNCTOINS_SELECT_TRAIN_MANAGER("FUNCTOINS_SELECT_TRAIN_MANAGER", "选培管理"),
	FUNCTOINS_TEACHING_RESEARCH_MANAGER("FUNCTOINS_TEACHING_RESEARCH_MANAGER", "教研管理"),
	TEN_CLASS_TEACHING_MANAGER("TEN_CLASS_TEACHING_MANAGER", "教学管理"),
	TEN_CLASS_SUBJECT_OPERATION("TEN_CLASS_SUBJECT_OPERATION", "学科运营"),
	TEN_CLASS_PRODUCT_DEVELOPMENT("TEN_CLASS_PRODUCT_DEVELOPMENT", "产品研发");
	
	private String value;
	private String name;
	
	private TeacherFunctions(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name();
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
