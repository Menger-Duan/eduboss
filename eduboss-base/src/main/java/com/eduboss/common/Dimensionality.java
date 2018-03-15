package com.eduboss.common;

public enum Dimensionality {
	
	BRENCH_DIM("BRENCH_DIM", "分公司维度"),//分公司维度
	CAMPUS_DIM("CAMPUS_DIM", "校区维度");//校区维度
	
	private String value;
	private String name;
	
	private Dimensionality(String value, String name) {
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
