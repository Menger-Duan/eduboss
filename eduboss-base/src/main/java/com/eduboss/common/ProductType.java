package com.eduboss.common;


/**
 * 产品类型 2014-7-31
 */
public enum ProductType {
	
	ONE_ON_ONE_COURSE("ONE_ON_ONE_COURSE", "一对一"),//一对一 
	ONE_ON_ONE_COURSE_NORMAL("ONE_ON_ONE_COURSE_NORMAL", "一对一统一价格"),//一对一统一价格 
	ONE_ON_ONE_COURSE_FREE("ONE_ON_ONE_COURSE_FREE", "一对一赠送课时价格"),//一对一赠送课时价格
	SMALL_CLASS("SMALL_CLASS", "小班"), //小班
	ECS_CLASS("ECS_CLASS","目标班"),//目标班（目标班）
	OTHERS("OTHERS", "其他"),//其他
	ONE_ON_MANY("ONE_ON_MANY","一对多"),//一对多
	LECTURE("LECTURE","讲座"),
	TWO_TEACHER("TWO_TEACHER","双师"),
	LIVE("LIVE","直播");
	
	private String value;
	private String name;
	
	private ProductType(String value, String name) {
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
