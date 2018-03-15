package com.eduboss.domain;

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

import com.eduboss.common.SummaryCycleType;

/**
 * SummaryStudymanagerPersonal entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "SUMMARY_STUDYMANAGER_PERSONAL")
public class SummaryStudymanagerPersonal implements java.io.Serializable {

	// Fields

	private String id;
	private SummaryCycleType summaryCycleType;
	private String summaryCycleStartDate;
	private String summaryCycleEndDate;
	private String createTime;
	private User studyManager;
	private Double oneononeSignAdvancePrice;
	private Integer oneononeSignPeopleNum;
	private Integer oneononeSignSubjectNum;
	private Double oneononeSignCoursehour;
	private Double oneononeGiveCoursehour;
	private Double oneononeAveragePrice;
	private Double oneononeGiveRate;
	private Double oneononePeopleAverageSubjectnum;
	private Double miniclassSignAdvancePrice;
	private Integer miniclassSignPeopleNum;
	private Integer miniclassSignSubjectNum;
	private Double miniclassAveragePrice;
	private Double miniclassPeopleAverageSubjectnum;
	private Double promiseclassSignAdvancePrice;
	private Integer promiseclassSignPeopleNum;
	private Double personalTotalAdvancePrice;
	private Double advanceTarget;
	private Double oneononeRefund;
	private Double miniclassRefund;
	private Double advanceTargetReachRate;
	private Double oneononeAdvanceTarget;
	private Double oneononeAdvanceReachRate;
	private Integer miniclassRefundSubjectNum;
	private Integer miniclassSubjectNumTarget;
	private Double miniclassSubjectnumReachRate;
	private Double miniclassAdvanceTarget;
	private Double miniclassAdvanceReachRate;
	private Double promiseclassAdvanceTarget;
	private Double promiseclassAdvanceReachRate;
	private Integer changeReferralNum;

	// Constructors

	/** default constructor */
	public SummaryStudymanagerPersonal() {
	}

	/** minimal constructor */
	public SummaryStudymanagerPersonal(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			User studyManagerId) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.studyManager = studyManagerId;
	}

	/** full constructor */
	public SummaryStudymanagerPersonal(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			User studyManager, Double oneononeSignAdvancePrice,
			Integer oneononeSignPeopleNum, Integer oneononeSignSubjectNum,
			Double oneononeSignCoursehour, Double oneononeGiveCoursehour,
			Double oneononeAveragePrice, Double oneononeGiveRate,
			Double oneononePeopleAverageSubjectnum,
			Double miniclassSignAdvancePrice, Integer miniclassSignPeopleNum,
			Integer miniclassSignSubjectNum, Double miniclassAveragePrice,
			Double miniclassPeopleAverageSubjectnum,
			Double promiseclassSignAdvancePrice,
			Integer promiseclassSignPeopleNum, Double personalTotalAdvancePrice,
			Double advanceTarget, Double oneononeRefund,
			Double miniclassRefund, Double advanceTargetReachRate,
			Double oneononeAdvanceTarget, Double oneononeAdvanceReachRate,
			Integer miniclassRefundSubjectNum,
			Integer miniclassSubjectNumTarget,
			Double miniclassSubjectnumReachRate, Double miniclassAdvanceTarget,
			Double miniclassAdvanceReachRate, Double promiseclassAdvanceTarget,
			Double promiseclassAdvanceReachRate, Integer changeReferralNum) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.studyManager = studyManager;
		this.oneononeSignAdvancePrice = oneononeSignAdvancePrice;
		this.oneononeSignPeopleNum = oneononeSignPeopleNum;
		this.oneononeSignSubjectNum = oneononeSignSubjectNum;
		this.oneononeSignCoursehour = oneononeSignCoursehour;
		this.oneononeGiveCoursehour = oneononeGiveCoursehour;
		this.oneononeAveragePrice = oneononeAveragePrice;
		this.oneononeGiveRate = oneononeGiveRate;
		this.oneononePeopleAverageSubjectnum = oneononePeopleAverageSubjectnum;
		this.miniclassSignAdvancePrice = miniclassSignAdvancePrice;
		this.miniclassSignPeopleNum = miniclassSignPeopleNum;
		this.miniclassSignSubjectNum = miniclassSignSubjectNum;
		this.miniclassAveragePrice = miniclassAveragePrice;
		this.miniclassPeopleAverageSubjectnum = miniclassPeopleAverageSubjectnum;
		this.promiseclassSignAdvancePrice = promiseclassSignAdvancePrice;
		this.promiseclassSignPeopleNum = promiseclassSignPeopleNum;
		this.personalTotalAdvancePrice = personalTotalAdvancePrice;
		this.advanceTarget = advanceTarget;
		this.oneononeRefund = oneononeRefund;
		this.miniclassRefund = miniclassRefund;
		this.advanceTargetReachRate = advanceTargetReachRate;
		this.oneononeAdvanceTarget = oneononeAdvanceTarget;
		this.oneononeAdvanceReachRate = oneononeAdvanceReachRate;
		this.miniclassRefundSubjectNum = miniclassRefundSubjectNum;
		this.miniclassSubjectNumTarget = miniclassSubjectNumTarget;
		this.miniclassSubjectnumReachRate = miniclassSubjectnumReachRate;
		this.miniclassAdvanceTarget = miniclassAdvanceTarget;
		this.miniclassAdvanceReachRate = miniclassAdvanceReachRate;
		this.promiseclassAdvanceTarget = promiseclassAdvanceTarget;
		this.promiseclassAdvanceReachRate = promiseclassAdvanceReachRate;
		this.changeReferralNum = changeReferralNum;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANAGER_ID")
	public User getStudyManager() {
		return this.studyManager;
	}

	public void setStudyManager(User studyManager) {
		this.studyManager = studyManager;
	}

	@Column(name = "ONEONONE_SIGN_ADVANCE_PRICE", length = 16)
	public Double getOneononeSignAdvancePrice() {
		return this.oneononeSignAdvancePrice;
	}

	public void setOneononeSignAdvancePrice(Double oneononeSignAdvancePrice) {
		this.oneononeSignAdvancePrice = oneononeSignAdvancePrice;
	}

	@Column(name = "ONEONONE_SIGN_PEOPLE_NUM")
	public Integer getOneononeSignPeopleNum() {
		return this.oneononeSignPeopleNum;
	}

	public void setOneononeSignPeopleNum(Integer oneononeSignPeopleNum) {
		this.oneononeSignPeopleNum = oneononeSignPeopleNum;
	}

	@Column(name = "ONEONONE_SIGN_SUBJECT_NUM")
	public Integer getOneononeSignSubjectNum() {
		return this.oneononeSignSubjectNum;
	}

	public void setOneononeSignSubjectNum(Integer oneononeSignSubjectNum) {
		this.oneononeSignSubjectNum = oneononeSignSubjectNum;
	}

	@Column(name = "ONEONONE_SIGN_COURSEHOUR", length = 10)
	public Double getOneononeSignCoursehour() {
		return this.oneononeSignCoursehour;
	}

	public void setOneononeSignCoursehour(Double oneononeSignCoursehour) {
		this.oneononeSignCoursehour = oneononeSignCoursehour;
	}

	@Column(name = "ONEONONE_GIVE_COURSEHOUR", length = 10)
	public Double getOneononeGiveCoursehour() {
		return this.oneononeGiveCoursehour;
	}

	public void setOneononeGiveCoursehour(Double oneononeGiveCoursehour) {
		this.oneononeGiveCoursehour = oneononeGiveCoursehour;
	}

	@Column(name = "ONEONONE_AVERAGE_PRICE", length = 10)
	public Double getOneononeAveragePrice() {
		return this.oneononeAveragePrice;
	}

	public void setOneononeAveragePrice(Double oneononeAveragePrice) {
		this.oneononeAveragePrice = oneononeAveragePrice;
	}

	@Column(name = "ONEONONE_GIVE_RATE", length = 10)
	public Double getOneononeGiveRate() {
		return this.oneononeGiveRate;
	}

	public void setOneononeGiveRate(Double oneononeGiveRate) {
		this.oneononeGiveRate = oneononeGiveRate;
	}

	@Column(name = "ONEONONE_PEOPLE_AVERAGE_SUBJECTNUM", length = 10)
	public Double getOneononePeopleAverageSubjectnum() {
		return this.oneononePeopleAverageSubjectnum;
	}

	public void setOneononePeopleAverageSubjectnum(
			Double oneononePeopleAverageSubjectnum) {
		this.oneononePeopleAverageSubjectnum = oneononePeopleAverageSubjectnum;
	}

	@Column(name = "MINICLASS_SIGN_ADVANCE_PRICE", length = 16)
	public Double getMiniclassSignAdvancePrice() {
		return this.miniclassSignAdvancePrice;
	}

	public void setMiniclassSignAdvancePrice(Double miniclassSignAdvancePrice) {
		this.miniclassSignAdvancePrice = miniclassSignAdvancePrice;
	}

	@Column(name = "MINICLASS_SIGN_PEOPLE_NUM")
	public Integer getMiniclassSignPeopleNum() {
		return this.miniclassSignPeopleNum;
	}

	public void setMiniclassSignPeopleNum(Integer miniclassSignPeopleNum) {
		this.miniclassSignPeopleNum = miniclassSignPeopleNum;
	}

	@Column(name = "MINICLASS_SIGN_SUBJECT_NUM")
	public Integer getMiniclassSignSubjectNum() {
		return this.miniclassSignSubjectNum;
	}

	public void setMiniclassSignSubjectNum(Integer miniclassSignSubjectNum) {
		this.miniclassSignSubjectNum = miniclassSignSubjectNum;
	}

	@Column(name = "MINICLASS_AVERAGE_PRICE", length = 10)
	public Double getMiniclassAveragePrice() {
		return this.miniclassAveragePrice;
	}

	public void setMiniclassAveragePrice(Double miniclassAveragePrice) {
		this.miniclassAveragePrice = miniclassAveragePrice;
	}

	@Column(name = "MINICLASS_PEOPLE_AVERAGE_SUBJECTNUM", length = 10)
	public Double getMiniclassPeopleAverageSubjectnum() {
		return this.miniclassPeopleAverageSubjectnum;
	}

	public void setMiniclassPeopleAverageSubjectnum(
			Double miniclassPeopleAverageSubjectnum) {
		this.miniclassPeopleAverageSubjectnum = miniclassPeopleAverageSubjectnum;
	}

	@Column(name = "PROMISECLASS_SIGN_ADVANCE_PRICE", length = 16)
	public Double getPromiseclassSignAdvancePrice() {
		return this.promiseclassSignAdvancePrice;
	}

	public void setPromiseclassSignAdvancePrice(
			Double promiseclassSignAdvancePrice) {
		this.promiseclassSignAdvancePrice = promiseclassSignAdvancePrice;
	}

	@Column(name = "PROMISECLASS_SIGN_PEOPLE_NUM")
	public Integer getPromiseclassSignPeopleNum() {
		return this.promiseclassSignPeopleNum;
	}

	public void setPromiseclassSignPeopleNum(Integer promiseclassSignPeopleNum) {
		this.promiseclassSignPeopleNum = promiseclassSignPeopleNum;
	}

	@Column(name = "PERSONAL_TOTAL_ADVANCE_PRICE", length = 16)
	public Double getPersonalTotalAdvancePrice() {
		return this.personalTotalAdvancePrice;
	}

	public void setPersonalTotalAdvancePrice(Double personalTotalAdvancePrice) {
		this.personalTotalAdvancePrice = personalTotalAdvancePrice;
	}

	@Column(name = "ADVANCE_TARGET", length = 16)
	public Double getAdvanceTarget() {
		return this.advanceTarget;
	}

	public void setAdvanceTarget(Double advanceTarget) {
		this.advanceTarget = advanceTarget;
	}

	@Column(name = "ONEONONE_REFUND", length = 16)
	public Double getOneononeRefund() {
		return this.oneononeRefund;
	}

	public void setOneononeRefund(Double oneononeRefund) {
		this.oneononeRefund = oneononeRefund;
	}

	@Column(name = "MINICLASS_REFUND", length = 16)
	public Double getMiniclassRefund() {
		return this.miniclassRefund;
	}

	public void setMiniclassRefund(Double miniclassRefund) {
		this.miniclassRefund = miniclassRefund;
	}

	@Column(name = "ADVANCE_TARGET_REACH_RATE", length = 10)
	public Double getAdvanceTargetReachRate() {
		return this.advanceTargetReachRate;
	}

	public void setAdvanceTargetReachRate(Double advanceTargetReachRate) {
		this.advanceTargetReachRate = advanceTargetReachRate;
	}

	@Column(name = "ONEONONE_ADVANCE_TARGET", length = 16)
	public Double getOneononeAdvanceTarget() {
		return this.oneononeAdvanceTarget;
	}

	public void setOneononeAdvanceTarget(Double oneononeAdvanceTarget) {
		this.oneononeAdvanceTarget = oneononeAdvanceTarget;
	}

	@Column(name = "ONEONONE_ADVANCE_REACH_RATE", length = 10)
	public Double getOneononeAdvanceReachRate() {
		return this.oneononeAdvanceReachRate;
	}

	public void setOneononeAdvanceReachRate(Double oneononeAdvanceReachRate) {
		this.oneononeAdvanceReachRate = oneononeAdvanceReachRate;
	}

	@Column(name = "MINICLASS_REFUND_SUBJECT_NUM")
	public Integer getMiniclassRefundSubjectNum() {
		return this.miniclassRefundSubjectNum;
	}

	public void setMiniclassRefundSubjectNum(Integer miniclassRefundSubjectNum) {
		this.miniclassRefundSubjectNum = miniclassRefundSubjectNum;
	}

	@Column(name = "MINICLASS_SUBJECT_NUM_TARGET")
	public Integer getMiniclassSubjectNumTarget() {
		return this.miniclassSubjectNumTarget;
	}

	public void setMiniclassSubjectNumTarget(Integer miniclassSubjectNumTarget) {
		this.miniclassSubjectNumTarget = miniclassSubjectNumTarget;
	}

	@Column(name = "MINICLASS_SUBJECTNUM_REACH_RATE", length = 10)
	public Double getMiniclassSubjectnumReachRate() {
		return this.miniclassSubjectnumReachRate;
	}

	public void setMiniclassSubjectnumReachRate(
			Double miniclassSubjectnumReachRate) {
		this.miniclassSubjectnumReachRate = miniclassSubjectnumReachRate;
	}

	@Column(name = "MINICLASS_ADVANCE_TARGET", length = 16)
	public Double getMiniclassAdvanceTarget() {
		return this.miniclassAdvanceTarget;
	}

	public void setMiniclassAdvanceTarget(Double miniclassAdvanceTarget) {
		this.miniclassAdvanceTarget = miniclassAdvanceTarget;
	}

	@Column(name = "MINICLASS_ADVANCE_REACH_RATE", length = 10)
	public Double getMiniclassAdvanceReachRate() {
		return this.miniclassAdvanceReachRate;
	}

	public void setMiniclassAdvanceReachRate(Double miniclassAdvanceReachRate) {
		this.miniclassAdvanceReachRate = miniclassAdvanceReachRate;
	}

	@Column(name = "PROMISECLASS_ADVANCE_TARGET", length = 16)
	public Double getPromiseclassAdvanceTarget() {
		return this.promiseclassAdvanceTarget;
	}

	public void setPromiseclassAdvanceTarget(Double promiseclassAdvanceTarget) {
		this.promiseclassAdvanceTarget = promiseclassAdvanceTarget;
	}

	@Column(name = "PROMISECLASS_ADVANCE_REACH_RATE", length = 10)
	public Double getPromiseclassAdvanceReachRate() {
		return this.promiseclassAdvanceReachRate;
	}

	public void setPromiseclassAdvanceReachRate(
			Double promiseclassAdvanceReachRate) {
		this.promiseclassAdvanceReachRate = promiseclassAdvanceReachRate;
	}

	@Column(name = "CHANGE_REFERRAL_NUM")
	public Integer getChangeReferralNum() {
		return this.changeReferralNum;
	}

	public void setChangeReferralNum(Integer changeReferralNum) {
		this.changeReferralNum = changeReferralNum;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	

}