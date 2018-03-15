package com.eduboss.common;

public enum WeekDay {
	
	MON("MON", "星期一"),//星期一
	TUE("TUE", "星期二"),//星期二
	WED("WED", "星期三"),//星期三
	THU("THU", "星期四"),//星期四
	FRI("FRI", "星期五"),//星期五
	SAT("SAT", "星期六"),//星期六
	SUN("SUN", "星期日");//星期日
	
	private String value;
	private String name;
	
	private WeekDay(String value, String name) {
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
