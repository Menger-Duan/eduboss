package com.eduboss.common;

/**
 * 付款方式
 * @author ndd
 * 2014-8-1
 */
public enum PayWay {
	CASH("CASH", "现金"),//现金
	POS("POS", "POS"),//刷卡
	WEB_CHART_PAY("WEB_CHART_PAY", "微信支付"),//刷卡
	ALI_PAY("ALI_PAY", "支付宝支付"),//刷卡
	BANK_TRANSFER("BANK_TRANSFER", "银行汇款"),//刷卡
	FEEDBACK_REFUND("FEEDBACK_REFUND", "退费返还"), //退费返还
    ELECTRONIC_ACCOUNT("ELECTRONIC_ACCOUNT", "电子账户") // 电子账户
    //TRANSFER_ACCOUNTS("TRANSFER_ACCOUNTS", "转账"),//转账
    , REFUND_NORMAL_AMOUNT("REFUND_NORMAL_AMOUNT","基本账户退费")
    , REFUND_PROMOTION_AMOUNT("REFUND_PROMOTION_AMOUNT","优惠账户退费")
	, REFUND_MONEY("REFUND_MONEY", "退费")
	, PROMOTION_MONEY("PROMOTION_MONEY", "优惠支付")
	,IS_NORMAL_INCOME("IS_NORMAL_INCOME","划归收入")
    ;
	private String value;
	private String name;
	
	private PayWay(String value, String name) {
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
