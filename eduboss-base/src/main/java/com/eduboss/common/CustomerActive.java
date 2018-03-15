package com.eduboss.common;

/**
 * Created by Administrator on 2017/4/25.
 */

/**
 * 是否6个月未跟进
 * 客户未签单的情况：距离最近一次跟进时间超过6个月的客户，属于6个月内未跟进客户。
 * 客户已签单的情况：学生在6个月内没有产生过课消，属于6个月内未跟进的客户。
 */
public enum CustomerActive {
    NEW_CUSTOMER("NEW_CUSTOMER", "新客户"),
    ACTIVE("ACTIVE", "活跃"),
    INACTIVE("INACTIVE", "不活跃");

    private String value;
    private String name;

    private CustomerActive(String value, String name) {
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
