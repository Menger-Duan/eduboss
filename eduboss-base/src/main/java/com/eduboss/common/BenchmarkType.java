package com.eduboss.common;
/**
 * 基准判定类型
 * @author arvin
 *
 */
public enum BenchmarkType {
    EFFECTIVE("EFFECTIVE", "有效"),
    INVALID("INVALID","无效"),
	RECORD_ONLY("RECORD_ONLY","仅作记录");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private BenchmarkType(String value, String name) {
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
