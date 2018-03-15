package com.eduboss.common;


/**
 * 合同产品类型 2014-7-31
 */
public enum ContractProductStatus {
	
	NORMAL("NORMAL","正常"), // 正常
	STARTED("STARTED","开始上课"), // 开始上课
	FROZEN("FROZEN", "冻结中"),//冻结中
	REFUNDED("REFUNDED", "已经退款"),
	ENDED("ENDED","结束") , // 正常结束
	CLOSE_PRODUCT("CLOSE_PRODUCT","结课"), //结课
	UNVALID("UNVALID", "无效"); //无效
	
	private String value;
	private String name;
	
	private ContractProductStatus(String value, String name) {
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
