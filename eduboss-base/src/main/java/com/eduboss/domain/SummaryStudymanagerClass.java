package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.SummaryClassType;
import com.eduboss.common.SummaryCycleType;

/**
 * SummaryStudymanagerClass entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "SUMMARY_STUDYMANAGER_CLASS")
public class SummaryStudymanagerClass implements java.io.Serializable {

	// Fields

	private String id;
	private SummaryCycleType summaryCycleType;
	private String summaryCycleStartDate;
	private String summaryCycleEndDate;
	private String createTime;
	private SummaryClassType summaryClassType;
	private Integer newSignStudent;
	private Integer renewalStudent;
	private Integer refundStudent;
	private Integer suspendCourseStudent;
	private Integer finishCourseStudent;
	private Integer totalAtSchoolStudent;
	private Double suspendCourseStudentRate;
	private Double renewalStudentRate;
	private Integer monthChangeReferralNum;
	private Integer under20hStudentNum;
	private Integer under10hStudentNum;
	private Integer renewalSubjectNum;
	private Integer newsignRenewalSubjectNum;
	private Integer refundSubjectNum;
	private Integer targetSubjectNum;
	private Double subjectNumReachRate;
	private Double peopleAverageSubjectNum;
	private Integer totalOpenClassNum;
	private Double classAveragePeopleNum;
	private Integer newStudentNum;
	private Integer oldStudentNum;
	private Double planCoursehour;
	private Double campusRealTotalCoursehour;
	private Double coursehourReachRate;
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

	// Constructors

	/** default constructor */
	public SummaryStudymanagerClass() {
	}

	/** minimal constructor */
	public SummaryStudymanagerClass(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			SummaryClassType summaryClassType) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.summaryClassType = summaryClassType;
	}

	/** full constructor */
	public SummaryStudymanagerClass(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			SummaryClassType summaryClassType, Integer newSignStudent, Integer renewalStudent,
			Integer refundStudent, Integer suspendCourseStudent,
			Integer finishCourseStudent, Integer totalAtSchoolStudent,
			Double suspendCourseStudentRate, Double renewalStudentRate,
			Integer monthChangeReferralNum, Integer under20hStudentNum,
			Integer under10hStudentNum, Integer renewalSubjectNum,
			Integer newsignRenewalSubjectNum, Integer refundSubjectNum,
			Integer targetSubjectNum, Double subjectNumReachRate,
			Double peopleAverageSubjectNum, Integer totalOpenClassNum,
			Double classAveragePeopleNum, Integer newStudentNum,
			Integer oldStudentNum, Double planCoursehour,
			Double campusRealTotalCoursehour, Double coursehourReachRate,
			Integer chineseSubjectStudentNum, Integer mathSubjectStudentNum,
			Integer englishSubjectStudentNum, Integer physicsStudentNum,
			Integer chemistryStudentNum, Integer politicsStudentNum,
			Integer historyStudentNum, Integer geographyStudentNum,
			Integer biologyStudentNum, Integer otherStudentNum,
			Double primarySchoolGrade1to5Rate, Double primarySchoolGradeRate6,
			Double middleSchoolGrade1Rate, Double middleSchoolGrade2Rate,
			Double middleSchoolGrade3Rate, Double highSchoolGrade1Rate,
			Double highSchoolGrade2Rate, Double highSchoolGrade3Rate) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.summaryClassType = summaryClassType;
		this.newSignStudent = newSignStudent;
		this.renewalStudent = renewalStudent;
		this.refundStudent = refundStudent;
		this.suspendCourseStudent = suspendCourseStudent;
		this.finishCourseStudent = finishCourseStudent;
		this.totalAtSchoolStudent = totalAtSchoolStudent;
		this.suspendCourseStudentRate = suspendCourseStudentRate;
		this.renewalStudentRate = renewalStudentRate;
		this.monthChangeReferralNum = monthChangeReferralNum;
		this.under20hStudentNum = under20hStudentNum;
		this.under10hStudentNum = under10hStudentNum;
		this.renewalSubjectNum = renewalSubjectNum;
		this.newsignRenewalSubjectNum = newsignRenewalSubjectNum;
		this.refundSubjectNum = refundSubjectNum;
		this.targetSubjectNum = targetSubjectNum;
		this.subjectNumReachRate = subjectNumReachRate;
		this.peopleAverageSubjectNum = peopleAverageSubjectNum;
		this.totalOpenClassNum = totalOpenClassNum;
		this.classAveragePeopleNum = classAveragePeopleNum;
		this.newStudentNum = newStudentNum;
		this.oldStudentNum = oldStudentNum;
		this.planCoursehour = planCoursehour;
		this.campusRealTotalCoursehour = campusRealTotalCoursehour;
		this.coursehourReachRate = coursehourReachRate;
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
		this.primarySchoolGrade6Rate = primarySchoolGradeRate6;
		this.middleSchoolGrade1Rate = middleSchoolGrade1Rate;
		this.middleSchoolGrade2Rate = middleSchoolGrade2Rate;
		this.middleSchoolGrade3Rate = middleSchoolGrade3Rate;
		this.highSchoolGrade1Rate = highSchoolGrade1Rate;
		this.highSchoolGrade2Rate = highSchoolGrade2Rate;
		this.highSchoolGrade3Rate = highSchoolGrade3Rate;
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
		return this.summaryClassType;
	}

	public void setSummaryClassType(SummaryClassType summaryClassType) {
		this.summaryClassType = summaryClassType;
	}

	@Column(name="NEW_SIGN_STUDENT")
	public Integer getNewSignStudent() {
		return this.newSignStudent;
	}

	public void setNewSignStudent(Integer newSignStudent) {
		this.newSignStudent = newSignStudent;
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

	@Column(name="SUSPEND_COURSE_STUDENT_RATE", precision = 10)
	public Double getSuspendCourseStudentRate() {
		return this.suspendCourseStudentRate;
	}

	public void setSuspendCourseStudentRate(Double suspendCourseStudentRate) {
		this.suspendCourseStudentRate = suspendCourseStudentRate;
	}

	@Column(name="RENEWAL_STUDENT_RATE", precision = 10)
	public Double getRenewalStudentRate() {
		return this.renewalStudentRate;
	}

	public void setRenewalStudentRate(Double renewalStudentRate) {
		this.renewalStudentRate = renewalStudentRate;
	}

	@Column(name="MONTH_CHANGE_REFERRAL_NUM")
	public Integer getMonthChangeReferralNum() {
		return this.monthChangeReferralNum;
	}

	public void setMonthChangeReferralNum(Integer monthChangeReferralNum) {
		this.monthChangeReferralNum = monthChangeReferralNum;
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

	@Column(name="NEWSIGN_RENEWAL_SUBJECT_NUM")
	public Integer getNewsignRenewalSubjectNum() {
		return this.newsignRenewalSubjectNum;
	}

	public void setNewsignRenewalSubjectNum(Integer newsignRenewalSubjectNum) {
		this.newsignRenewalSubjectNum = newsignRenewalSubjectNum;
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

	@Column(name="PEOPLE_AVERAGE_SUBJECT_NUM", precision = 10)
	public Double getPeopleAverageSubjectNum() {
		return this.peopleAverageSubjectNum;
	}

	public void setPeopleAverageSubjectNum(Double peopleAverageSubjectNum) {
		this.peopleAverageSubjectNum = peopleAverageSubjectNum;
	}

	@Column(name="TOTAL_OPEN_CLASS_NUM")
	public Integer getTotalOpenClassNum() {
		return this.totalOpenClassNum;
	}

	public void setTotalOpenClassNum(Integer totalOpenClassNum) {
		this.totalOpenClassNum = totalOpenClassNum;
	}

	@Column(name="CLASS_AVERAGE_PEOPLE_NUM")
	public Double getClassAveragePeopleNum() {
		return this.classAveragePeopleNum;
	}

	public void setClassAveragePeopleNum(Double classAveragePeopleNum) {
		this.classAveragePeopleNum = classAveragePeopleNum;
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

	@Column(name="PLAN_COURSEHOUR", precision = 10)
	public Double getPlanCoursehour() {
		return this.planCoursehour;
	}

	public void setPlanCoursehour(Double planCoursehour) {
		this.planCoursehour = planCoursehour;
	}

	@Column(name="CAMPUS_REAL_TOTAL_COURSEHOUR", precision = 16)
	public Double getCampusRealTotalCoursehour() {
		return this.campusRealTotalCoursehour;
	}

	public void setCampusRealTotalCoursehour(Double campusRealTotalCoursehour) {
		this.campusRealTotalCoursehour = campusRealTotalCoursehour;
	}

	@Column(name="COURSEHOUR_REACH_RATE", precision = 10)
	public Double getCoursehourReachRate() {
		return this.coursehourReachRate;
	}

	public void setCoursehourReachRate(Double coursehourReachRate) {
		this.coursehourReachRate = coursehourReachRate;
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

	@Column(name="PRIMARY_SCHOOL_GRADE1TO5_RATE", precision = 10)
	public Double getPrimarySchoolGrade1to5Rate() {
		return this.primarySchoolGrade1to5Rate;
	}

	public void setPrimarySchoolGrade1to5Rate(Double primarySchoolGrade1to5Rate) {
		this.primarySchoolGrade1to5Rate = primarySchoolGrade1to5Rate;
	}

	@Column(name="PRIMARY_SCHOOL_GRADE6_RATE", precision = 10)
	public Double getPrimarySchoolGrade6Rate() {
		return this.primarySchoolGrade6Rate;
	}

	public void setPrimarySchoolGrade6Rate(Double primarySchoolGrade6Rate) {
		this.primarySchoolGrade6Rate = primarySchoolGrade6Rate;
	}

	@Column(name="MIDDLE_SCHOOL_GRADE1_RATE", precision = 10)
	public Double getMiddleSchoolGrade1Rate() {
		return this.middleSchoolGrade1Rate;
	}

	public void setMiddleSchoolGrade1Rate(Double middleSchoolGrade1Rate) {
		this.middleSchoolGrade1Rate = middleSchoolGrade1Rate;
	}

	@Column(name="MIDDLE_SCHOOL_GRADE2_RATE", precision = 10)
	public Double getMiddleSchoolGrade2Rate() {
		return this.middleSchoolGrade2Rate;
	}

	public void setMiddleSchoolGrade2Rate(Double middleSchoolGrade2Rate) {
		this.middleSchoolGrade2Rate = middleSchoolGrade2Rate;
	}

	@Column(name="MIDDLE_SCHOOL_GRADE3_RATE", precision = 10)
	public Double getMiddleSchoolGrade3Rate() {
		return this.middleSchoolGrade3Rate;
	}

	public void setMiddleSchoolGrade3Rate(Double middleSchoolGrade3Rate) {
		this.middleSchoolGrade3Rate = middleSchoolGrade3Rate;
	}

	@Column(name="HIGH_SCHOOL_GRADE1_RATE", precision = 10)
	public Double getHighSchoolGrade1Rate() {
		return this.highSchoolGrade1Rate;
	}

	public void setHighSchoolGrade1Rate(Double highSchoolGrade1Rate) {
		this.highSchoolGrade1Rate = highSchoolGrade1Rate;
	}

	@Column(name="HIGH_SCHOOL_GRADE2_RATE", precision = 10)
	public Double getHighSchoolGrade2Rate() {
		return this.highSchoolGrade2Rate;
	}

	public void setHighSchoolGrade2Rate(Double highSchoolGrade2Rate) {
		this.highSchoolGrade2Rate = highSchoolGrade2Rate;
	}

	@Column(name="HIGH_SCHOOL_GRADE3_RATE", precision = 10)
	public Double getHighSchoolGrade3Rate() {
		return this.highSchoolGrade3Rate;
	}

	public void setHighSchoolGrade3Rate(Double highSchoolGrade3Rate) {
		this.highSchoolGrade3Rate = highSchoolGrade3Rate;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	

}