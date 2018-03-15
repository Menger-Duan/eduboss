package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class LiveTransferPayVo {

	private String id;
	private BigDecimal amount = BigDecimal.ZERO;
	private String retcode;//SUCCESS/FAIL
	private String liveType;//NEW/RENEW
	private String financeType;//INCOME/REFUND
	private String createTime;
	private String modifyTime;
	private String remark;
	private String trxid;
	private String chnltrxid;
	private String payType;
	private String trxstatus;
	private String title;
	private String payInfo;
	private String finishTime;
	private String errmsg;
	private String reqsn;
	private String transactionNum;
	private String callbackUrl;
	private String campusId;
	private BigDecimal activeTime = BigDecimal.ZERO;//不传默认30
	private BigDecimal group = BigDecimal.ZERO;
	
	private String url;
	private String payTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getLiveType() {
		return liveType;
	}
	public void setLiveType(String liveType) {
		this.liveType = liveType;
	}
	public String getFinanceType() {
		return financeType;
	}
	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public String getChnltrxid() {
		return chnltrxid;
	}
	public void setChnltrxid(String chnltrxid) {
		this.chnltrxid = chnltrxid;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTrxstatus() {
		return trxstatus;
	}
	public void setTrxstatus(String trxstatus) {
		this.trxstatus = trxstatus;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	public String getTransactionNum() {
		return transactionNum;
	}
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	public String getCallbackUrl() {
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	
	public BigDecimal getActiveTime() {
		return activeTime;
	}
	public void setActiveTime(BigDecimal activeTime) {
		this.activeTime = activeTime;
	}
	public BigDecimal getGroup() {
		return group;
	}
	public void setGroup(BigDecimal group) {
		this.group = group;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	
}
