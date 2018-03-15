package com.eduboss.common;
/**
 * 成绩类型
 * @author arvin
 *
 */
public enum AchievementType {
	SCORE("SCORE", "分数"),
	CLASS_RANKING("CLASS_RANKING","班级排名"),
	GRADE_RANKING("GRADE_RANKING","年级排名"),
	GRADE("GRADE","评级");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private AchievementType(String value, String name) {
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
