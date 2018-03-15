package com.eduboss.common;

public enum OrganizationSign {

	OTHER("OTHER", "其他"),//其他
	XHJY("XHJY", "星火教育"),//星火教育
	XZB("XZB", "晓直播"),//晓直播
	XPY("XPY", "晓培优");//晓培优

	private String value;
	private String name;

	private OrganizationSign(String value, String name) {
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
