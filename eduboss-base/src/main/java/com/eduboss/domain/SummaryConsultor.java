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
 * SummaryConsultor entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "SUMMARY_CONSULTOR")
public class SummaryConsultor implements java.io.Serializable {

	// Fields

	private String id;
	private SummaryCycleType summaryCycleType;
	private String summaryCycleStartDate;
	private String summaryCycleEndDate;
	private String createTime;
	private User consultor;
	private Integer directVisitNum;
	private Integer directSignNum;
	private Double directSignRate;
	private Integer incomingTelNum;
	private Integer incomingTelArrangeVisit;
	private Double incomingTelArrangeRate;
	private Integer effectiveSpeakPopularizeNum;
	private Integer speakPopularizeArrangeVisit;
	private Double speakPopularizeArrangeRate;
	private Integer strangeVisitConsultationNum;
	private Integer strangeVisitArrangeVisit;
	private Double strangeVisitArrangeRate;
	private Integer onlineEffectiveResource;
	private Integer onlineResourceVisit;
	private Integer outerCallEffectiveResource;
	private Integer outerCallResourceVisit;
	private Integer otherEffectiveResource;
	private Integer otherResourceVisit;
	private Integer totalResource;
	private Integer totalVisitSignNum;
	private Integer totalVisitNum;
	private Double totalVisitSignRate;
	private Integer oneononeSignAdvancePrice;
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
	private Integer totalNewsignPeopleNum;
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
	public SummaryConsultor() {
	}

