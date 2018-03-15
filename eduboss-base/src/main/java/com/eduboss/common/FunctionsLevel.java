package com.eduboss.common;


/**
 * 2016-12-15 教师所属编制
 * @author lixuejun
 *
 */
public enum FunctionsLevel {
	
	STAFF_MEMBER("STAFF_MEMBER", "职员"),
	EXECUTIVE_DIRECTOR("EXECUTIVE_DIRECTOR", "主管"),
	MANAGER("MANAGER", "经理"),
	COMMISSIONER("COMMISSIONER", "专员"),
	DIRECTOR("DIRECTOR", "主任"),
	COMMISSIONER_FIRST("COMMISSIONER_FIRST", "一级专员"),
	COMMISSIONER_SECOND("COMMISSIONER_SECOND", "二级专员"),
	COMMISSIONER_THIRD("COMMISSIONER_THIRD", "三级专员"),
	COMMISSIONER_FOURTH("COMMISSIONER_FOURTH", "四级专员"),
	COMMISSIONER_SENIOR("COMMISSIONER_SENIOR", "高级专员"),
	EXECUTIVE_DIRECTOR_FIRST("EXECUTIVE_DIRECTOR_FIRST", "一级主管"),
	EXECUTIVE_DIRECTOR_SECOND("EXECUTIVE_DIRECTOR_SECOND", "二级主管"),
	EXECUTIVE_DIRECTOR_SENIOR("EXECUTIVE_DIRECTOR_SENIOR", "高级主管");
	
	private String value;
	private String name;
	
	private FunctionsLevel(String value, String name) {
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
