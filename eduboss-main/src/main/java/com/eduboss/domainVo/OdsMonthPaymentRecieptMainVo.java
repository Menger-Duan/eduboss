package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.EvidenceAuditStatus;

public class OdsMonthPaymentRecieptMainVo {

	private String id;
	private String groupId;
	private String branchId;
	private String campusId;
	private String groupName;
	private String branchName;
	private String campusName;

	private BigDecimal newMoney            ;
	private BigDecimal reMoney             ;
	private BigDecimal allMoney            ;
	private BigDecimal washMoney           ;
	private BigDecimal totalMoney          ;
	private BigDecimal bonusMoney          ;
	private BigDecimal refundMoney         ;
	private BigDecimal specialRefundMoney ;
	private BigDecimal totalRefundMoney   ;
	private BigDecimal refundBonusMoney   ;
	private String receiptMonth        ;
	private String receiptDate         ;
	private EvidenceAuditStatus receiptStatus       ;
	private String remark               ;
	private String modifyTime          ;
	private String modifyUser                    ;
	private BigDecimal totalFinace         ;
	private BigDecimal totalBonus          ;
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
	public BigDecimal getNewMoney() {
		return newMoney;
	}
	public void setNewMoney(BigDecimal newMoney) {
		this.newMoney = newMoney;
	}
	public BigDecimal getReMoney() {
		return reMoney;
	}
	public void setReMoney(BigDecimal reMoney) {
		this.reMoney = reMoney;
	}
	public BigDecimal getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(BigDecimal allMoney) {
		this.allMoney = allMoney;
	}
	public BigDecimal getWashMoney() {
		return washMoney;
	}
	public void setWashMoney(BigDecimal washMoney) {
		this.washMoney = washMoney;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public BigDecimal getBonusMoney() {
		return bonusMoney;
	}
	public void setBonusMoney(BigDecimal bonusMoney) {
		this.bonusMoney = bonusMoney;
	}
	public BigDecimal getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}
	public BigDecimal getSpecialRefundMoney() {
		return specialRefundMoney;
	}
	public void setSpecialRefundMoney(BigDecimal specialRefundMoney) {
		this.specialRefundMoney = specialRefundMoney;
	}
	public BigDecimal getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(BigDecimal totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	public BigDecimal getRefundBonusMoney() {
		return refundBonusMoney;
	}
	public void setRefundBonusMoney(BigDecimal refundBonusMoney) {
		this.refundBonusMoney = refundBonusMoney;
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
	public BigDecimal getTotalFinace() {
		return totalFinace;
	}
	public void setTotalFinace(BigDecimal totalFinace) {
		this.totalFinace = totalFinace;
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
	
}
