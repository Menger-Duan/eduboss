package com.eduboss.common;

/**
 * 业绩分配目标类型
 * Created by Administrator on 2016/12/17.
 */

public enum BonusDistributeType {
    CAMPUS("CAMPUS", "校区业绩"),
    CAMPUS_CAMPUS("CAMPUS_CAMPUS", "校区"),//校区业绩_校区
    USER("USER", "提成人业绩"),
    USER_USER("USER_USER", "个人"),//提成人业绩_个人
    USER_CAMPUS("USER_CAMPUS", "校区团队"),//提成人业绩_校区团队
    USER_BRANCH("USER_BRANCH", "分公司团队");//提成人业绩_分公司团队

    private String value;
    private String name;

    BonusDistributeType(String value, String name) {
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
