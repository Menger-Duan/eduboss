package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "intenet_pay")
public class IntenetPay {

	private int id;
	private BigDecimal amount;
	private String retcode;
	private FundsChangeHistory fundsChange;
	private String modifyTime;
	private User modifyUser;
	private String remark;
	private String trxid;
	private String chnltrxid;
	private String payType;
	private String title;
	private String trxstatus;
	private String payInfo;
	private String finishTime;
	private String errorMsg;
	private User createUser;
	private String createTime;
	private String retmsg;
	private String reqsn;
	private String contractId;
	private String status;
	private int version;

	private String authcode;//微星支付宝付款二维码
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Version
	@Column(name = "VERSION", nullable=false,unique=true)
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "AMOUNT")
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


	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FUNDS_CHANGE_ID")
	public FundsChangeHistory getFundsChange() {
		return fundsChange;
	}


	public void setFundsChange(FundsChangeHistory fundsChange) {
		this.fundsChange = fundsChange;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MODIFY_USER")
	public User getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "TRXID", length = 20)
	public String getTrxid() {
		return trxid;
	}

	public void setTrxid(String trxid) {
		this.trxid = trxid;
	}

	@Column(name = "CHNLTRIXID", length = 50)
	public String getChnltrxid() {
		return chnltrxid;
	}

	public void setChnltrxid(String chnltrxid) {
		this.chnltrxid = chnltrxid;
	}
	
	@Column(name = "PAY_TYPE", length = 20)
	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "TITLE", length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "trxstatus", length = 10)
	public String getTrxstatus() {
		return trxstatus;
	}

	public void setTrxstatus(String trxstatus) {
		this.trxstatus = trxstatus;
	}

	@Column(name = "PAY_INFO", length = 1000)
	public String getPayInfo() {
		return payInfo;
	}

	

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	@Column(name = "FINISH_TIME", length = 20)
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	@Column(name = "ERRMSG", length = 100)
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="CREATE_USER")
	public User getCreateUser() {
		return createUser;
	}

	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "retmsg", length = 100)
	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	@Column(name = "reqsn", length = 32)
	public String getReqsn() {
		return reqsn;
	}

	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}

	@Column(name = "contract_id", length = 32)
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	@Column(name = "status", length = 10)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "AUTH_CODE", length = 18)
	public String getAuthcode() {
		return authcode;
	}

	public void setAuthcode(String authcode) {
		this.authcode = authcode;
	}
}
