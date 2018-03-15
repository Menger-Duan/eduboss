package com.eduboss.common;

public enum MiniClassRelationType {

	CONTINUE("CONTINUE", "续班"),
	EXTEND("EXTEND", "扩科");
	
	private String value;
	private String name;
	
	private MiniClassRelationType(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
