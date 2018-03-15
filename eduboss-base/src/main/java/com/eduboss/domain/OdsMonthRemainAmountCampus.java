package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.EvidenceAuditStatus;

@Entity
@Table(name = "ods_month_remain_amount_campus")
public class OdsMonthRemainAmountCampus {

	private String id;
	private String groupId;
	private String brenchId;
	private String campusId;
	private BigDecimal realRemainAmountInit;
	private BigDecimal promotionRemainAmountInit;
	private BigDecimal realPaidAmountMid;
	private BigDecimal promotionPaidAmountMid;
	private BigDecimal historyWashAmountMid;
	private BigDecimal electronicTransferInMid;
	private BigDecimal realConsumeAmountMid;
	private BigDecimal promotionConsumeAmountMid;
	private BigDecimal isNormalRealAmountMid;
	private BigDecimal isNormalPromotionAmountMid;
	private BigDecimal realReturnFeeMid;
	private BigDecimal promotionReturnFeeMid;
	private BigDecimal electronicTransferOutMid;
	private BigDecimal realHistoryWashAmountMid;
	private BigDecimal promotionHistoryWashAmountMid;
	private BigDecimal realRemainAmountFinal;
	private BigDecimal promotionRemainAmountFinal;
	private String countDate;
	private String mappingDate;
	private EvidenceAuditStatus evidenceAuditStatus;
	
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
	
