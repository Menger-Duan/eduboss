package com.eduboss.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * OdsDayFinance entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ODS_DAY_FINANCE")
public class OdsDayFinance implements java.io.Serializable {

	// Fields

	private OdsDayFinanceId id;

	// Constructors

	/** default constructor */
	public OdsDayFinance() {
	}

	/** full constructor */
	public OdsDayFinance(OdsDayFinanceId id) {
		this.id = id;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "groupId", column = @Column(name = "GROUP_ID", length = 32)),
			@AttributeOverride(name = "branchId", column = @Column(name = "BRANCH_ID", length = 32)),
			@AttributeOverride(name = "campusId", column = @Column(name = "CAMPUS_ID", length = 32)),
			@AttributeOverride(name = "userId", column = @Column(name = "USER_ID", length = 32)),
			@AttributeOverride(name = "countDate", column = @Column(name = "COUNT_DATE", length = 10)),
			@AttributeOverride(name = "countOnoOnOne", column = @Column(name = "COUNT_ONO_ON_ONE", precision = 10)),
			@AttributeOverride(name = "countMiniClass", column = @Column(name = "COUNT_MINI_CLASS", precision = 10)),
			@AttributeOverride(name = "countPromise", column = @Column(name = "COUNT_PROMISE", precision = 10)),
			@AttributeOverride(name = "countOthers", column = @Column(name = "COUNT_OTHERS", precision = 10)),
			@AttributeOverride(name = "countTotal", column = @Column(name = "COUNT_TOTAL", precision = 10)),
			@AttributeOverride(name = "countCharged", column = @Column(name = "COUNT_CHARGED", precision = 10)),
			@AttributeOverride(name = "countPaddingPay", column = @Column(name = "COUNT_PADDING_PAY", precision = 10)),
			@AttributeOverride(name = "countClassAmount", column = @Column(name = "COUNT_CLASS_AMOUNT", precision = 10)),
			@AttributeOverride(name = "countRefundAmount", column = @Column(name = "COUNT_REFUND_AMOUNT", precision = 10)) })
	public OdsDayFinanceId getId() {
		return this.id;
	}

	public void setId(OdsDayFinanceId id) {
		this.id = id;
	}

}