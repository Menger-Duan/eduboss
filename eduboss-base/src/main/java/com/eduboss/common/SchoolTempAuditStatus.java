package com.eduboss.common;

/**
 * Created by Administrator on 2016/10/10.
 */
public enum SchoolTempAuditStatus {

    VALIDATE("VALIDATE", "已审核"), // 有效
    UNVALIDATE("UNVALIDATE", "审批不通过"), // 无效
    UNAUDIT("UNAUDIT", "未审批");//未审批

    private String value;
    private String name;

    SchoolTempAuditStatus(String value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