	/** minimal constructor */
	public SummaryConsultor(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			User consultor) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.consultor = consultor;
	}

	/** full constructor */
	public SummaryConsultor(String id, SummaryCycleType summaryCycleType,
			String summaryCycleStartDate, String summaryCycleEndDate,
			User consultorId, Integer directVisitNum, Integer directSignNum,
			Double directSignRate, Integer incomingTelNum,
			Integer incomingTelArrangeVisit, Double incomingTelArrangeRate,
			Integer effectiveSpeakPopularizeNum,
			Integer speakPopularizeArrangeVisit,
			Double speakPopularizeArrangeRate,
			Integer strangeVisitConsultationNum,
			Integer strangeVisitArrangeVisit, Double strangeVisitArrangeRate,
			Integer onlineEffectiveResource, Integer onlineResourceVisit,
			Integer outerCallEffectiveResource, Integer outerCallResourceVisit,
			Integer otherEffectiveResource, Integer otherResourceVisit,
			Integer totalResource, Integer totalVisitSignNum,
			Integer totalVisitNum, Double totalVisitSignRate,
			Integer oneononeSignAdvancePrice, Integer oneononeSignPeopleNum,
			Integer oneononeSignSubjectNum, Double oneononeSignCoursehour,
			Double oneononeGiveCoursehour, Double oneononeAveragePrice,
			Double oneononeGiveRate, Double oneononePeopleAverageSubjectnum,
			Double miniclassSignAdvancePrice, Integer miniclassSignPeopleNum,
			Integer miniclassSignSubjectNum, Double miniclassAveragePrice,
			Double miniclassPeopleAverageSubjectnum,
			Double promiseclassSignAdvancePrice,
			Integer promiseclassSignPeopleNum, Double personalTotalAdvancePrice,
			Double advanceTarget, Double oneononeRefund,
			Double miniclassRefund, Double advanceTargetReachRate,
			Integer totalNewsignPeopleNum, Double oneononeAdvanceTarget,
			Double oneononeAdvanceReachRate, Integer miniclassRefundSubjectNum,
			Integer miniclassSubjectNumTarget,
			Double miniclassSubjectnumReachRate, Double miniclassAdvanceTarget,
			Double miniclassAdvanceReachRate, Double promiseclassAdvanceTarget,
			Double promiseclassAdvanceReachRate, Integer changeReferralNum) {
		this.id = id;
		this.summaryCycleType = summaryCycleType;
		this.summaryCycleStartDate = summaryCycleStartDate;
		this.summaryCycleEndDate = summaryCycleEndDate;
		this.consultor = consultorId;
		this.directVisitNum = directVisitNum;
		this.directSignNum = directSignNum;
		this.directSignRate = directSignRate;
		this.incomingTelNum = incomingTelNum;
		this.incomingTelArrangeVisit = incomingTelArrangeVisit;
		this.incomingTelArrangeRate = incomingTelArrangeRate;
		this.effectiveSpeakPopularizeNum = effectiveSpeakPopularizeNum;
		this.speakPopularizeArrangeVisit = speakPopularizeArrangeVisit;
		this.speakPopularizeArrangeRate = speakPopularizeArrangeRate;
		this.strangeVisitConsultationNum = strangeVisitConsultationNum;
		this.strangeVisitArrangeVisit = strangeVisitArrangeVisit;
		this.strangeVisitArrangeRate = strangeVisitArrangeRate;
		this.onlineEffectiveResource = onlineEffectiveResource;
		this.onlineResourceVisit = onlineResourceVisit;
		this.outerCallEffectiveResource = outerCallEffectiveResource;
		this.outerCallResourceVisit = outerCallResourceVisit;
		this.otherEffectiveResource = otherEffectiveResource;
		this.otherResourceVisit = otherResourceVisit;
		this.totalResource = totalResource;
		this.totalVisitSignNum = totalVisitSignNum;
		this.totalVisitNum = totalVisitNum;
		this.totalVisitSignRate = totalVisitSignRate;
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
		this.totalNewsignPeopleNum = totalNewsignPeopleNum;
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
	@JoinColumn(name = "CONSULTOR_ID")
	public User getConsultor() {
		return this.consultor;
	}

	public void setConsultor(User consultor) {
		this.consultor = consultor;
	}

	@Column(name="DIRECT_VISIT_NUM")
	public Integer getDirectVisitNum() {
		return this.directVisitNum;
	}

	public void setDirectVisitNum(Integer directVisitNum) {
		this.directVisitNum = directVisitNum;
	}

	@Column(name="DIRECT_SIGN_NUM")
	public Integer getDirectSignNum() {
		return this.directSignNum;
	}

	public void setDirectSignNum(Integer directSignNum) {
		this.directSignNum = directSignNum;
	}

	@Column(name="DIRECT_SIGN_RATE", precision = 10)
	public Double getDirectSignRate() {
		return this.directSignRate;
	}

	public void setDirectSignRate(Double directSignRate) {
		this.directSignRate = directSignRate;
	}

	@Column(name="INCOMING_TEL_NUM")
	public Integer getIncomingTelNum() {
		return this.incomingTelNum;
	}

	public void setIncomingTelNum(Integer incomingTelNum) {
		this.incomingTelNum = incomingTelNum;
	}

	@Column(name="INCOMING_TEL_ARRANGE_VISIT")
	public Integer getIncomingTelArrangeVisit() {
		return this.incomingTelArrangeVisit;
	}

	public void setIncomingTelArrangeVisit(Integer incomingTelArrangeVisit) {
		this.incomingTelArrangeVisit = incomingTelArrangeVisit;
	}

	@Column(name="INCOMING_TEL_ARRANGE_RATE", precision = 10)
	public Double getIncomingTelArrangeRate() {
		return this.incomingTelArrangeRate;
	}

	public void setIncomingTelArrangeRate(Double incomingTelArrangeRate) {
		this.incomingTelArrangeRate = incomingTelArrangeRate;
	}

	@Column(name="EFFECTIVE_SPEAK_POPULARIZE_NUM")
	public Integer getEffectiveSpeakPopularizeNum() {
		return this.effectiveSpeakPopularizeNum;
	}

	public void setEffectiveSpeakPopularizeNum(
			Integer effectiveSpeakPopularizeNum) {
		this.effectiveSpeakPopularizeNum = effectiveSpeakPopularizeNum;
	}

	@Column(name="SPEAK_POPULARIZE_ARRANGE_VISIT")
	public Integer getSpeakPopularizeArrangeVisit() {
		return this.speakPopularizeArrangeVisit;
	}

	public void setSpeakPopularizeArrangeVisit(
			Integer speakPopularizeArrangeVisit) {
		this.speakPopularizeArrangeVisit = speakPopularizeArrangeVisit;
	}

	@Column(name="SPEAK_POPULARIZE_ARRANGE_RATE", precision = 10)
	public Double getSpeakPopularizeArrangeRate() {
		return this.speakPopularizeArrangeRate;
	}

	public void setSpeakPopularizeArrangeRate(Double speakPopularizeArrangeRate) {
		this.speakPopularizeArrangeRate = speakPopularizeArrangeRate;
	}

	@Column(name="STRANGE_VISIT_CONSULTATION_NUM")
	public Integer getStrangeVisitConsultationNum() {
		return this.strangeVisitConsultationNum;
	}

	public void setStrangeVisitConsultationNum(
			Integer strangeVisitConsultationNum) {
		this.strangeVisitConsultationNum = strangeVisitConsultationNum;
	}

	@Column(name="STRANGE_VISIT_ARRANGE_VISIT")
	public Integer getStrangeVisitArrangeVisit() {
		return this.strangeVisitArrangeVisit;
	}

	public void setStrangeVisitArrangeVisit(Integer strangeVisitArrangeVisit) {
		this.strangeVisitArrangeVisit = strangeVisitArrangeVisit;
	}

	@Column(name="STRANGE_VISIT_ARRANGE_RATE", precision = 10)
	public Double getStrangeVisitArrangeRate() {
		return this.strangeVisitArrangeRate;
	}

	public void setStrangeVisitArrangeRate(Double strangeVisitArrangeRate) {
		this.strangeVisitArrangeRate = strangeVisitArrangeRate;
	}

	@Column(name="ONLINE_EFFECTIVE_RESOURCE")
	public Integer getOnlineEffectiveResource() {
		return this.onlineEffectiveResource;
	}

	public void setOnlineEffectiveResource(Integer onlineEffectiveResource) {
		this.onlineEffectiveResource = onlineEffectiveResource;
	}

	@Column(name="ONLINE_RESOURCE_VISIT")
	public Integer getOnlineResourceVisit() {
		return this.onlineResourceVisit;
	}

	public void setOnlineResourceVisit(Integer onlineResourceVisit) {
		this.onlineResourceVisit = onlineResourceVisit;
	}

	@Column(name="OUTER_CALL_EFFECTIVE_RESOURCE")
	public Integer getOuterCallEffectiveResource() {
		return this.outerCallEffectiveResource;
	}

	public void setOuterCallEffectiveResource(Integer outerCallEffectiveResource) {
		this.outerCallEffectiveResource = outerCallEffectiveResource;
	}

	@Column(name="OUTER_CALL_RESOURCE_VISIT")
	public Integer getOuterCallResourceVisit() {
		return this.outerCallResourceVisit;
	}

	public void setOuterCallResourceVisit(Integer outerCallResourceVisit) {
		this.outerCallResourceVisit = outerCallResourceVisit;
	}

	@Column(name="OTHER_EFFECTIVE_RESOURCE")
	public Integer getOtherEffectiveResource() {
		return this.otherEffectiveResource;
	}

	public void setOtherEffectiveResource(Integer otherEffectiveResource) {
		this.otherEffectiveResource = otherEffectiveResource;
	}

	@Column(name="OTHER_RESOURCE_VISIT")
	public Integer getOtherResourceVisit() {
		return this.otherResourceVisit;
	}

	public void setOtherResourceVisit(Integer otherResourceVisit) {
		this.otherResourceVisit = otherResourceVisit;
	}

	@Column(name="TOTAL_RESOURCE")
	public Integer getTotalResource() {
		return this.totalResource;
	}

	public void setTotalResource(Integer totalResource) {
		this.totalResource = totalResource;
	}

	@Column(name="TOTAL_VISIT_SIGN_NUM")
	public Integer getTotalVisitSignNum() {
		return this.totalVisitSignNum;
	}

	public void setTotalVisitSignNum(Integer totalVisitSignNum) {
		this.totalVisitSignNum = totalVisitSignNum;
	}

	@Column(name="TOTAL_VISIT_NUM")
	public Integer getTotalVisitNum() {
		return this.totalVisitNum;
	}

	public void setTotalVisitNum(Integer totalVisitNum) {
		this.totalVisitNum = totalVisitNum;
	}

	@Column(name="TOTAL_VISIT_SIGN_RATE", precision = 10)
	public Double getTotalVisitSignRate() {
		return this.totalVisitSignRate;
	}

	public void setTotalVisitSignRate(Double totalVisitSignRate) {
		this.totalVisitSignRate = totalVisitSignRate;
	}

	@Column(name="ONEONONE_SIGN_ADVANCE_PRICE")
	public Integer getOneononeSignAdvancePrice() {
		return this.oneononeSignAdvancePrice;
	}

	public void setOneononeSignAdvancePrice(Integer oneononeSignAdvancePrice) {
		this.oneononeSignAdvancePrice = oneononeSignAdvancePrice;
	}

	@Column(name="ONEONONE_SIGN_PEOPLE_NUM")
	public Integer getOneononeSignPeopleNum() {
		return this.oneononeSignPeopleNum;
	}

	public void setOneononeSignPeopleNum(Integer oneononeSignPeopleNum) {
		this.oneononeSignPeopleNum = oneononeSignPeopleNum;
	}

	@Column(name="ONEONONE_SIGN_SUBJECT_NUM")
	public Integer getOneononeSignSubjectNum() {
		return this.oneononeSignSubjectNum;
	}

	public void setOneononeSignSubjectNum(Integer oneononeSignSubjectNum) {
		this.oneononeSignSubjectNum = oneononeSignSubjectNum;
	}

	@Column(name="ONEONONE_SIGN_COURSEHOUR", precision = 10)
	public Double getOneononeSignCoursehour() {
		return this.oneononeSignCoursehour;
	}

	public void setOneononeSignCoursehour(Double oneononeSignCoursehour) {
		this.oneononeSignCoursehour = oneononeSignCoursehour;
	}

	@Column(name="ONEONONE_GIVE_COURSEHOUR", precision = 10)
	public Double getOneononeGiveCoursehour() {
		return this.oneononeGiveCoursehour;
	}

	public void setOneononeGiveCoursehour(Double oneononeGiveCoursehour) {
		this.oneononeGiveCoursehour = oneononeGiveCoursehour;
	}

	@Column(name="ONEONONE_AVERAGE_PRICE", precision = 10)
	public Double getOneononeAveragePrice() {
		return this.oneononeAveragePrice;
	}

	public void setOneononeAveragePrice(Double oneononeAveragePrice) {
		this.oneononeAveragePrice = oneononeAveragePrice;
	}

	@Column(name="ONEONONE_GIVE_RATE", precision = 10)
	public Double getOneononeGiveRate() {
		return this.oneononeGiveRate;
	}

	public void setOneononeGiveRate(Double oneononeGiveRate) {
		this.oneononeGiveRate = oneononeGiveRate;
	}

	@Column(name="ONEONONE_PEOPLE_AVERAGE_SUBJECTNUM", precision = 10)
	public Double getOneononePeopleAverageSubjectnum() {
		return this.oneononePeopleAverageSubjectnum;
	}

	public void setOneononePeopleAverageSubjectnum(
			Double oneononePeopleAverageSubjectnum) {
		this.oneononePeopleAverageSubjectnum = oneononePeopleAverageSubjectnum;
	}

	@Column(name="MINICLASS_SIGN_ADVANCE_PRICE", precision = 16)
	public Double getMiniclassSignAdvancePrice() {
		return this.miniclassSignAdvancePrice;
	}

	public void setMiniclassSignAdvancePrice(Double miniclassSignAdvancePrice) {
		this.miniclassSignAdvancePrice = miniclassSignAdvancePrice;
	}

	@Column(name="MINICLASS_SIGN_PEOPLE_NUM")
	public Integer getMiniclassSignPeopleNum() {
		return this.miniclassSignPeopleNum;
	}

	public void setMiniclassSignPeopleNum(Integer miniclassSignPeopleNum) {
		this.miniclassSignPeopleNum = miniclassSignPeopleNum;
	}

	@Column(name="MINICLASS_SIGN_SUBJECT_NUM")
	public Integer getMiniclassSignSubjectNum() {
		return this.miniclassSignSubjectNum;
	}

	public void setMiniclassSignSubjectNum(Integer miniclassSignSubjectNum) {
		this.miniclassSignSubjectNum = miniclassSignSubjectNum;
	}

	@Column(name="MINICLASS_AVERAGE_PRICE", precision = 10)
	public Double getMiniclassAveragePrice() {
		return this.miniclassAveragePrice;
	}

	public void setMiniclassAveragePrice(Double miniclassAveragePrice) {
		this.miniclassAveragePrice = miniclassAveragePrice;
	}

	@Column(name="MINICLASS_PEOPLE_AVERAGE_SUBJECTNUM", precision = 10)
	public Double getMiniclassPeopleAverageSubjectnum() {
		return this.miniclassPeopleAverageSubjectnum;
	}

	public void setMiniclassPeopleAverageSubjectnum(
			Double miniclassPeopleAverageSubjectnum) {
		this.miniclassPeopleAverageSubjectnum = miniclassPeopleAverageSubjectnum;
	}

	@Column(name="PROMISECLASS_SIGN_ADVANCE_PRICE", precision = 16)
	public Double getPromiseclassSignAdvancePrice() {
		return this.promiseclassSignAdvancePrice;
	}

	public void setPromiseclassSignAdvancePrice(
			Double promiseclassSignAdvancePrice) {
		this.promiseclassSignAdvancePrice = promiseclassSignAdvancePrice;
	}

	@Column(name="PROMISECLASS_SIGN_PEOPLE_NUM")
	public Integer getPromiseclassSignPeopleNum() {
		return this.promiseclassSignPeopleNum;
	}

	public void setPromiseclassSignPeopleNum(Integer promiseclassSignPeopleNum) {
		this.promiseclassSignPeopleNum = promiseclassSignPeopleNum;
	}

	@Column(name="PERSONAL_TOTAL_ADVANCE_PRICE", precision = 16)
	public Double getPersonalTotalAdvancePrice() {
		return this.personalTotalAdvancePrice;
	}

	public void setPersonalTotalAdvancePrice(Double personalTotalAdvancePrice) {
		this.personalTotalAdvancePrice = personalTotalAdvancePrice;
	}

	@Column(name="ADVANCE_TARGET", precision = 16)
	public Double getAdvanceTarget() {
		return this.advanceTarget;
	}

	public void setAdvanceTarget(Double advanceTarget) {
		this.advanceTarget = advanceTarget;
	}

	@Column(name="ONEONONE_REFUND", precision = 16)
	public Double getOneononeRefund() {
		return this.oneononeRefund;
	}

	public void setOneononeRefund(Double oneononeRefund) {
		this.oneononeRefund = oneononeRefund;
	}

	@Column(name="MINICLASS_REFUND", precision = 16)
	public Double getMiniclassRefund() {
		return this.miniclassRefund;
	}

	public void setMiniclassRefund(Double miniclassRefund) {
		this.miniclassRefund = miniclassRefund;
	}

	@Column(name="ADVANCE_TARGET_REACH_RATE", precision = 10)
	public Double getAdvanceTargetReachRate() {
		return this.advanceTargetReachRate;
	}

	public void setAdvanceTargetReachRate(Double advanceTargetReachRate) {
		this.advanceTargetReachRate = advanceTargetReachRate;
	}

	@Column(name="TOTAL_NEWSIGN_PEOPLE_NUM")
	public Integer getTotalNewsignPeopleNum() {
		return this.totalNewsignPeopleNum;
	}

	public void setTotalNewsignPeopleNum(Integer totalNewsignPeopleNum) {
		this.totalNewsignPeopleNum = totalNewsignPeopleNum;
	}

	@Column(name="ONEONONE_ADVANCE_TARGET", precision = 16)
	public Double getOneononeAdvanceTarget() {
		return this.oneononeAdvanceTarget;
	}

	public void setOneononeAdvanceTarget(Double oneononeAdvanceTarget) {
		this.oneononeAdvanceTarget = oneononeAdvanceTarget;
	}

	@Column(name="ONEONONE_ADVANCE_REACH_RATE", precision = 10)
	public Double getOneononeAdvanceReachRate() {
		return this.oneononeAdvanceReachRate;
	}

	public void setOneononeAdvanceReachRate(Double oneononeAdvanceReachRate) {
		this.oneononeAdvanceReachRate = oneononeAdvanceReachRate;
	}

	@Column(name="MINICLASS_REFUND_SUBJECT_NUM")
	public Integer getMiniclassRefundSubjectNum() {
		return this.miniclassRefundSubjectNum;
	}

	public void setMiniclassRefundSubjectNum(Integer miniclassRefundSubjectNum) {
		this.miniclassRefundSubjectNum = miniclassRefundSubjectNum;
	}

	@Column(name="MINICLASS_SUBJECT_NUM_TARGET")
	public Integer getMiniclassSubjectNumTarget() {
		return this.miniclassSubjectNumTarget;
	}

	public void setMiniclassSubjectNumTarget(Integer miniclassSubjectNumTarget) {
		this.miniclassSubjectNumTarget = miniclassSubjectNumTarget;
	}

	@Column(name="MINICLASS_SUBJECTNUM_REACH_RATE", precision = 10)
	public Double getMiniclassSubjectnumReachRate() {
		return this.miniclassSubjectnumReachRate;
	}

	public void setMiniclassSubjectnumReachRate(
			Double miniclassSubjectnumReachRate) {
		this.miniclassSubjectnumReachRate = miniclassSubjectnumReachRate;
	}

	@Column(name="MINICLASS_ADVANCE_TARGET", precision = 16)
	public Double getMiniclassAdvanceTarget() {
		return this.miniclassAdvanceTarget;
	}

	public void setMiniclassAdvanceTarget(Double miniclassAdvanceTarget) {
		this.miniclassAdvanceTarget = miniclassAdvanceTarget;
	}

	@Column(name="MINICLASS_ADVANCE_REACH_RATE", precision = 10)
	public Double getMiniclassAdvanceReachRate() {
		return this.miniclassAdvanceReachRate;
	}

	public void setMiniclassAdvanceReachRate(Double miniclassAdvanceReachRate) {
		this.miniclassAdvanceReachRate = miniclassAdvanceReachRate;
	}

	@Column(name="PROMISECLASS_ADVANCE_TARGET", precision = 16)
	public Double getPromiseclassAdvanceTarget() {
		return this.promiseclassAdvanceTarget;
	}

	public void setPromiseclassAdvanceTarget(Double promiseclassAdvanceTarget) {
		this.promiseclassAdvanceTarget = promiseclassAdvanceTarget;
	}

	@Column(name="PROMISECLASS_ADVANCE_REACH_RATE", precision = 10)
	public Double getPromiseclassAdvanceReachRate() {
		return this.promiseclassAdvanceReachRate;
	}

	public void setPromiseclassAdvanceReachRate(
			Double promiseclassAdvanceReachRate) {
		this.promiseclassAdvanceReachRate = promiseclassAdvanceReachRate;
	}

	@Column(name="CHANGE_REFERRAL_NUM")
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