package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 银联反馈支付数据Vo
 * @author lixuejun
 *
 */
public class PosPayDataToExcelVo {

	private String blBrenchName; // 所属分公司
	private String blCampusName; // 所属校区名称
	private String posNumber; // 终端编号
	private String merchantName; // 商户名称
	private String posId; // 系统流水号（交易参考号）
	private BigDecimal amount; // 交易金额
	private String posTime; // 交易时间
	private String createTime;
	private String createUserName;
	private String matchingStatusName;
	private String remark;
	public String getBlBrenchName() {
		return blBrenchName;
	}
	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getPosNumber() {
		return posNumber;
	}
	public void setPosNumber(String posNumber) {
		this.posNumber = posNumber;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getPosId() {
		return posId;
	}
	public void setPosId(String posId) {
		this.posId = posId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPosTime() {
		return posTime;
	}
	public void setPosTime(String posTime) {
		this.posTime = posTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getMatchingStatusName() {
		return matchingStatusName;
	}
	public void setMatchingStatusName(String matchingStatusName) {
		this.matchingStatusName = matchingStatusName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
