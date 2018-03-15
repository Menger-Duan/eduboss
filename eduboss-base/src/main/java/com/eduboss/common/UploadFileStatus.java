package com.eduboss.common;
/**@author wmy
 *@date 2015年11月4日上午11:30:57
 *@version 1.0 
 *@description
 */
public enum UploadFileStatus {

	INUSE("INUSE", "在使用"),
	UNUSED("UNUSED", "不使用"),
	DELETED("DELETED", "已删除"); 
	
	private String value;
	private String name;
	
	private UploadFileStatus(String value, String name) {
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


