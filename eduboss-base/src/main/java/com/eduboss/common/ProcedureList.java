package com.eduboss.common;


public enum ProcedureList {
	INITFINACEDATABYDATE("initFinaceDataByDate", "刷新现金流"),
	INITINCOMEDATABYDATE("initIncomeDataByDate", "刷新营收");
	
	private String value;
	private String name;
	
	private ProcedureList(String value, String name) {
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
