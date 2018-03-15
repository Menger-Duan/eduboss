package com.eduboss.common;


/**
 * 2016-12-15 教师级别
 * @author lixuejun
 *
 */
public enum TeacherLevel {
	
	PART_TIME("PART_TIME", "无"), // 兼职无级别
	NORMAL_STAR_THIRD("NORMAL_STAR_THIRD", "X3"),
	NORMAL_STAR_FOURTH("NORMAL_STAR_FOURTH", "X4"),
	NORMAL_STAR_FIFTH("NORMAL_STAR_FIFTH", "X5"),
	NORMAL_STAR_SIXTH("NORMAL_STAR_FOURTH", "X6"),
	NORAMAL_H3("NORAMAL_H3", "H3"),
	NORAMAL_H4("NORAMAL_H4", "H4"),
	NORAMAL_H5("NORAMAL_H5", "H5"),
	NORAMAL_H6("NORAMAL_H6", "H6"),
//	NORMAL_STAR_SIXTH("NORMAL_STAR_FOURTH", "六星"),
//	NORMAL_STAR_SEVENTH("NORMAL_STAR_SEVENTH", "七星"),
//	NORMAL_GOLD_MEDAL("NORMAL_GOLD_MEDAL", "金牌"),
	V1("V1", "V1"),
	V2("V2", "V2"),
	V3("V3", "V3"),
	V4("V4", "V4"),
	V5("V5", "V5"),
	V6("V6", "V6"),
	V7("V7", "V7"),
	V8("V8", "V8"),
	V9("V9", "V9"),
	V10("V10", "V10"),
	V11("V11", "V11"),
	V12("V12", "V12"),
	V13("V13", "V13"),
	V14("V14", "V14"),
	V15("V15", "V15"),
	V16("V16", "V16"),
	V17("V17", "V17"),
	V18("V18", "V18"),
	V19("V19", "V19"),
	V20("V20", "V20");
	
	private String value;
	private String name;
	
	private TeacherLevel(String value, String name) {
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
