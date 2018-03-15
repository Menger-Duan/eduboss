package com.eduboss.common;

/**
 * Created by Administrator on 2016/12/26.
 */
public enum IncomDistributeStatementsType {
    DISTRIBUTION("DISTRIBUTION", "分配"),
    EXTRACT("EXTRACT", "提取");

    private String value;
    private String name;

    IncomDistributeStatementsType(String value, String name) {
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
