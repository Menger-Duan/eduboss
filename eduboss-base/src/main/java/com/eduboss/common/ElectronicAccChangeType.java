package com.eduboss.common;

public enum ElectronicAccChangeType {
	/************** FBI warning  *********收入用IN结尾，支出用OUT*******************************/
	RECHARGE("RECHARGE", "财务充值"),//电子账户充值（预留）（变更为财务充值 by 郭诗博 redmine id#333）
	REFUND_IN("REFUND_IN", "退费到电子账户"),//退费到电子账户
	RECEIPT_IN("RECEIPT_IN", "电子账户转账收款"),//电子账户收款（转账收款）
	WASH_IN("WASH_IN", "电子账户冲销收款"),//电子账户冲销（划归收入冲销）   已经无用了。。。。。以前用作回滚的。
	NORMAL_INCOME_OUT("NORMAL_INCOME_OUT", "电子账户划归收入"),//电子账户划归收入
	PAY_OUT("PAY_OUT", "电子账户支付"),//电子账户支付 （合同用电子账户付款）
	TRANSFER_OUT("TRANSFER_OUT", "电子账户转账出款"),//电子账户出款 （转账出款）
	REFUND_OUT("REFUND_OUT", "电子账户退费");//电子账户退费
	
	private String value;
	private String name;
	
	private ElectronicAccChangeType(String value, String name) {
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
