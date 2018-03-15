package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.dto.Response;

/**
 * 直播平台，支付二维码
 *
 */
@Entity
@Table(name = "live_transfer_pay")
public class LiveTransferPay extends Response implements java.io.Serializable {

	// Fields

	private String id;
	private BigDecimal amount = BigDecimal.ZERO;
	private String retcode;//SUCCESS/FAIL
	private String retmsg;
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
	private String status;//0未支付，1支付成功未记录流水，2流水记录成功,3支付失败
	private String payTime;
	
	private String campusId;
	
	public LiveTransferPay() {
		super();
	}


	public LiveTransferPay(String id, BigDecimal amount, String retcode, String retmsg, String liveType,
			String financeType, String createTime, String modifyTime, String remark, String trxid, String chnltrxid,
			String payType, String trxstatus, String title, String payInfo, String finishTime, String errmsg,
			String reqsn, String transactionNum, String callbackUrl, String status, String payTime, String campusId) {
		super();
		this.id = id;
		this.amount = amount;
		this.retcode = retcode;
		this.retmsg = retmsg;
		this.liveType = liveType;
		this.financeType = financeType;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
		this.trxid = trxid;
		this.chnltrxid = chnltrxid;
		this.payType = payType;
		this.trxstatus = trxstatus;
		this.title = title;
		this.payInfo = payInfo;
		this.finishTime = finishTime;
		this.errmsg = errmsg;
		this.reqsn = reqsn;
		this.transactionNum = transactionNum;
		this.callbackUrl = callbackUrl;
		this.status = status;
		this.payTime = payTime;
		this.campusId = campusId;
	}

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	@Column(name = "amount", precision = 10 ,scale = 2)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name = "retcode", length = 10)
	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	@Column(name = "live_type", length = 10)
	public String getLiveType() {
		return liveType;
	}

	public void setLiveType(String liveType) {
		this.liveType = liveType;
	}
	
	@Column(name = "finance_type", length = 10)
	public String getFinanceType() {
		return financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}
	
	@Column(name = "create_time", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modify_time", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "trxid", length = 20)
	public String getTrxid() {
		return trxid;
	}

	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}

	@Column(name = "chnltrxid", length = 50)
	public String getChnltrxid() {
		return chnltrxid;
	}

	public void setChnltrxid(String chnltrxid) {
		this.chnltrxid = chnltrxid;
	}

	@Column(name = "pay_type", length = 10)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "trxstatus", length = 10)
	public String getTrxstatus() {
		return trxstatus;
	}

	public void setTrxstatus(String trxstatus) {
		this.trxstatus = trxstatus;
	}

	@Column(name = "title", length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "pay_info", length = 1000)
	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	
	@Column(name = "finish_time", length = 20)
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	
	@Column(name = "errmsg", length = 100)
	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	@Column(name = "reqsn", length = 32)
	public String getReqsn() {
		return reqsn;
	}

	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	
	@Column(name = "transaction_num", length = 32)
	public String getTransactionNum() {
		return transactionNum;
	}

	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	
	@Column(name = "callback_url", length = 1000)
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}
	
	@Column(name = "retmsg", length = 100)
	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}
	
	@Column(name = "status", length = 5)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "pay_time", length = 20)
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	@Column(name = "campus_id", length = 32)
	public String getCampusId() {
		return campusId;
	}


	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}


	@Override
	public String toString() {
		return "LiveTransferPay [id=" + id + ", amount=" + amount + ", retcode=" + retcode + ", retmsg=" + retmsg
				+ ", liveType=" + liveType + ", financeType=" + financeType + ", createTime=" + createTime
				+ ", modifyTime=" + modifyTime + ", remark=" + remark + ", trxid=" + trxid + ", chnltrxid=" + chnltrxid
				+ ", payType=" + payType + ", trxstatus=" + trxstatus + ", title=" + title + ", payInfo=" + payInfo
				+ ", finishTime=" + finishTime + ", errmsg=" + errmsg + ", reqsn=" + reqsn + ", transactionNum="
				+ transactionNum + ", callbackUrl=" + callbackUrl + ", status=" + status + ", payTime=" + payTime
				+ ", campusId=" + campusId + "]";
	}






	
}