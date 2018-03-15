package com.eduboss.common;

public enum PromotionType {
	
	DISCOUNT("DISCOUNT", "打折"),//打折
	REDUCTION("REDUCTION", "减免现金"),//减免现金
	GIFT_CASH("GIFT_CASH", "赠送现金"),//赠送现金
	GIFT_COURSES_BY_RATE("GIFT_COURSES_BY_RATE", "赠送比例课时"),//赠送比例课时
	GIFT_COURSES("GIFT_COURSES", "赠送课时"); // 赠送课时
	
	private String value;
	private String name;
	
	private PromotionType(String value, String name) {
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
