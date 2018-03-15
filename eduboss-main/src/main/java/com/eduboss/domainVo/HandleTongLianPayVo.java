package com.eduboss.domainVo;

import java.io.Serializable;

public class HandleTongLianPayVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;//LIVE/BOSS
	private String trxid;
	private String reqsn;
	
	public HandleTongLianPayVo() {
		super();
	}
	public HandleTongLianPayVo(String type, String trxid, String reqsn) {
		super();
		this.type = type;
		this.trxid = trxid;
		this.reqsn = reqsn;
	}
	public String getTrxid() {
		return trxid;
	}
	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "handleTongLianPayVo [type=" + type + ", trxid=" + trxid + ", reqsn=" + reqsn + "]";
	}
	
	
	
}
