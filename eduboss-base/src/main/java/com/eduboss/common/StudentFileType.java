package com.eduboss.common;

public enum StudentFileType {
	
 
	TEACHINGPLAN("TEACHINGPLAN", "教案"),//教案
	EXAMPAPER("EXAMPAPER", "试卷"),//试卷
	COACHING("COACHING", "辅导"),//辅导	 
	STUDYPLAN("STUDYPLAN", "学习计划"),//学习计划
	FIVE_METTING("FIVE_METTING", "五方会议"),//五方会议
	PARENT_METTING("PARENT_METTING", "家长会"),//家长会
	OTHER("OTHER", "其他");//其他	 
	
	private String value;
	private String name;
	
	private StudentFileType(String value, String name) {
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
