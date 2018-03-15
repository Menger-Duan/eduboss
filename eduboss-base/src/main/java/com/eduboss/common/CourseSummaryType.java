package com.eduboss.common;

/**
 * 排课类型
 * Created by xuwen on 2015/3/6.
 */
public enum CourseSummaryType {
    STUDENT("STUDENT", "面向学生"),
    TEACHER("TEACHER", "面向老师");

    private String value;
    private String name;

    private CourseSummaryType(String value, String name) {
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
