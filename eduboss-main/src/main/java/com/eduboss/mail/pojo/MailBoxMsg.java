package com.eduboss.mail.pojo;

import java.math.BigDecimal;

public class MailBoxMsg {

	private String mailAddr; //用户邮件地址: user_id@domain_name
	/*
	 *邮件总数 mbox_msgcnt
	 */
	private Integer mboxMsgCnt;
	/*
	 * 邮件大小mbox_msgsize
	 */
	private BigDecimal mboxMsgSize;
	/*
	 * 未读邮件数mbox_newmsgcnt
	 */
	private Integer mboxNewMsgCnt;
	/*
	 * 未读邮件大小mbox_newmsgsize
	 */
	private Integer mboxNewMsgSize;
	public String getMailAddr() {
		return mailAddr;
	}
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	public Integer getMboxMsgCnt() {
		return mboxMsgCnt;
	}
	public void setMboxMsgCnt(Integer mboxMsgCnt) {
		this.mboxMsgCnt = mboxMsgCnt;
	}
	public BigDecimal getMboxMsgSize() {
		return mboxMsgSize;
	}
	public void setMboxMsgSize(BigDecimal mboxMsgSize) {
		this.mboxMsgSize = mboxMsgSize;
	}
	public Integer getMboxNewMsgCnt() {
		return mboxNewMsgCnt;
	}
	public void setMboxNewMsgCnt(Integer mboxNewMsgCnt) {
		this.mboxNewMsgCnt = mboxNewMsgCnt;
	}
	public Integer getMboxNewMsgSize() {
		return mboxNewMsgSize;
	}
	public void setMboxNewMsgSize(Integer mboxNewMsgSize) {
		this.mboxNewMsgSize = mboxNewMsgSize;
	}
	
}
