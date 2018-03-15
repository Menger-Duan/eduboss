package com.eduboss.common;

/**
 * 推荐状态
 * @author lixuejun
 *
 */
public enum RecommendStatus {
	
	NOT_RECOMMEND("NOT_RECOMMEND", "不推荐"), // 不推荐
	RECOMMEND("RECOMMEND", "推荐"); // 推荐
	
	private String value;
	private String name;
	
	private RecommendStatus(String value, String name) {
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
