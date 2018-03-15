package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eduboss.common.EvidenceAuditStatus;

@Entity
@Table(name = "ods_month_payment_receipt_main")
public class OdsMonthPaymentRecieptMain implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	private User currentAuditUser;
	private String currentAuditTime;
	private User campusConfirmUser;
	private String campusConfirmTime;
	private User financeFirstAuditUser;
	private String financeFirstAuditTime;
	private User brenchConfirmUser;
	private String brenchConfirmTime;
	private User financeEndAuditUser;
	private String financeEndAuditTime;
	
	private BigDecimal LiveNewMoney;
	
	@Id
	@Column(name = "id", unique = true, nullable = false)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "RECEIPT_MONTH")
	public String getReceiptMonth() {
		return receiptMonth;
	}
	public void setReceiptMonth(String receiptMonth) {
		this.receiptMonth = receiptMonth;
	}
	@Column(name = "RECEIPT_DATE")
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "RECEIPT_STATUS")
	public EvidenceAuditStatus getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(EvidenceAuditStatus receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	
	@Column(name = "group_id")
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	
	@Column(name = " branch_id")
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	
	@Column(name = " campus_id")
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	
	@Column(name = "group_name")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name = "branch_Name")
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	@Column(name = "campus_name")
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	@Column(name = "new_money")
	public BigDecimal getNewMoney() {
		return newMoney;
	}
	public void setNewMoney(BigDecimal newMoney) {
		this.newMoney = newMoney;
	}
	
	@Column(name = "re_money")
	public BigDecimal getReMoney() {
		return reMoney;
	}
	public void setReMoney(BigDecimal reMoney) {
		this.reMoney = reMoney;
	}
	
	@Column(name = "all_money")
	public BigDecimal getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(BigDecimal allMoney) {
		this.allMoney = allMoney;
	}
	
	@Column(name = "wash_money")
	public BigDecimal getWashMoney() {
		return washMoney;
	}
	public void setWashMoney(BigDecimal washMoney) {
		this.washMoney = washMoney;
	}
	
	@Column(name = "total_money")
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Column(name = "bonus_money")
	public BigDecimal getBonusMoney() {
		return bonusMoney;
	}
	public void setBonusMoney(BigDecimal bonusMoney) {
		this.bonusMoney = bonusMoney;
	}
	
	@Column(name = "refund_money")
	public BigDecimal getRefundMoney() {
		return refundMoney;
	}
	public void setRefundMoney(BigDecimal refundMoney) {
		this.refundMoney = refundMoney;
	}
	
	@Column(name = "special_refund_money")
	public BigDecimal getSpecialRefundMoney() {
		return specialRefundMoney;
	}
	public void setSpecialRefundMoney(BigDecimal specialRefundMoney) {
		this.specialRefundMoney = specialRefundMoney;
	}
	
	@Column(name = "total_refund_money")
	public BigDecimal getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(BigDecimal totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
	}
	
	@Column(name = "refund_bonus_money")
	public BigDecimal getRefundBonusMoney() {
		return refundBonusMoney;
	}
	public void setRefundBonusMoney(BigDecimal refundBonusMoney) {
		this.refundBonusMoney = refundBonusMoney;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "modify_time")
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@Column(name = "modify_user")
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	@Column(name = "total_finace")
	public BigDecimal getTotalFinace() {
		return totalFinace;
	}
	public void setTotalFinace(BigDecimal totalFinace) {
		this.totalFinace = totalFinace;
	}
	
	@Column(name = "total_bonus")
	public BigDecimal getTotalBonus() {
		return totalBonus;
	}
	public void setTotalBonus(BigDecimal totalBonus) {
		this.totalBonus = totalBonus;
	}
	
	@Column(name = "modify_money")
	public BigDecimal getModifyMoney() {
		return modifyMoney;
	}
	public void setModifyMoney(BigDecimal modifyMoney) {
		this.modifyMoney = modifyMoney;
	}
	
	@Column(name = "after_modify_money")
	public BigDecimal getAfterModifyMoney() {
		return afterModifyMoney;
	}
	public void setAfterModifyMoney(BigDecimal afterModifyMoney) {
		this.afterModifyMoney = afterModifyMoney;
	}
	
	@Transient
	public String getReceiptStatusName() {
		return receiptStatus.getName();
	}
	public void setReceiptStatusName(String receiptStatusName) {
		this.receiptStatusName = receiptStatus.getName();
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENT_AUDIT_USER")
	public User getCurrentAuditUser() {
		return currentAuditUser;
	}

	public void setCurrentAuditUser(User currentAuditUser) {
		this.currentAuditUser = currentAuditUser;
	}

	@Column(name = "CURRENT_AUDIT_TIME", length = 20)
	public String getCurrentAuditTime() {
		return currentAuditTime;
	}

	public void setCurrentAuditTime(String currentAuditTime) {
		this.currentAuditTime = currentAuditTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAMPUS_CONFIRM_USER")
	public User getCampusConfirmUser() {
		return campusConfirmUser;
	}

	public void setCampusConfirmUser(User campusConfirmUser) {
		this.campusConfirmUser = campusConfirmUser;
	}

	@Column(name = "CAMPUS_CONFIRM_TIME", length = 20)
	public String getCampusConfirmTime() {
		return campusConfirmTime;
	}

	public void setCampusConfirmTime(String campusConfirmTime) {
		this.campusConfirmTime = campusConfirmTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FINANCE_FIRST_AUDIT_USER")
	public User getFinanceFirstAuditUser() {
		return financeFirstAuditUser;
	}

	public void setFinanceFirstAuditUser(User financeFirstAuditUser) {
		this.financeFirstAuditUser = financeFirstAuditUser;
	}

	@Column(name = "FINANCE_FIRST_AUDIT_TIME", length = 20)
	public String getFinanceFirstAuditTime() {
		return financeFirstAuditTime;
	}

	public void setFinanceFirstAuditTime(String financeFirstAuditTime) {
		this.financeFirstAuditTime = financeFirstAuditTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BRENCH_CONFIRM_USER")
	public User getBrenchConfirmUser() {
		return brenchConfirmUser;
	}

	public void setBrenchConfirmUser(User brenchConfirmUser) {
		this.brenchConfirmUser = brenchConfirmUser;
	}

	@Column(name = "BRENCH_CONFIRM_TIME", length = 20)
	public String getBrenchConfirmTime() {
		return brenchConfirmTime;
	}

	public void setBrenchConfirmTime(String brenchConfirmTime) {
		this.brenchConfirmTime = brenchConfirmTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FINANCE_END_AUDIT_USER")
	public User getFinanceEndAuditUser() {
		return financeEndAuditUser;
	}

	public void setFinanceEndAuditUser(User financeEndAuditUser) {
		this.financeEndAuditUser = financeEndAuditUser;
	}

	@Column(name = "FINANCE_END_AUDIT_TIME", length = 20)
	public String getFinanceEndAuditTime() {
		return financeEndAuditTime;
	}

	public void setFinanceEndAuditTime(String financeEndAuditTime) {
		this.financeEndAuditTime = financeEndAuditTime;
	}
	
	@Column(name = "live_new_money")
	public BigDecimal getLiveNewMoney() {
		return LiveNewMoney;
	}
	public void setLiveNewMoney(BigDecimal liveNewMoney) {
		LiveNewMoney = liveNewMoney;
	}
	
	
	
}
