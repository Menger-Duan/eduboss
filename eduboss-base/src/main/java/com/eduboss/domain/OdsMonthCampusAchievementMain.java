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
/**
 * 
 * @author dmr
 *
 */
@Entity
@Table(name = "ods_month_campus_achievement_main")
public class OdsMonthCampusAchievementMain implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
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
	
	@Column(name = "all_money")
	public BigDecimal getAllMoney() {
		return allMoney;
	}
	public void setAllMoney(BigDecimal allMoney) {
		this.allMoney = allMoney;
	}
	
	@Column(name = "total_money")
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Column(name = "total_refund_money")
	public BigDecimal getTotalRefundMoney() {
		return totalRefundMoney;
	}
	public void setTotalRefundMoney(BigDecimal totalRefundMoney) {
		this.totalRefundMoney = totalRefundMoney;
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
	
	@Column(name = "campus_amount_new")
	public BigDecimal getCampusAmountNew() {
		return campusAmountNew;
	}
	public void setCampusAmountNew(BigDecimal campusAmountNew) {
		this.campusAmountNew = campusAmountNew;
	}
	
	@Column(name = "campus_amount_re")
	public BigDecimal getCampusAmountRe() {
		return campusAmountRe;
	}
	public void setCampusAmountRe(BigDecimal campusAmountRe) {
		this.campusAmountRe = campusAmountRe;
	}
	
	@Column(name = "refund_amount")
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}
	
	@Column(name = "live_income_new")
	public BigDecimal getLiveIncomeNew() {
		return liveIncomeNew;
	}
	public void setLiveIncomeNew(BigDecimal liveIncomeNew) {
		this.liveIncomeNew = liveIncomeNew;
	}
	
	@Column(name = "live_income_renew")
	public BigDecimal getLiveIncomeRenew() {
		return liveIncomeRenew;
	}
	public void setLiveIncomeRenew(BigDecimal liveIncomeRenew) {
		this.liveIncomeRenew = liveIncomeRenew;
	}
	
	@Column(name = "live_refund_new")
	public BigDecimal getLiveRefundNew() {
		return liveRefundNew;
	}
	public void setLiveRefundNew(BigDecimal liveRefundNew) {
		this.liveRefundNew = liveRefundNew;
	}
	
	@Column(name = "live_refund_renew")
	public BigDecimal getLiveRefundRenew() {
		return liveRefundRenew;
	}
	public void setLiveRefundRenew(BigDecimal liveRefundRenew) {
		this.liveRefundRenew = liveRefundRenew;
	}
	
	
	@Column(name = "live_total_money")
	public BigDecimal getLiveTotalMoney() {
		return liveTotalMoney;
	}
	public void setLiveTotalMoney(BigDecimal liveTotalMoney) {
		this.liveTotalMoney = liveTotalMoney;
	}
	
	@Column(name = "total_income_money")
	public BigDecimal getTotalIncomeMoney() {
		return totalIncomeMoney;
	}
	public void setTotalIncomeMoney(BigDecimal totalIncomeMoney) {
		this.totalIncomeMoney = totalIncomeMoney;
	}
	
	
	
	
}
