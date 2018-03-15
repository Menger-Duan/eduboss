package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.EvidenceAuditStatus;

/**
 * 
 * @author dmr
 *
 */
public class OdsMonthCampusAchievementMainVo {

	private String id;
	private String groupId;
	private String branchId;
	private String campusId;
	private String groupName;
	private String branchName;
	private String campusName;

	private BigDecimal campusAmountNew            ;
	private BigDecimal campusAmountRe             ;
	private BigDecimal allMoney            ;
	private BigDecimal refundAmount           ;
	private BigDecimal totalMoney          ;
	private BigDecimal liveIncomeNew          ;
	private BigDecimal liveIncomeRenew         ;
	private BigDecimal liveRefundNew ;
	private BigDecimal liveRefundRenew   ;
	private BigDecimal liveTotalMoney   ;
	private BigDecimal totalIncomeMoney ;
	private BigDecimal totalRefundMoney   ;
	private BigDecimal totalBonus   ;
	private String receiptMonth        ;
	private String receiptDate         ;
	private EvidenceAuditStatus receiptStatus       ;
	private String remark               ;
	private String modifyTime          ;
	private String modifyUser                    ;
	private BigDecimal modifyMoney         ;
	private BigDecimal afterModifyMoney;
	private String receiptStatusName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	public BigDecimal getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(BigDecimal allMoney) {
		this.allMoney = allMoney;
	}
	
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	public BigDecimal getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(BigDecimal totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	
	public String getReceiptMonth() {
		return receiptMonth;
	}
	public void setReceiptMonth(String receiptMonth) {
		this.receiptMonth = receiptMonth;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public EvidenceAuditStatus getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(EvidenceAuditStatus receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	public BigDecimal getTotalBonus() {
		return totalBonus;
	}
	public void setTotalBonus(BigDecimal totalBonus) {
		this.totalBonus = totalBonus;
	}
	public BigDecimal getModifyMoney() {
		return modifyMoney;
	}
	public void setModifyMoney(BigDecimal modifyMoney) {
		this.modifyMoney = modifyMoney;
	}
	public BigDecimal getAfterModifyMoney() {
		return afterModifyMoney;
	}
	public void setAfterModifyMoney(BigDecimal afterModifyMoney) {
		this.afterModifyMoney = afterModifyMoney;
	}
	public String getReceiptStatusName() {
		return receiptStatusName;
	}
	public void setReceiptStatusName(String receiptStatusName) {
		this.receiptStatusName = receiptStatusName;
	}
	public BigDecimal getCampusAmountNew() {
		return campusAmountNew;
	}
	public void setCampusAmountNew(BigDecimal campusAmountNew) {
		this.campusAmountNew = campusAmountNew;
	}
	public BigDecimal getCampusAmountRe() {
		return campusAmountRe;
	}
	public void setCampusAmountRe(BigDecimal campusAmountRe) {
		this.campusAmountRe = campusAmountRe;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	public BigDecimal getLiveIncomeNew() {
		return liveIncomeNew;
	}
	public void setLiveIncomeNew(BigDecimal liveIncomeNew) {
		this.liveIncomeNew = liveIncomeNew;
	}
	public BigDecimal getLiveIncomeRenew() {
		return liveIncomeRenew;
	}
	public void setLiveIncomeRenew(BigDecimal liveIncomeRenew) {
		this.liveIncomeRenew = liveIncomeRenew;
	}
	public BigDecimal getLiveRefundNew() {
		return liveRefundNew;
	}
	public void setLiveRefundNew(BigDecimal liveRefundNew) {
		this.liveRefundNew = liveRefundNew;
	}
	public BigDecimal getLiveRefundRenew() {
		return liveRefundRenew;
	}
	public void setLiveRefundRenew(BigDecimal liveRefundRenew) {
		this.liveRefundRenew = liveRefundRenew;
	}
	public BigDecimal getLiveTotalMoney() {
		return liveTotalMoney;
	}
	public void setLiveTotalMoney(BigDecimal liveTotalMoney) {
		this.liveTotalMoney = liveTotalMoney;
	}
	public BigDecimal getTotalIncomeMoney() {
		return totalIncomeMoney;
	}
	public void setTotalIncomeMoney(BigDecimal totalIncomeMoney) {
		this.totalIncomeMoney = totalIncomeMoney;
	}
	
	
}
