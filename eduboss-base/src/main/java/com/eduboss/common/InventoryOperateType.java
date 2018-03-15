package com.eduboss.common;

public enum InventoryOperateType {
	WAREHOUSING("WAREHOUSING","入库"),
	MOVEINVENTORY("MOVEINVENTORY","移库"),
	CONSUME("CONSUME", "领用"),//领用
	SALE("SALE","销售"), //销售
	RETURN("RETURN","退回"),//退回
	DAMAGE("DAMAGE","报损");//报损
	
	private String value;
	private String name;
	
	private InventoryOperateType(String value, String name) {
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
