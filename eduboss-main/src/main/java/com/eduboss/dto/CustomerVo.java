package com.eduboss.dto;



/**
 * 专门用于接收客户资源参数的 VO
 * @author robinzhang
 *
 */
public class CustomerVo {
	private String cusName;
	private int cusPhone;
	
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public int getCusPhone() {
		return cusPhone;
	}
	public void setCusPhone(int cusPhone) {
		this.cusPhone = cusPhone;
	}
	
	
}
