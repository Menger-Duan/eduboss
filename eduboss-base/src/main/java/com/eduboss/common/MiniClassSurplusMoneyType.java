package com.eduboss.common;

public enum MiniClassSurplusMoneyType {
	
	TEACHER("TEACHER", "老师"),//老师
	STUDY_MANEGER("STUDY_MANEGER", "班主任"),//班主任（学管）
	CAMPUS("CAMPUS", "校区"),//校区
	BRENCH("BRENCH", "分公司"),//分公司
	GROUNP("GROUNP", "集团");//集团
	
	private String value;
	private String name;
	
	private MiniClassSurplusMoneyType(String value, String name) {
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