	public OdsMonthRemainAmountCampus() {
		super();
	}


	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 50)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "GROUP_ID", length = 32)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "BRENCH_ID", length = 32)
	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
	}

	@Column(name = "CAMPUS_ID", length = 32)
	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	@Column(name = "REAL_REMAIN_AMOUNT_INIT", precision = 10)
	public BigDecimal getRealRemainAmountInit() {
		return realRemainAmountInit;
	}

	public void setRealRemainAmountInit(BigDecimal realRemainAmountInit) {
		this.realRemainAmountInit = realRemainAmountInit;
	}

	@Column(name = "PROMOTION_REMAIN_AMOUNT_INIT", precision = 10)
	public BigDecimal getPromotionRemainAmountInit() {
		return promotionRemainAmountInit;
	}

	public void setPromotionRemainAmountInit(BigDecimal promotionRemainAmountInit) {
		this.promotionRemainAmountInit = promotionRemainAmountInit;
	}

	@Column(name = "REAL_PAID_AMOUNT_MID", precision = 10)
	public BigDecimal getRealPaidAmountMid() {
		return realPaidAmountMid;
	}

	public void setRealPaidAmountMid(BigDecimal realPaidAmountMid) {
		this.realPaidAmountMid = realPaidAmountMid;
	}

	@Column(name = "PROMOTION_PAID_AMOUNT_MID", precision = 10)
	public BigDecimal getPromotionPaidAmountMid() {
		return promotionPaidAmountMid;
	}

	public void setPromotionPaidAmountMid(BigDecimal promotionPaidAmountMid) {
		this.promotionPaidAmountMid = promotionPaidAmountMid;
	}

	@Column(name = "HISTORY_WASH_AMOUNT_MID", precision = 10)
	public BigDecimal getHistoryWashAmountMid() {
		return historyWashAmountMid;
	}

	public void setHistoryWashAmountMid(BigDecimal historyWashAmountMid) {
		this.historyWashAmountMid = historyWashAmountMid;
	}

	@Column(name = "ELECTRONIC_TRANSFER_IN_MID", precision = 10)
	public BigDecimal getElectronicTransferInMid() {
		return electronicTransferInMid;
	}

	public void setElectronicTransferInMid(BigDecimal electronicTransferInMid) {
		this.electronicTransferInMid = electronicTransferInMid;
	}

	@Column(name = "REAL_CONSUME_AMOUNT_MID", precision = 10)
	public BigDecimal getRealConsumeAmountMid() {
		return realConsumeAmountMid;
	}

	public void setRealConsumeAmountMid(BigDecimal realConsumeAmountMid) {
		this.realConsumeAmountMid = realConsumeAmountMid;
	}

	@Column(name = "PROMOTION_CONSUME_AMOUNT_MID", precision = 10)
	public BigDecimal getPromotionConsumeAmountMid() {
		return promotionConsumeAmountMid;
	}

	public void setPromotionConsumeAmountMid(BigDecimal promotionConsumeAmountMid) {
		this.promotionConsumeAmountMid = promotionConsumeAmountMid;
	}

	@Column(name = "IS_NORMAL_REAL_AMOUNT_MID", precision = 10)
	public BigDecimal getIsNormalRealAmountMid() {
		return isNormalRealAmountMid;
	}

	public void setIsNormalRealAmountMid(BigDecimal isNormalRealAmountMid) {
		this.isNormalRealAmountMid = isNormalRealAmountMid;
	}

	@Column(name = "IS_NORMAL_PROMOTION_AMOUNT_MID", precision = 10)
	public BigDecimal getIsNormalPromotionAmountMid() {
		return isNormalPromotionAmountMid;
	}

	public void setIsNormalPromotionAmountMid(BigDecimal isNormalPromotionAmountMid) {
		this.isNormalPromotionAmountMid = isNormalPromotionAmountMid;
	}

	@Column(name = "REAL_RETURN_FEE_MID", precision = 10)
	public BigDecimal getRealReturnFeeMid() {
		return realReturnFeeMid;
	}

	public void setRealReturnFeeMid(BigDecimal realReturnFeeMid) {
		this.realReturnFeeMid = realReturnFeeMid;
	}

	@Column(name = "PROMOTION_RETURN_FEE_MID", precision = 10)
	public BigDecimal getPromotionReturnFeeMid() {
		return promotionReturnFeeMid;
	}

	public void setPromotionReturnFeeMid(BigDecimal promotionReturnFeeMid) {
		this.promotionReturnFeeMid = promotionReturnFeeMid;
	}

	@Column(name = "ELECTRONIC_TRANSFER_OUT_MID", precision = 10)
	public BigDecimal getElectronicTransferOutMid() {
		return electronicTransferOutMid;
	}

	public void setElectronicTransferOutMid(BigDecimal electronicTransferOutMid) {
		this.electronicTransferOutMid = electronicTransferOutMid;
	}

	@Column(name = "REAL_HISTORY_WASH_AMOUNT_MID", precision = 10)
	public BigDecimal getRealHistoryWashAmountMid() {
		return realHistoryWashAmountMid;
	}

	public void setRealHistoryWashAmountMid(BigDecimal realHistoryWashAmountMid) {
		this.realHistoryWashAmountMid = realHistoryWashAmountMid;
	}

	@Column(name = "PROMOTION_HISTORY_WASH_AMOUNT_MID", precision = 10)
	public BigDecimal getPromotionHistoryWashAmountMid() {
		return promotionHistoryWashAmountMid;
	}

	public void setPromotionHistoryWashAmountMid(
			BigDecimal promotionHistoryWashAmountMid) {
		this.promotionHistoryWashAmountMid = promotionHistoryWashAmountMid;
	}

	@Column(name = "REAL_REMAIN_AMOUNT_FINAL", precision = 10)
	public BigDecimal getRealRemainAmountFinal() {
		return realRemainAmountFinal;
	}

	public void setRealRemainAmountFinal(BigDecimal realRemainAmountFinal) {
		this.realRemainAmountFinal = realRemainAmountFinal;
	}

	@Column(name = "PROMOTION_REMAIN_AMOUNT_FINAL", precision = 10)
	public BigDecimal getPromotionRemainAmountFinal() {
		return promotionRemainAmountFinal;
	}

	public void setPromotionRemainAmountFinal(BigDecimal promotionRemainAmountFinal) {
		this.promotionRemainAmountFinal = promotionRemainAmountFinal;
	}

	@Column(name = "COUNT_DATE", length = 10)
	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	@Column(name = "MAPPING_DATE", length = 10)
	public String getMappingDate() {
		return mappingDate;
	}

	public void setMappingDate(String mappingDate) {
		this.mappingDate = mappingDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "EVIDENCE_AUDIT_STATUS", length = 32)
	public EvidenceAuditStatus getEvidenceAuditStatus() {
		return evidenceAuditStatus;
	}

	public void setEvidenceAuditStatus(EvidenceAuditStatus evidenceAuditStatus) {
		this.evidenceAuditStatus = evidenceAuditStatus;
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
}
