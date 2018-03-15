package com.eduboss.common;

/**
 * Created by Administrator on 2017/7/17.
 */
public enum StateOfEmergency {
    NORMAL("NORMAL", "正常"), // 正常 NORMAL normal
    EMERGENCY("EMERGENCY", "紧急"); //紧急 emergency

    private String value;
    private String name;

    StateOfEmergency(String value, String name) {
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
