package com.eduboss.domainVo;

import java.math.BigDecimal;


public class IntenetPayVo {

	private int id;
	private BigDecimal amount;
	private String retcode;
	private String fundsChangeId;
	private String remark;
	private String trxid;
	private String chnltrxid;
	private String payType;
	private String title;
	private String trxstatus;
	private String payInfo;
	private String finishTime;
	private String errorMsg;
	private String retmsg;
	private String reqsn;
	private String contractId;
	private String url;
	private String status;
	private String authcode;

	/**
	* 帐号信息
	*/
	private String cusId;
	private String appId;
	private String appKey;
	/**
	 * 组织架构ID
	 */
	private String organizationId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getFundsChangeId() {
		return fundsChangeId;
	}
	public void setFundsChangeId(String fundsChangeId) {
		this.fundsChangeId = fundsChangeId;
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
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTrxstatus() {
		return trxstatus;
	}
	public void setTrxstatus(String trxstatus) {
		this.trxstatus = trxstatus;
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
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getRetmsg() {
		return retmsg;
	}
	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	public String getChnltrxid() {
		return chnltrxid;
	}
	public void setChnltrxid(String chnltrxid) {
		this.chnltrxid = chnltrxid;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	public String getRetcode() {
		return retcode;
	}
	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}

	public String getCusId() {
		return cusId;
	}

	public void setCusId(String cusId) {
		this.cusId = cusId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
}
