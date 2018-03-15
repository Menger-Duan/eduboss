package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * OdsDayFinanceId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class OdsDayFinanceId implements java.io.Serializable {

	// Fields

	private String groupId;
	private String branchId;
	private String campusId;
	private String userId;
	private String countDate;
	private Double countOnoOnOne;
	private Double countMiniClass;
	private Double countPromise;
	private Double countOthers;
	private Double countTotal;
	private Double countCharged;
	private Double countPaddingPay;
	private Double countClassAmount;
	private Double countRefundAmount;

	// Constructors

	/** default constructor */
	public OdsDayFinanceId() {
	}

	/** full constructor */
	public OdsDayFinanceId(String groupId, String branchId, String campusId,
			String userId, String countDate, Double countOnoOnOne,
			Double countMiniClass, Double countPromise, Double countOthers,
			Double countTotal, Double countCharged, Double countPaddingPay,
			Double countClassAmount, Double countRefundAmount) {
		this.groupId = groupId;
		this.branchId = branchId;
		this.campusId = campusId;
		this.userId = userId;
		this.countDate = countDate;
		this.countOnoOnOne = countOnoOnOne;
		this.countMiniClass = countMiniClass;
		this.countPromise = countPromise;
		this.countOthers = countOthers;
		this.countTotal = countTotal;
		this.countCharged = countCharged;
		this.countPaddingPay = countPaddingPay;
		this.countClassAmount = countClassAmount;
		this.countRefundAmount = countRefundAmount;
	}

	// Property accessors

	@Column(name = "GROUP_ID", length = 32)
	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Column(name = "BRANCH_ID", length = 32)
	public String getBranchId() {
		return this.branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	@Column(name = "CAMPUS_ID", length = 32)
	public String getCampusId() {
		return this.campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "COUNT_DATE", length = 10)
	public String getCountDate() {
		return this.countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	@Column(name = "COUNT_ONO_ON_ONE", precision = 10)
	public Double getCountOnoOnOne() {
		return this.countOnoOnOne;
	}

	public void setCountOnoOnOne(Double countOnoOnOne) {
		this.countOnoOnOne = countOnoOnOne;
	}

	@Column(name = "COUNT_MINI_CLASS", precision = 10)
	public Double getCountMiniClass() {
		return this.countMiniClass;
	}

	public void setCountMiniClass(Double countMiniClass) {
		this.countMiniClass = countMiniClass;
	}

	@Column(name = "COUNT_PROMISE", precision = 10)
	public Double getCountPromise() {
		return this.countPromise;
	}

	public void setCountPromise(Double countPromise) {
		this.countPromise = countPromise;
	}

	@Column(name = "COUNT_OTHERS", precision = 10)
	public Double getCountOthers() {
		return this.countOthers;
	}

	public void setCountOthers(Double countOthers) {
		this.countOthers = countOthers;
	}

	@Column(name = "COUNT_TOTAL", precision = 10)
	public Double getCountTotal() {
		return this.countTotal;
	}

	public void setCountTotal(Double countTotal) {
		this.countTotal = countTotal;
	}

	@Column(name = "COUNT_CHARGED", precision = 10)
	public Double getCountCharged() {
		return this.countCharged;
	}

	public void setCountCharged(Double countCharged) {
		this.countCharged = countCharged;
	}

	@Column(name = "COUNT_PADDING_PAY", precision = 10)
	public Double getCountPaddingPay() {
		return this.countPaddingPay;
	}

	public void setCountPaddingPay(Double countPaddingPay) {
		this.countPaddingPay = countPaddingPay;
	}

	@Column(name = "COUNT_CLASS_AMOUNT", precision = 10)
	public Double getCountClassAmount() {
		return this.countClassAmount;
	}

	public void setCountClassAmount(Double countClassAmount) {
		this.countClassAmount = countClassAmount;
	}

	@Column(name = "COUNT_REFUND_AMOUNT", precision = 10)
	public Double getCountRefundAmount() {
		return this.countRefundAmount;
	}

	public void setCountRefundAmount(Double countRefundAmount) {
		this.countRefundAmount = countRefundAmount;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OdsDayFinanceId))
			return false;
		OdsDayFinanceId castOther = (OdsDayFinanceId) other;

		return ((this.getGroupId() == castOther.getGroupId()) || (this
				.getGroupId() != null && castOther.getGroupId() != null && this
				.getGroupId().equals(castOther.getGroupId())))
				&& ((this.getBranchId() == castOther.getBranchId()) || (this
						.getBranchId() != null
						&& castOther.getBranchId() != null && this
						.getBranchId().equals(castOther.getBranchId())))
				&& ((this.getCampusId() == castOther.getCampusId()) || (this
						.getCampusId() != null
						&& castOther.getCampusId() != null && this
						.getCampusId().equals(castOther.getCampusId())))
				&& ((this.getUserId() == castOther.getUserId()) || (this
						.getUserId() != null && castOther.getUserId() != null && this
						.getUserId().equals(castOther.getUserId())))
				&& ((this.getCountDate() == castOther.getCountDate()) || (this
						.getCountDate() != null
						&& castOther.getCountDate() != null && this
						.getCountDate().equals(castOther.getCountDate())))
				&& ((this.getCountOnoOnOne() == castOther.getCountOnoOnOne()) || (this
						.getCountOnoOnOne() != null
						&& castOther.getCountOnoOnOne() != null && this
						.getCountOnoOnOne()
						.equals(castOther.getCountOnoOnOne())))
				&& ((this.getCountMiniClass() == castOther.getCountMiniClass()) || (this
						.getCountMiniClass() != null
						&& castOther.getCountMiniClass() != null && this
						.getCountMiniClass().equals(
								castOther.getCountMiniClass())))
				&& ((this.getCountPromise() == castOther.getCountPromise()) || (this
						.getCountPromise() != null
						&& castOther.getCountPromise() != null && this
						.getCountPromise().equals(castOther.getCountPromise())))
				&& ((this.getCountOthers() == castOther.getCountOthers()) || (this
						.getCountOthers() != null
						&& castOther.getCountOthers() != null && this
						.getCountOthers().equals(castOther.getCountOthers())))
				&& ((this.getCountTotal() == castOther.getCountTotal()) || (this
						.getCountTotal() != null
						&& castOther.getCountTotal() != null && this
						.getCountTotal().equals(castOther.getCountTotal())))
				&& ((this.getCountCharged() == castOther.getCountCharged()) || (this
						.getCountCharged() != null
						&& castOther.getCountCharged() != null && this
						.getCountCharged().equals(castOther.getCountCharged())))
				&& ((this.getCountPaddingPay() == castOther
						.getCountPaddingPay()) || (this.getCountPaddingPay() != null
						&& castOther.getCountPaddingPay() != null && this
						.getCountPaddingPay().equals(
								castOther.getCountPaddingPay())))
				&& ((this.getCountClassAmount() == castOther
						.getCountClassAmount()) || (this.getCountClassAmount() != null
						&& castOther.getCountClassAmount() != null && this
						.getCountClassAmount().equals(
								castOther.getCountClassAmount())))
				&& ((this.getCountRefundAmount() == castOther
						.getCountRefundAmount()) || (this
						.getCountRefundAmount() != null
						&& castOther.getCountRefundAmount() != null && this
						.getCountRefundAmount().equals(
								castOther.getCountRefundAmount())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getGroupId() == null ? 0 : this.getGroupId().hashCode());
		result = 37 * result
				+ (getBranchId() == null ? 0 : this.getBranchId().hashCode());
		result = 37 * result
				+ (getCampusId() == null ? 0 : this.getCampusId().hashCode());
		result = 37 * result
				+ (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result
				+ (getCountDate() == null ? 0 : this.getCountDate().hashCode());
		result = 37
				* result
				+ (getCountOnoOnOne() == null ? 0 : this.getCountOnoOnOne()
						.hashCode());
		result = 37
				* result
				+ (getCountMiniClass() == null ? 0 : this.getCountMiniClass()
						.hashCode());
		result = 37
				* result
				+ (getCountPromise() == null ? 0 : this.getCountPromise()
						.hashCode());
		result = 37
				* result
				+ (getCountOthers() == null ? 0 : this.getCountOthers()
						.hashCode());
		result = 37
				* result
				+ (getCountTotal() == null ? 0 : this.getCountTotal()
						.hashCode());
		result = 37
				* result
				+ (getCountCharged() == null ? 0 : this.getCountCharged()
						.hashCode());
		result = 37
				* result
				+ (getCountPaddingPay() == null ? 0 : this.getCountPaddingPay()
						.hashCode());
		result = 37
				* result
				+ (getCountClassAmount() == null ? 0 : this
						.getCountClassAmount().hashCode());
		result = 37
				* result
				+ (getCountRefundAmount() == null ? 0 : this
						.getCountRefundAmount().hashCode());
		return result;
	}

}