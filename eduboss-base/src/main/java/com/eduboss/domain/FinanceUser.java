package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FINANCE_USER")
public class FinanceUser extends Finance  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String countDate;
	private String campusId;
	private String campusName;
	private String userId;
	private String userName;
	private BigDecimal countPaidTotalAmount;
	private BigDecimal countPaidCashAmountNew;
	private BigDecimal countPaidCashAmountPre;
	private int sort;
	private BigDecimal targetValue;
	private BigDecimal returnFee;
	private String createTime;
	private String modifyTime;
	private BigDecimal onlineAmount;
	private BigDecimal lineAmount;

	public FinanceUser() {
		countPaidTotalAmount=BigDecimal.ZERO;
		countPaidCashAmountNew=BigDecimal.ZERO;
		countPaidCashAmountPre=BigDecimal.ZERO;
		onlineAmount=BigDecimal.ZERO;
		lineAmount=BigDecimal.ZERO;
		targetValue=BigDecimal.ZERO;
		returnFee=BigDecimal.ZERO;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "COUNT_DATE", length = 10)
	public String getCountDate() {
		return countDate;
	}


	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}


	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "count_paid_total_amount")
	public BigDecimal getCountPaidTotalAmount() {
		return countPaidTotalAmount;
	}


	public void setCountPaidTotalAmount(BigDecimal countPaidTotalAmount) {
		this.countPaidTotalAmount = countPaidTotalAmount;
	}

	@Column(name = "count_paid_cash_amount_new")
	public BigDecimal getCountPaidCashAmountNew() {
		return countPaidCashAmountNew;
	}


	public void setCountPaidCashAmountNew(BigDecimal countPaidCashAmountNew) {
		this.countPaidCashAmountNew = countPaidCashAmountNew;
	}

	@Column(name = "count_paid_cash_amount_re")
	public BigDecimal getCountPaidCashAmountPre() {
		return countPaidCashAmountPre;
	}


	public void setCountPaidCashAmountPre(BigDecimal countPaidCashAmountPre) {
		this.countPaidCashAmountPre = countPaidCashAmountPre;
	}

	@Column(name = "sort")
	public int getSort() {
		return sort;
	}


	public void setSort(int sort) {
		this.sort = sort;
	}

	@Column(name = "target_Value")
	public BigDecimal getTargetValue() {
		return targetValue;
	}


	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	@Column(name = "return_fee")
	public BigDecimal getReturnFee() {
		return returnFee;
	}


	public void setReturnFee(BigDecimal returnFee) {
		this.returnFee = returnFee;
	}

	@Column(name = "campus_id", length = 32)
	public String getCampusId() {
		return campusId;
	}


	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	@Column(name = "campus_name", length = 100)
	public String getCampusName() {
		return campusName;
	}


	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	@Column(name = "user_id", length = 32)
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", length = 100)
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "online_amount")
	public BigDecimal getOnlineAmount() {
		return onlineAmount;
	}

	public void setOnlineAmount(BigDecimal onlineAmount) {
		this.onlineAmount = onlineAmount;
	}
	@Column(name = "line_amount")
	public BigDecimal getLineAmount() {
		return lineAmount;
	}

	public void setLineAmount(BigDecimal lineAmount) {
		this.lineAmount = lineAmount;
	}
}
