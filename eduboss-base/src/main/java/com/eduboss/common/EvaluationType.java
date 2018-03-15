package com.eduboss.common;
/**
 * 评级类型
 * @author arvin
 *
 */
public enum EvaluationType {
    A("A", "A"),
	B("B", "B"),
	C("C", "C"),
    D("D", "D"),
    E("E", "E");
	
	private String value;
	private String name;
	/**
	 * @param value
	 * @param name
	 */
	private EvaluationType(String value, String name) {
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
