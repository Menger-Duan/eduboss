package com.eduboss.common;
/**
 * 成绩抽象类型
 * @author arvin
 *
 */
public enum AchievementAbstractType {
	SCORE_RANKING("SCORE_RANKING", "分数和排名"),
	GRADE("GRADE","评级");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private AchievementAbstractType(String value, String name) {
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
