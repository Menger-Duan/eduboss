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

import com.eduboss.common.DateVeidooType;
import com.eduboss.common.MonthType;
import com.eduboss.common.QuarterType;

/**
 * PlanManagement entity. @author ndd
 */
@Entity
@Table(name = "PLAN_MANAGEMENT")
public class PlanManagement implements java.io.Serializable {

	// Fields

	private String id;
	private String goalType;
	private String goalId;
	private DateVeidooType timeType;
	private DataDict year;
	private QuarterType quarterId;
	private MonthType monthId;
	private DataDict targetType;
	private BigDecimal targetValue;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	private String modifyUserId;
	private String remark;
	private Integer planOrder;

	// Constructors

	/** default constructor */
	public PlanManagement() {
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "GOAL_TYPE", length = 32)
	public String getGoalType() {
		return this.goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}

	@Column(name = "GOAL_ID", length = 32)
	public String getGoalId() {
		return this.goalId;
	}

	public void setGoalId(String goalId) {
		this.goalId = goalId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TIME_TYPE", length = 32)
	public DateVeidooType getTimeType() {
		return this.timeType;
	}

	public void setTimeType(DateVeidooType timeType) {
		this.timeType = timeType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "YEAR_ID")
	public DataDict getYear() {
		return year;
	}

	public void setYear(DataDict year) {
		this.year = year;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "QUARTER_ID", length = 32)
	public QuarterType getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(QuarterType quarterId) {
		this.quarterId = quarterId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MONTH_ID", length = 32)
	public MonthType getMonthId() {
		return this.monthId;
	}

	public void setMonthId(MonthType monthId) {
		this.monthId = monthId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TARGET_TYPE")
	public DataDict getTargetType() {
		return targetType;
	}

	public void setTargetType(DataDict targetType) {
		this.targetType = targetType;
	}

	@Column(name = "TARGET_VALUE", precision = 12)
	public BigDecimal getTargetValue() {
		return this.targetValue;
	}

	public void setTargetValue(BigDecimal targetValue) {
		this.targetValue = targetValue;
	}

	@Column(name = "CREATE_USER_ID", length = 20)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 20)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "PLAN_ORDER")
	public Integer getPlanOrder() {
		return this.planOrder;
	}

	public void setPlanOrder(Integer planOrder) {
		this.planOrder = planOrder;
	}

}