package com.eduboss.common;

public enum ChargeType {
	
	NORMAL("NORMAL", "正常"),//正常 
	REFUND_CONSUME("REFUND_CONSUME", "退费消费"),//退费消费
	FEEDBACK_REFUND("FEEDBACK_REFUND", "退费返还"), // 退费返还
	TRANSFER_NORMAL_TO_ELECT_ACC("TRANSFER_NORMAL_TO_ELECT_ACC", "转移正常资金到学生电子账户"),
	TRANSFER_PROMOTION_TO_ELECT_ACC("TRANSFER_PROMOTION_TO_ELECT_ACC", "转移优惠资金到学生电子账户"),
	IS_NORMAL_INCOME("IS_NORMAL_INCOME", "划归收入"),
	PROMOTION_RETURN("PROMOTION_RETURN", "优惠对消");
	
	
	
	
	
	private String value;
	private String name;
	
	private ChargeType(String value, String name) {
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
