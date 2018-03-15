package com.eduboss.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.SummaryClassType;
import com.eduboss.common.SummaryCycleType;

/**
 * SummaryCampus entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "SUMMARY_CAMPUS")
public class SummaryCampus implements java.io.Serializable {

	// Fields

	private String id;
	private SummaryCycleType summaryCycleType;
	private String summaryCycleStartDate;
	private String summaryCycleEndDate;
	private SummaryClassType summaryClassType;
	private String createTime;
	private Double consultorAdvance;
	private Double studyManagerAdvance;
	private Double refundAmount;
	private Double campusAdvance;
	private Double refundProportion;
	private Double advanceChain;
	private Double advanceYearonyear;
	private Double courseConsumeIncome;
	private Double courseConsumeChain;
	private Double courseConsumeYearonyear;
	private Double renewalProportion;
	private Double changeReferralRateNewsigning;
	private Double operationRate;
	private Double predictTarget;
	private Double advanceReachRate;
	private Double advanceQuarterReachRate;
	private Double advanceYearReachRate;
	private Double courseConsumeTarget;
	private Double courseConsumeReachRate;
	private Double courseConsumeQuarterReachrate;
	private Double courseConsumeYearReachrate;
	private Double consultorReachRate;
	private Double studyManagerReachRate;
	private Double planCourseHour;
	private Double realCourseHour;
	private Double courseHourReachRate;
	private Double fulltimeTeacherAverageClasshour;
	private Double BTeacherAverageClasshour;
	private Double fulltimeParttimeCoursehourRate;
	private Double oneononeGiveRate;
	private Double oneononeCoursehourAveragePrice;
	private Double surplusCourseHour;
	private Double oneononeStudentAverageCourseconsume;
	private Double departmentFulltimeAverageCoursehour;
	private Integer newSigningStudent;
	private Integer renewalStudent;
	private Integer refundStudent;
	private Integer suspendCourseStudent;
	private Integer finishCourseStudent;
	private Integer totalAtSchoolStudent;
	private Double suspendCourseStudentRate;
	private Double renewalStudentRate;
	private Integer monthReferralNum;
	private Integer under20hStudentNum;
	private Integer under10hStudentNum;
	private Integer renewalSubjectNum;
	private Integer newSigningSubjectNum;
	private Integer refundSubjectNum;
	private Integer targetSubjectNum;
	private Double subjectNumReachRate;
	private Integer peopleAverageSubjectNum;
	private Integer openClassNum;
	private Integer classAverageStudentNum;
	private Integer newStudentNum;
	private Integer oldStudentNum;
	private Integer chineseSubjectStudentNum;
	private Integer mathSubjectStudentNum;
	private Integer englishSubjectStudentNum;
	private Integer physicsStudentNum;
	private Integer chemistryStudentNum;
	private Integer politicsStudentNum;
	private Integer historyStudentNum;
	private Integer geographyStudentNum;
	private Integer biologyStudentNum;
	private Integer otherStudentNum;
	private Double primarySchoolGrade1to5Rate;
	private Double primarySchoolGrade6Rate;
	private Double middleSchoolGrade1Rate;
	private Double middleSchoolGrade2Rate;
	private Double middleSchoolGrade3Rate;
	private Double highSchoolGrade1Rate;
	private Double highSchoolGrade2Rate;
	private Double highSchoolGrade3Rate;
	private Set<SummaryCampusTotalExtend> summaryCampusTotalExtends = new HashSet<SummaryCampusTotalExtend>(0);

	// Constructors

	/** default constructor */
	public SummaryCampus() {
	}

	/** minimal constructor */
	public SummaryCampus(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
	}

	/** full constructor */
	public SummaryCampus(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			SummaryClassType summaryClassType,
			Double consultorAdvance, Double studyManagerAdvance,
			Double refundAmount, Double campusAdvance, Double refundProportion,
			Double advanceChain, Double advanceYearonyear,
			Double courseConsumeIncome, Double courseConsumeChain,
			Double courseConsumeYearonyear, Double renewalProportion,
			Double changeReferralRateNewsigning, Double operationRate,
			Double predictTarget, Double advanceReachRate,
			Double advanceQuarterReachRate, Double advanceYearReachRate,
			Double courseConsumeTarget, Double courseConsumeReachRate,
			Double courseConsumeQuarterReachrate,
			Double courseConsumeYearReachrate, Double consultorReachRate,
			Double studyManagerReachRate, Double planCourseHour,
			Double realCourseHour, Double courseHourReachRate,
			Double fulltimeTeacherAverageClasshour,
			Double BTeacherAverageClasshour,
			Double fulltimeParttimeCoursehourRate, Double oneononeGiveRate,
			Double oneononeCoursehourAveragePrice, Double surplusCourseHour,
			Double oneononeStudentAverageCourseconsume,
			Double departmentFulltimeAverageCoursehour,
			Integer newSigningStudent, Integer renewalStudent,
			Integer refundStudent, Integer suspendCourseStudent,
			Integer finishCourseStudent, Integer totalAtSchoolStudent,
			Double suspendCourseStudentRate, Double renewalStudentRate,
			Integer monthReferralNum, Integer under20hStudentNum,
			Integer under10hStudentNum, Integer renewalSubjectNum,
			Integer newSigningSubjectNum, Integer refundSubjectNum,
			Integer targetSubjectNum, Double subjectNumReachRate,
			Integer peopleAverageSubjectNum, Integer openClassNum,
			Integer classAverageStudentNum, Integer newStudentNum,
			Integer oldStudentNum, Integer chineseSubjectStudentNum,
			Integer mathSubjectStudentNum, Integer englishSubjectStudentNum,
			Integer physicsStudentNum, Integer chemistryStudentNum,
			Integer politicsStudentNum, Integer historyStudentNum,
			Integer geographyStudentNum, Integer biologyStudentNum,
			Integer otherStudentNum, Double primarySchoolGrade1to5Rate,
			Double primarySchoolGrade6Rate, Double middleSchoolGrade1Rate,
			Double middleSchoolGrade2Rate, Double middleSchoolGrade3Rate,
			Double highSchoolGrade1Rate, Double highSchoolGrade2Rate,
			Double highSchoolGrade3Rate, Set summaryCampusTotalExtends) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.summaryClassType = summaryClassType;
		this.consultorAdvance = consultorAdvance;
		this.studyManagerAdvance = studyManagerAdvance;
		this.refundAmount = refundAmount;
		this.campusAdvance = campusAdvance;
		this.refundProportion = refundProportion;
		this.advanceChain = advanceChain;
		this.advanceYearonyear = advanceYearonyear;
		this.courseConsumeIncome = courseConsumeIncome;
		this.courseConsumeChain = courseConsumeChain;
		this.courseConsumeYearonyear = courseConsumeYearonyear;
		this.renewalProportion = renewalProportion;
		this.changeReferralRateNewsigning = changeReferralRateNewsigning;
		this.operationRate = operationRate;
		this.predictTarget = predictTarget;
		this.advanceReachRate = advanceReachRate;
		this.advanceQuarterReachRate = advanceQuarterReachRate;
		this.advanceYearReachRate = advanceYearReachRate;
		this.courseConsumeTarget = courseConsumeTarget;
		this.courseConsumeReachRate = courseConsumeReachRate;
		this.courseConsumeQuarterReachrate = courseConsumeQuarterReachrate;
		this.courseConsumeYearReachrate = courseConsumeYearReachrate;
		this.consultorReachRate = consultorReachRate;
		this.studyManagerReachRate = studyManagerReachRate;
		this.planCourseHour = planCourseHour;
		this.realCourseHour = realCourseHour;
		this.courseHourReachRate = courseHourReachRate;
		this.fulltimeTeacherAverageClasshour = fulltimeTeacherAverageClasshour;
		this.BTeacherAverageClasshour = BTeacherAverageClasshour;
		this.fulltimeParttimeCoursehourRate = fulltimeParttimeCoursehourRate;
		this.oneononeGiveRate = oneononeGiveRate;
		this.oneononeCoursehourAveragePrice = oneononeCoursehourAveragePrice;
		this.surplusCourseHour = surplusCourseHour;
		this.oneononeStudentAverageCourseconsume = oneononeStudentAverageCourseconsume;
		this.departmentFulltimeAverageCoursehour = departmentFulltimeAverageCoursehour;
		this.newSigningStudent = newSigningStudent;
		this.renewalStudent = renewalStudent;
		this.refundStudent = refundStudent;
		this.suspendCourseStudent = suspendCourseStudent;
		this.finishCourseStudent = finishCourseStudent;
		this.totalAtSchoolStudent = totalAtSchoolStudent;
		this.suspendCourseStudentRate = suspendCourseStudentRate;
		this.renewalStudentRate = renewalStudentRate;
		this.monthReferralNum = monthReferralNum;
		this.under20hStudentNum = under20hStudentNum;
		this.under10hStudentNum = under10hStudentNum;
		this.renewalSubjectNum = renewalSubjectNum;
		this.newSigningSubjectNum = newSigningSubjectNum;
		this.refundSubjectNum = refundSubjectNum;
		this.targetSubjectNum = targetSubjectNum;
		this.subjectNumReachRate = subjectNumReachRate;
		this.peopleAverageSubjectNum = peopleAverageSubjectNum;
		this.openClassNum = openClassNum;
		this.classAverageStudentNum = classAverageStudentNum;
		this.newStudentNum = newStudentNum;
		this.oldStudentNum = oldStudentNum;
		this.chineseSubjectStudentNum = chineseSubjectStudentNum;
		this.mathSubjectStudentNum = mathSubjectStudentNum;
		this.englishSubjectStudentNum = englishSubjectStudentNum;
		this.physicsStudentNum = physicsStudentNum;
		this.chemistryStudentNum = chemistryStudentNum;
		this.politicsStudentNum = politicsStudentNum;
		this.historyStudentNum = historyStudentNum;
		this.geographyStudentNum = geographyStudentNum;
		this.biologyStudentNum = biologyStudentNum;
		this.otherStudentNum = otherStudentNum;
		this.primarySchoolGrade1to5Rate = primarySchoolGrade1to5Rate;
		this.primarySchoolGrade6Rate = primarySchoolGrade6Rate;
		this.middleSchoolGrade1Rate = middleSchoolGrade1Rate;
		this.middleSchoolGrade2Rate = middleSchoolGrade2Rate;
		this.middleSchoolGrade3Rate = middleSchoolGrade3Rate;
		this.highSchoolGrade1Rate = highSchoolGrade1Rate;
		this.highSchoolGrade2Rate = highSchoolGrade2Rate;
		this.highSchoolGrade3Rate = highSchoolGrade3Rate;
		this.summaryCampusTotalExtends = summaryCampusTotalExtends;
	}

	// Property accessors

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SUMMARY_CYCLE_TYPE", length = 32)
	public SummaryCycleType getSummaryCycleType() {
		return this.summaryCycleType;
	}

	public void setSummaryCycleType(SummaryCycleType summaryCycleType) {
		this.summaryCycleType = summaryCycleType;
	}

	@Column(name = "SUMMARY_CYCLE_START_DATE", length = 10)
	public String getSummaryCycleStartDate() {
		return this.summaryCycleStartDate;
	}

	public void setSummaryCycleStartDate(String summaryCycleStartDate) {
		this.summaryCycleStartDate = summaryCycleStartDate;
	}

	@Column(name = "SUMMARY_CYCLE_END_DATE", length = 10)
	public String getSummaryCycleEndDate() {
		return this.summaryCycleEndDate;
	}
	
	public void setSummaryCycleEndDate(String summaryCycleEndDate) {
		this.summaryCycleEndDate = summaryCycleEndDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SUMMARY_CLASS_TYPE", length = 32)
	public SummaryClassType getSummaryClassType() {
		return summaryClassType;
	}

	public void setSummaryClassType(SummaryClassType summaryClassType) {
		this.summaryClassType = summaryClassType;
	}

	@Column(name = "CONSULTOR_ADVANCE", precision = 16)
	public Double getConsultorAdvance() {
		return this.consultorAdvance;
	}

	public void setConsultorAdvance(Double consultorAdvance) {
		this.consultorAdvance = consultorAdvance;
	}

	@Column(name = "STUDY_MANAGER_ADVANCE", precision = 16)
	public Double getStudyManagerAdvance() {
		return this.studyManagerAdvance;
	}

	public void setStudyManagerAdvance(Double studyManagerAdvance) {
		this.studyManagerAdvance = studyManagerAdvance;
	}

	@Column(name = "REFUND_AMOUNT", precision = 16)
	public Double getRefundAmount() {
		return this.refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	@Column(name = "CAMPUS_ADVANCE", precision = 16)
	public Double getCampusAdvance() {
		return this.campusAdvance;
	}

	public void setCampusAdvance(Double campusAdvance) {
		this.campusAdvance = campusAdvance;
	}

	@Column(name = "REFUND_PROPORTION", precision = 10)
	public Double getRefundProportion() {
		return this.refundProportion;
	}

	public void setRefundProportion(Double refundProportion) {
		this.refundProportion = refundProportion;
	}

	@Column(name = "ADVANCE_CHAIN", precision = 10)
	public Double getAdvanceChain() {
		return this.advanceChain;
	}

	public void setAdvanceChain(Double advanceChain) {
		this.advanceChain = advanceChain;
	}

	@Column(name = "ADVANCE_YEARONYEAR", precision = 10)
	public Double getAdvanceYearonyear() {
		return this.advanceYearonyear;
	}

	public void setAdvanceYearonyear(Double advanceYearonyear) {
		this.advanceYearonyear = advanceYearonyear;
	}

	@Column(name = "COURSE_CONSUME_INCOME", precision = 16)
	public Double getCourseConsumeIncome() {
		return this.courseConsumeIncome;
	}

	public void setCourseConsumeIncome(Double courseConsumeIncome) {
		this.courseConsumeIncome = courseConsumeIncome;
	}

	@Column(name = "COURSE_CONSUME_CHAIN", precision = 10)
	public Double getCourseConsumeChain() {
		return this.courseConsumeChain;
	}

	public void setCourseConsumeChain(Double courseConsumeChain) {
		this.courseConsumeChain = courseConsumeChain;
	}

	@Column(name = "COURSE_CONSUME_YEARONYEAR", precision = 10)
	public Double getCourseConsumeYearonyear() {
		return this.courseConsumeYearonyear;
	}

	public void setCourseConsumeYearonyear(Double courseConsumeYearonyear) {
		this.courseConsumeYearonyear = courseConsumeYearonyear;
	}

	@Column(name = "RENEWAL_PROPORTION", precision = 10)
	public Double getRenewalProportion() {
		return this.renewalProportion;
	}

	public void setRenewalProportion(Double renewalProportion) {
		this.renewalProportion = renewalProportion;
	}

	@Column(name = "CHANGE_REFERRAL_RATE_NEWSIGNING", precision = 10)
	public Double getChangeReferralRateNewsigning() {
		return this.changeReferralRateNewsigning;
	}

	public void setChangeReferralRateNewsigning(
			Double changeReferralRateNewsigning) {
		this.changeReferralRateNewsigning = changeReferralRateNewsigning;
	}

	@Column(name = "OPERATION_RATE", precision = 10)
	public Double getOperationRate() {
		return this.operationRate;
	}

	public void setOperationRate(Double operationRate) {
		this.operationRate = operationRate;
	}

	@Column(name = "PREDICT_TARGET", precision = 16)
	public Double getPredictTarget() {
		return this.predictTarget;
	}

	public void setPredictTarget(Double predictTarget) {
		this.predictTarget = predictTarget;
	}

	@Column(name = "ADVANCE_REACH_RATE", precision = 10)
	public Double getAdvanceReachRate() {
		return this.advanceReachRate;
	}

	public void setAdvanceReachRate(Double advanceReachRate) {
		this.advanceReachRate = advanceReachRate;
	}

	@Column(name = "ADVANCE_QUARTER_REACH_RATE", precision = 10)
	public Double getAdvanceQuarterReachRate() {
		return this.advanceQuarterReachRate;
	}

	public void setAdvanceQuarterReachRate(Double advanceQuarterReachRate) {
		this.advanceQuarterReachRate = advanceQuarterReachRate;
	}

	@Column(name = "ADVANCE_YEAR_REACH_RATE", precision = 10)
	public Double getAdvanceYearReachRate() {
		return this.advanceYearReachRate;
	}

	public void setAdvanceYearReachRate(Double advanceYearReachRate) {
		this.advanceYearReachRate = advanceYearReachRate;
	}

	@Column(name = "COURSE_CONSUME_TARGET", precision = 16)
	public Double getCourseConsumeTarget() {
		return this.courseConsumeTarget;
	}

	public void setCourseConsumeTarget(Double courseConsumeTarget) {
		this.courseConsumeTarget = courseConsumeTarget;
	}

	@Column(name = "COURSE_CONSUME_REACH_RATE", precision = 10)
	public Double getCourseConsumeReachRate() {
		return this.courseConsumeReachRate;
	}

	public void setCourseConsumeReachRate(Double courseConsumeReachRate) {
		this.courseConsumeReachRate = courseConsumeReachRate;
	}

	@Column(name = "COURSE_CONSUME_QUARTER_REACHRATE", precision = 10)
	public Double getCourseConsumeQuarterReachrate() {
		return this.courseConsumeQuarterReachrate;
	}

	public void setCourseConsumeQuarterReachrate(
			Double courseConsumeQuarterReachrate) {
		this.courseConsumeQuarterReachrate = courseConsumeQuarterReachrate;
	}

	@Column(name = "COURSE_CONSUME_YEAR_REACHRATE", precision = 10)
	public Double getCourseConsumeYearReachrate() {
		return this.courseConsumeYearReachrate;
	}

	public void setCourseConsumeYearReachrate(Double courseConsumeYearReachrate) {
		this.courseConsumeYearReachrate = courseConsumeYearReachrate;
	}

	@Column(name = "CONSULTOR_REACH_RATE", precision = 10)
	public Double getConsultorReachRate() {
		return this.consultorReachRate;
	}

	public void setConsultorReachRate(Double consultorReachRate) {
		this.consultorReachRate = consultorReachRate;
	}

	@Column(name = "STUDY_MANAGER_REACH_RATE", precision = 10)
	public Double getStudyManagerReachRate() {
		return this.studyManagerReachRate;
	}

	public void setStudyManagerReachRate(Double studyManagerReachRate) {
		this.studyManagerReachRate = studyManagerReachRate;
	}

	@Column(name = "PLAN_COURSE_HOUR", precision = 16)
	public Double getPlanCourseHour() {
		return this.planCourseHour;
	}

	public void setPlanCourseHour(Double planCourseHour) {
		this.planCourseHour = planCourseHour;
	}

	@Column(name = "REAL_COURSE_HOUR", precision = 16)
	public Double getRealCourseHour() {
		return this.realCourseHour;
	}

	public void setRealCourseHour(Double realCourseHour) {
		this.realCourseHour = realCourseHour;
	}

	@Column(name = "COURSE_HOUR_REACH_RATE", precision = 10)
	public Double getCourseHourReachRate() {
		return this.courseHourReachRate;
	}

	public void setCourseHourReachRate(Double courseHourReachRate) {
		this.courseHourReachRate = courseHourReachRate;
	}

	@Column(name = "FULLTIME_TEACHER_AVERAGE_CLASSHOUR", precision = 16)
	public Double getFulltimeTeacherAverageClasshour() {
		return this.fulltimeTeacherAverageClasshour;
	}

	public void setFulltimeTeacherAverageClasshour(
			Double fulltimeTeacherAverageClasshour) {
		this.fulltimeTeacherAverageClasshour = fulltimeTeacherAverageClasshour;
	}

	@Column(name = "B_TEACHER_AVERAGE_CLASSHOUR", precision = 16)
	public Double getBTeacherAverageClasshour() {
		return this.BTeacherAverageClasshour;
	}

	public void setBTeacherAverageClasshour(Double BTeacherAverageClasshour) {
		this.BTeacherAverageClasshour = BTeacherAverageClasshour;
	}

	@Column(name = "FULLTIME_PARTTIME_COURSEHOUR_RATE", precision = 10)
	public Double getFulltimeParttimeCoursehourRate() {
		return this.fulltimeParttimeCoursehourRate;
	}

	public void setFulltimeParttimeCoursehourRate(
			Double fulltimeParttimeCoursehourRate) {
		this.fulltimeParttimeCoursehourRate = fulltimeParttimeCoursehourRate;
	}

	@Column(name = "ONEONONE_GIVE_RATE", precision = 10)
	public Double getOneononeGiveRate() {
		return this.oneononeGiveRate;
	}

	public void setOneononeGiveRate(Double oneononeGiveRate) {
		this.oneononeGiveRate = oneononeGiveRate;
	}

	@Column(name = "ONEONONE_COURSEHOUR_AVERAGE_PRICE", precision = 16)
	public Double getOneononeCoursehourAveragePrice() {
		return this.oneononeCoursehourAveragePrice;
	}

	public void setOneononeCoursehourAveragePrice(
			Double oneononeCoursehourAveragePrice) {
		this.oneononeCoursehourAveragePrice = oneononeCoursehourAveragePrice;
	}

	@Column(name = "SURPLUS_COURSE_HOUR", precision = 16)
	public Double getSurplusCourseHour() {
		return this.surplusCourseHour;
	}

	public void setSurplusCourseHour(Double surplusCourseHour) {
		this.surplusCourseHour = surplusCourseHour;
	}

	@Column(name = "ONEONONE_STUDENT_AVERAGE_COURSECONSUME", precision = 16)
	public Double getOneononeStudentAverageCourseconsume() {
		return this.oneononeStudentAverageCourseconsume;
	}

	public void setOneononeStudentAverageCourseconsume(
			Double oneononeStudentAverageCourseconsume) {
		this.oneononeStudentAverageCourseconsume = oneononeStudentAverageCourseconsume;
	}

	@Column(name = "DEPARTMENT_FULLTIME_AVERAGE_COURSEHOUR", precision = 16)
	public Double getDepartmentFulltimeAverageCoursehour() {
		return this.departmentFulltimeAverageCoursehour;
	}

	public void setDepartmentFulltimeAverageCoursehour(
			Double departmentFulltimeAverageCoursehour) {
		this.departmentFulltimeAverageCoursehour = departmentFulltimeAverageCoursehour;
	}

	@Column(name="NEW_SIGNING_STUDENT")
	public Integer getNewSigningStudent() {
		return this.newSigningStudent;
	}

	public void setNewSigningStudent(Integer newSigningStudent) {
		this.newSigningStudent = newSigningStudent;
	}

	@Column(name="RENEWAL_STUDENT")
	public Integer getRenewalStudent() {
		return this.renewalStudent;
	}

	public void setRenewalStudent(Integer renewalStudent) {
		this.renewalStudent = renewalStudent;
	}

	@Column(name="REFUND_STUDENT")
	public Integer getRefundStudent() {
		return this.refundStudent;
	}

	public void setRefundStudent(Integer refundStudent) {
		this.refundStudent = refundStudent;
	}

	@Column(name="SUSPEND_COURSE_STUDENT")
	public Integer getSuspendCourseStudent() {
		return this.suspendCourseStudent;
	}

	public void setSuspendCourseStudent(Integer suspendCourseStudent) {
		this.suspendCourseStudent = suspendCourseStudent;
	}

	@Column(name="FINISH_COURSE_STUDENT")
	public Integer getFinishCourseStudent() {
		return this.finishCourseStudent;
	}

	public void setFinishCourseStudent(Integer finishCourseStudent) {
		this.finishCourseStudent = finishCourseStudent;
	}

	@Column(name="TOTAL_AT_SCHOOL_STUDENT")
	public Integer getTotalAtSchoolStudent() {
		return this.totalAtSchoolStudent;
	}

	public void setTotalAtSchoolStudent(Integer totalAtSchoolStudent) {
		this.totalAtSchoolStudent = totalAtSchoolStudent;
	}

	@Column(name = "SUSPEND_COURSE_STUDENT_RATE", precision = 10)
	public Double getSuspendCourseStudentRate() {
		return this.suspendCourseStudentRate;
	}

	public void setSuspendCourseStudentRate(Double suspendCourseStudentRate) {
		this.suspendCourseStudentRate = suspendCourseStudentRate;
	}

	@Column(name = "RENEWAL_STUDENT_RATE", precision = 10)
	public Double getRenewalStudentRate() {
		return this.renewalStudentRate;
	}

	public void setRenewalStudentRate(Double renewalStudentRate) {
		this.renewalStudentRate = renewalStudentRate;
	}

	@Column(name = "MONTH_REFERRAL_NUM")
	public Integer getMonthReferralNum() {
		return this.monthReferralNum;
	}

	public void setMonthReferralNum(Integer monthReferralNum) {
		this.monthReferralNum = monthReferralNum;
	}

	@Column(name="UNDER_20H_STUDENT_NUM")
	public Integer getUnder20hStudentNum() {
		return this.under20hStudentNum;
	}

	public void setUnder20hStudentNum(Integer under20hStudentNum) {
		this.under20hStudentNum = under20hStudentNum;
	}

	@Column(name="UNDER_10H_STUDENT_NUM")
	public Integer getUnder10hStudentNum() {
		return this.under10hStudentNum;
	}

	public void setUnder10hStudentNum(Integer under10hStudentNum) {
		this.under10hStudentNum = under10hStudentNum;
	}

	@Column(name="RENEWAL_SUBJECT_NUM")
	public Integer getRenewalSubjectNum() {
		return this.renewalSubjectNum;
	}

	public void setRenewalSubjectNum(Integer renewalSubjectNum) {
		this.renewalSubjectNum = renewalSubjectNum;
	}

	@Column(name="NEW_SIGNING_SUBJECT_NUM")
	public Integer getNewSigningSubjectNum() {
		return this.newSigningSubjectNum;
	}

	public void setNewSigningSubjectNum(Integer newSigningSubjectNum) {
		this.newSigningSubjectNum = newSigningSubjectNum;
	}

	@Column(name="REFUND_SUBJECT_NUM")
	public Integer getRefundSubjectNum() {
		return this.refundSubjectNum;
	}

	public void setRefundSubjectNum(Integer refundSubjectNum) {
		this.refundSubjectNum = refundSubjectNum;
	}

	@Column(name="TARGET_SUBJECT_NUM")
	public Integer getTargetSubjectNum() {
		return this.targetSubjectNum;
	}

	public void setTargetSubjectNum(Integer targetSubjectNum) {
		this.targetSubjectNum = targetSubjectNum;
	}

	@Column(name="SUBJECT_NUM_REACH_RATE", precision = 10)
	public Double getSubjectNumReachRate() {
		return this.subjectNumReachRate;
	}

	public void setSubjectNumReachRate(Double subjectNumReachRate) {
		this.subjectNumReachRate = subjectNumReachRate;
	}

	@Column(name="PEOPLE_AVERAGE_SUBJECT_NUM")
	public Integer getPeopleAverageSubjectNum() {
		return this.peopleAverageSubjectNum;
	}

	public void setPeopleAverageSubjectNum(Integer peopleAverageSubjectNum) {
		this.peopleAverageSubjectNum = peopleAverageSubjectNum;
	}

	@Column(name="OPEN_CLASS_NUM")
	public Integer getOpenClassNum() {
		return this.openClassNum;
	}

	public void setOpenClassNum(Integer openClassNum) {
		this.openClassNum = openClassNum;
	}

	@Column(name="CLASS_AVERAGE_STUDENT_NUM")
	public Integer getClassAverageStudentNum() {
		return this.classAverageStudentNum;
	}

	public void setClassAverageStudentNum(Integer classAverageStudentNum) {
		this.classAverageStudentNum = classAverageStudentNum;
	}

	@Column(name="NEW_STUDENT_NUM")
	public Integer getNewStudentNum() {
		return this.newStudentNum;
	}

	public void setNewStudentNum(Integer newStudentNum) {
		this.newStudentNum = newStudentNum;
	}

	@Column(name="OLD_STUDENT_NUM")
	public Integer getOldStudentNum() {
		return this.oldStudentNum;
	}

	public void setOldStudentNum(Integer oldStudentNum) {
		this.oldStudentNum = oldStudentNum;
	}

	@Column(name="CHINESE_SUBJECT_STUDENT_NUM")
	public Integer getChineseSubjectStudentNum() {
		return this.chineseSubjectStudentNum;
	}

	public void setChineseSubjectStudentNum(Integer chineseSubjectStudentNum) {
		this.chineseSubjectStudentNum = chineseSubjectStudentNum;
	}

	@Column(name="MATH_SUBJECT_STUDENT_NUM")
	public Integer getMathSubjectStudentNum() {
		return this.mathSubjectStudentNum;
	}

	public void setMathSubjectStudentNum(Integer mathSubjectStudentNum) {
		this.mathSubjectStudentNum = mathSubjectStudentNum;
	}

	@Column(name="ENGLISH_SUBJECT_STUDENT_NUM")
	public Integer getEnglishSubjectStudentNum() {
		return this.englishSubjectStudentNum;
	}

	public void setEnglishSubjectStudentNum(Integer englishSubjectStudentNum) {
		this.englishSubjectStudentNum = englishSubjectStudentNum;
	}

	@Column(name="PHYSICS_STUDENT_NUM")
	public Integer getPhysicsStudentNum() {
		return this.physicsStudentNum;
	}

	public void setPhysicsStudentNum(Integer physicsStudentNum) {
		this.physicsStudentNum = physicsStudentNum;
	}

	@Column(name="CHEMISTRY_STUDENT_NUM")
	public Integer getChemistryStudentNum() {
		return this.chemistryStudentNum;
	}

	public void setChemistryStudentNum(Integer chemistryStudentNum) {
		this.chemistryStudentNum = chemistryStudentNum;
	}

	@Column(name="POLITICS_STUDENT_NUM")
	public Integer getPoliticsStudentNum() {
		return this.politicsStudentNum;
	}

	public void setPoliticsStudentNum(Integer politicsStudentNum) {
		this.politicsStudentNum = politicsStudentNum;
	}

	@Column(name="HISTORY_STUDENT_NUM")
	public Integer getHistoryStudentNum() {
		return this.historyStudentNum;
	}

	public void setHistoryStudentNum(Integer historyStudentNum) {
		this.historyStudentNum = historyStudentNum;
	}

	@Column(name="GEOGRAPHY_STUDENT_NUM")
	public Integer getGeographyStudentNum() {
		return this.geographyStudentNum;
	}

	public void setGeographyStudentNum(Integer geographyStudentNum) {
		this.geographyStudentNum = geographyStudentNum;
	}

	@Column(name="BIOLOGY_STUDENT_NUM")
	public Integer getBiologyStudentNum() {
		return this.biologyStudentNum;
	}

	public void setBiologyStudentNum(Integer biologyStudentNum) {
		this.biologyStudentNum = biologyStudentNum;
	}

	@Column(name="OTHER_STUDENT_NUM")
	public Integer getOtherStudentNum() {
		return this.otherStudentNum;
	}

	public void setOtherStudentNum(Integer otherStudentNum) {
		this.otherStudentNum = otherStudentNum;
	}

	@Column(name = "PRIMARY_SCHOOL_GRADE1TO5_RATE", precision = 10)
	public Double getPrimarySchoolGrade1to5Rate() {
		return this.primarySchoolGrade1to5Rate;
	}

	public void setPrimarySchoolGrade1to5Rate(Double primarySchoolGrade1to5Rate) {
		this.primarySchoolGrade1to5Rate = primarySchoolGrade1to5Rate;
	}

	@Column(name = "PRIMARY_SCHOOL_GRADE6_RATE", precision = 10)
	public Double getPrimarySchoolGrade6Rate() {
		return this.primarySchoolGrade6Rate;
	}

	public void setPrimarySchoolGrade6Rate(Double primarySchoolGrade6Rate) {
		this.primarySchoolGrade6Rate = primarySchoolGrade6Rate;
	}

	@Column(name = "MIDDLE_SCHOOL_GRADE1_RATE", precision = 10)
	public Double getMiddleSchoolGrade1Rate() {
		return this.middleSchoolGrade1Rate;
	}

	public void setMiddleSchoolGrade1Rate(Double middleSchoolGrade1Rate) {
		this.middleSchoolGrade1Rate = middleSchoolGrade1Rate;
	}

	@Column(name = "MIDDLE_SCHOOL_GRADE2_RATE", precision = 10)
	public Double getMiddleSchoolGrade2Rate() {
		return this.middleSchoolGrade2Rate;
	}

	public void setMiddleSchoolGrade2Rate(Double middleSchoolGrade2Rate) {
		this.middleSchoolGrade2Rate = middleSchoolGrade2Rate;
	}

	@Column(name = "MIDDLE_SCHOOL_GRADE3_RATE", precision = 10)
	public Double getMiddleSchoolGrade3Rate() {
		return this.middleSchoolGrade3Rate;
	}

	public void setMiddleSchoolGrade3Rate(Double middleSchoolGrade3Rate) {
		this.middleSchoolGrade3Rate = middleSchoolGrade3Rate;
	}

	@Column(name = "HIGH_SCHOOL_GRADE1_RATE", precision = 10)
	public Double getHighSchoolGrade1Rate() {
		return this.highSchoolGrade1Rate;
	}

	public void setHighSchoolGrade1Rate(Double highSchoolGrade1Rate) {
		this.highSchoolGrade1Rate = highSchoolGrade1Rate;
	}

	@Column(name = "HIGH_SCHOOL_GRADE2_RATE", precision = 10)
	public Double getHighSchoolGrade2Rate() {
		return this.highSchoolGrade2Rate;
	}

	public void setHighSchoolGrade2Rate(Double highSchoolGrade2Rate) {
		this.highSchoolGrade2Rate = highSchoolGrade2Rate;
	}

	@Column(name = "HIGH_SCHOOL_GRADE3_RATE", precision = 10)
	public Double getHighSchoolGrade3Rate() {
		return this.highSchoolGrade3Rate;
	}

	public void setHighSchoolGrade3Rate(Double highSchoolGrade3Rate) {
		this.highSchoolGrade3Rate = highSchoolGrade3Rate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "summaryCampus")
	public Set<SummaryCampusTotalExtend> getSummaryCampusTotalExtends() {
		return this.summaryCampusTotalExtends;
	}

	public void setSummaryCampusTotalExtends(Set<SummaryCampusTotalExtend> summaryCampusTotalExtends) {
		this.summaryCampusTotalExtends = summaryCampusTotalExtends;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	

}