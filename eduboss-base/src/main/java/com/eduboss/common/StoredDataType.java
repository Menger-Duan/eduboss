package com.eduboss.common;

public enum StoredDataType {

	TEXT("TEXT", "文字"),//文字
	VOICE("VOICE", "声音"),//声音
	IMAGE("IMAGE", "图像");//图像
	private String value;
	private String name;
	
	private StoredDataType(String value, String name) {
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
