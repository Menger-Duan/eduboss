package com.eduboss.common;

public enum BusinessType {

    CONTRACT("CONTRACT", "合同"),//正常 
    FUNDS("FUNDS", "收款");
    
    private String value;
    private String name;
    
    private BusinessType(String value, String name) {
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
