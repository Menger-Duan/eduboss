package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 银联反馈支付数据Vo
 * @author lixuejun
 *
 */
public class PosPayDataVo {

	private int id;
	private String posId; // 系统流水号（交易参考号）
	private String posTime; // 交易时间
	private BigDecimal amount; // 交易金额
	private String posNumber; // 终端编号
	private String posAccount; // 交易账号
	private String merchantName; // 商户名称
	private BigDecimal poundage; // 商户手续费
	private String matchingStatusName;
	private String matchingStatusValue;
	private String remark;
	private String blBrenchId;
	private String blBrenchName;
	private String blCampusId;
	private String blCampusName;
	private String createUserId;
	private String createUserName;
	private String createTime;
	private String startDate;
	private String endDate;
	private String posTypeName;
	private String posType;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public String getPosTime() {
		return posTime;
	}
	public void setPosTime(String posTime) {
		this.posTime = posTime;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPosNumber() {
		return posNumber;
	}
	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}
	public String getPosAccount() {
		return posAccount;
	}
	public void setPosAccount(String posAccount) {
		this.posAccount = posAccount;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public BigDecimal getPoundage() {
		return poundage;
	}
	public void setPoundage(BigDecimal poundage) {
		this.poundage = poundage;
	}
	public String getMatchingStatusName() {
		return matchingStatusName;
	}
	public void setMatchingStatusName(String matchingStatusName) {
		this.matchingStatusName = matchingStatusName;
	}
	public String getMatchingStatusValue() {
		return matchingStatusValue;
	}
	public void setMatchingStatusValue(String matchingStatusValue) {
		this.matchingStatusValue = matchingStatusValue;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBlBrenchId() {
		return blBrenchId;
	}
	public void setBlBrenchId(String blBrenchId) {
		this.blBrenchId = blBrenchId;
	}
	public String getBlBrenchName() {
		return blBrenchName;
	}
	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    public String getPosTypeName() {
        return posTypeName;
    }
    public void setPosTypeName(String posTypeName) {
        this.posTypeName = posTypeName;
    }
    public String getPosType() {
        return posType;
    }
    public void setPosType(String posType) {
        this.posType = posType;
    }
	
}
