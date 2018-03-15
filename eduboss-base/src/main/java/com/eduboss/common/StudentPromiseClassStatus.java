package com.eduboss.common;

public enum StudentPromiseClassStatus {
	
	
	NEW("NEW", "新签"),//新签
	CLASSING("CLASSING", "上课中"),//上课中
	STOP_CLASS("STOP_CLASS", "停课"),//停课
	FINISH_CLASS("FINISH_CLASS", "结课"),//结课
	SPECIAL_STOP("SPECIAL_STOP", "特殊停课"),//特殊停课
	GRADUATION("GRADUATION","毕业");//毕业
	private String value;
	private String name;
	
	private StudentPromiseClassStatus(String value, String name) {
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
