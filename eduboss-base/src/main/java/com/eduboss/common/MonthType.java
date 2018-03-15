package com.eduboss.common;

public enum MonthType {
	
	JAN("JAN", "一月"),//一月
	FEB("FEB", "二月"),//二月
	MAR("MAR", "三月"),//三月
	APR("APR", "四月"), //四月
	MAY("MAY", "五月"),//五月
	JUN("JUN", "六月"),//六月
	JUL("JUL", "七月"),//七月
	AUG("AUG", "八月"),//八月
	SEPT("SEPT", "九月"),//九月
	OCT("OCT", "十月"),//十月
	NOV("NOV", "十一月"),//十一月
	DEC("DEC", "十二月"); //十二月
	
	private String value;
	private String name;
	
	private MonthType(String value, String name) {
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
