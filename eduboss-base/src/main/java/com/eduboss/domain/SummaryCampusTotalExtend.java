package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * SummaryCampusTotalExtend entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "SUMMARY_CAMPUS_TOTAL_EXTEND")
public class SummaryCampusTotalExtend implements java.io.Serializable {

	// Fields

	private String id;
	private SummaryCampus summaryCampus;
	private Integer consultorNum;
	private Integer studyManagerNum;
	private Integer fulltimeTeacherNum;
	private Integer parttimeTeacherNum;
	private Integer otherPeopleNum;
	private Integer BTeacherNum;
	private Double fulltimeParttimeRate;
	private Integer leaveOfficePeopleNum;
	private Double campusLeaveOfficeRate;
	private Double teacherLeaveOfficeRate;
	private Integer directVisitNum;
	private Integer incomingTelNum;
	private Integer effectiveSpeakPopularizeNum;
	private Integer outerCallResourceNum;
	private Integer onlineResourceNum;
	private Integer otherResourseNum;
	private Integer totalResourceNum;
	private Integer visitNum;
	private Double directSignRate;
	private Double incomingTelInvitationRate;
	private Double totalVisitSignRate;

	// Constructors

	/** default constructor */
	public SummaryCampusTotalExtend() {
	}

	/** minimal constructor */
	public SummaryCampusTotalExtend(String id, SummaryCampus summaryCampus) {
		this.id = id;
		this.summaryCampus = summaryCampus;
	}

	/** full constructor */
	public SummaryCampusTotalExtend(String id, SummaryCampus summaryCampus,
			Integer consultorNum, Integer studyManagerNum,
			Integer fulltimeTeacherNum, Integer parttimeTeacherNum,
			Integer otherPeopleNum, Integer BTeacherNum,
			Double fulltimeParttimeRate, Integer leaveOfficePeopleNum,
			Double campusLeaveOfficeRate, Double teacherLeaveOfficeRate,
			Integer directVisitNum, Integer incomingTelNum,
			Integer effectiveSpeakPopularizeNum, Integer outerCallResourceNum,
			Integer onlineResourceNum, Integer otherResourseNum,
			Integer totalResourceNum, Integer visitNum, Double directSignRate,
			Double incomingTelInvitationRate, Double totalVisitSignRate) {
		this.id = id;
		this.summaryCampus = summaryCampus;
		this.consultorNum = consultorNum;
		this.studyManagerNum = studyManagerNum;
		this.fulltimeTeacherNum = fulltimeTeacherNum;
		this.parttimeTeacherNum = parttimeTeacherNum;
		this.otherPeopleNum = otherPeopleNum;
		this.BTeacherNum = BTeacherNum;
		this.fulltimeParttimeRate = fulltimeParttimeRate;
		this.leaveOfficePeopleNum = leaveOfficePeopleNum;
		this.campusLeaveOfficeRate = campusLeaveOfficeRate;
		this.teacherLeaveOfficeRate = teacherLeaveOfficeRate;
		this.directVisitNum = directVisitNum;
		this.incomingTelNum = incomingTelNum;
		this.effectiveSpeakPopularizeNum = effectiveSpeakPopularizeNum;
		this.outerCallResourceNum = outerCallResourceNum;
		this.onlineResourceNum = onlineResourceNum;
		this.otherResourseNum = otherResourseNum;
		this.totalResourceNum = totalResourceNum;
		this.visitNum = visitNum;
		this.directSignRate = directSignRate;
		this.incomingTelInvitationRate = incomingTelInvitationRate;
		this.totalVisitSignRate = totalVisitSignRate;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUMMARY_CAMPUS_ID")
	public SummaryCampus getSummaryCampus() {
		return this.summaryCampus;
	}

	public void setSummaryCampus(SummaryCampus summaryCampus) {
		this.summaryCampus = summaryCampus;
	}

	@Column(name="CONSULTOR_NUM")
	public Integer getConsultorNum() {
		return this.consultorNum;
	}

	public void setConsultorNum(Integer consultorNum) {
		this.consultorNum = consultorNum;
	}

	@Column(name="STUDY_MANAGER_NUM")
	public Integer getStudyManagerNum() {
		return this.studyManagerNum;
	}

	public void setStudyManagerNum(Integer studyManagerNum) {
		this.studyManagerNum = studyManagerNum;
	}

	@Column(name="FULLTIME_TEACHER_NUM")
	public Integer getFulltimeTeacherNum() {
		return this.fulltimeTeacherNum;
	}

	public void setFulltimeTeacherNum(Integer fulltimeTeacherNum) {
		this.fulltimeTeacherNum = fulltimeTeacherNum;
	}

	@Column(name="PARTTIME_TEACHER_NUM")
	public Integer getParttimeTeacherNum() {
		return this.parttimeTeacherNum;
	}

	public void setParttimeTeacherNum(Integer parttimeTeacherNum) {
		this.parttimeTeacherNum = parttimeTeacherNum;
	}

	@Column(name="OTHER_PEOPLE_NUM")
	public Integer getOtherPeopleNum() {
		return this.otherPeopleNum;
	}

	public void setOtherPeopleNum(Integer otherPeopleNum) {
		this.otherPeopleNum = otherPeopleNum;
	}

	@Column(name="B_TEACHER_NUM")
	public Integer getBTeacherNum() {
		return this.BTeacherNum;
	}

	public void setBTeacherNum(Integer BTeacherNum) {
		this.BTeacherNum = BTeacherNum;
	}

	@Column(name = "FULLTIME_PARTTIME_RATE", precision = 10)
	public Double getFulltimeParttimeRate() {
		return this.fulltimeParttimeRate;
	}

	public void setFulltimeParttimeRate(Double fulltimeParttimeRate) {
		this.fulltimeParttimeRate = fulltimeParttimeRate;
	}

	@Column(name = "LEAVE_OFFICE_PEOPLE_NUM")
	public Integer getLeaveOfficePeopleNum() {
		return this.leaveOfficePeopleNum;
	}

	public void setLeaveOfficePeopleNum(Integer leaveOfficePeopleNum) {
		this.leaveOfficePeopleNum = leaveOfficePeopleNum;
	}

	@Column(name = "CAMPUS_LEAVE_OFFICE_RATE", precision = 10)
	public Double getCampusLeaveOfficeRate() {
		return this.campusLeaveOfficeRate;
	}

	public void setCampusLeaveOfficeRate(Double campusLeaveOfficeRate) {
		this.campusLeaveOfficeRate = campusLeaveOfficeRate;
	}

	@Column(name = "TEACHER_LEAVE_OFFICE_RATE", precision = 10)
	public Double getTeacherLeaveOfficeRate() {
		return this.teacherLeaveOfficeRate;
	}

	public void setTeacherLeaveOfficeRate(Double teacherLeaveOfficeRate) {
		this.teacherLeaveOfficeRate = teacherLeaveOfficeRate;
	}

	@Column(name="DIRECT_VISIT_NUM")
	public Integer getDirectVisitNum() {
		return this.directVisitNum;
	}

	public void setDirectVisitNum(Integer directVisitNum) {
		this.directVisitNum = directVisitNum;
	}

	@Column(name="INCOMING_TEL_NUM")
	public Integer getIncomingTelNum() {
		return this.incomingTelNum;
	}

	public void setIncomingTelNum(Integer incomingTelNum) {
		this.incomingTelNum = incomingTelNum;
	}

	@Column(name="EFFECTIVE_SPEAK_POPULARIZE_NUM")
	public Integer getEffectiveSpeakPopularizeNum() {
		return this.effectiveSpeakPopularizeNum;
	}

	public void setEffectiveSpeakPopularizeNum(
			Integer effectiveSpeakPopularizeNum) {
		this.effectiveSpeakPopularizeNum = effectiveSpeakPopularizeNum;
	}

	@Column(name="OUTER_CALL_RESOURCE_NUM")
	public Integer getOuterCallResourceNum() {
		return this.outerCallResourceNum;
	}

	public void setOuterCallResourceNum(Integer outerCallResourceNum) {
		this.outerCallResourceNum = outerCallResourceNum;
	}

	@Column(name="ONLINE_RESOURCE_NUM")
	public Integer getOnlineResourceNum() {
		return this.onlineResourceNum;
	}

	public void setOnlineResourceNum(Integer onlineResourceNum) {
		this.onlineResourceNum = onlineResourceNum;
	}

	@Column(name="OTHER_RESOURSE_NUM")
	public Integer getOtherResourseNum() {
		return this.otherResourseNum;
	}

	public void setOtherResourseNum(Integer otherResourseNum) {
		this.otherResourseNum = otherResourseNum;
	}

	@Column(name="TOTAL_RESOURCE_NUM")
	public Integer getTotalResourceNum() {
		return this.totalResourceNum;
	}

	public void setTotalResourceNum(Integer totalResourceNum) {
		this.totalResourceNum = totalResourceNum;
	}

	@Column(name="VISIT_NUM")
	public Integer getVisitNum() {
		return this.visitNum;
	}

	public void setVisitNum(Integer visitNum) {
		this.visitNum = visitNum;
	}

	@Column(name="DIRECT_SIGN_RATE", precision = 10)
	public Double getDirectSignRate() {
		return this.directSignRate;
	}

	public void setDirectSignRate(Double directSignRate) {
		this.directSignRate = directSignRate;
	}

	@Column(name="INCOMING_TEL_INVITATION_RATE", precision = 10)
	public Double getIncomingTelInvitationRate() {
		return this.incomingTelInvitationRate;
	}

	public void setIncomingTelInvitationRate(Double incomingTelInvitationRate) {
		this.incomingTelInvitationRate = incomingTelInvitationRate;
	}

	@Column(name="TOTAL_VISIT_SIGN_RATE", precision = 10)
	public Double getTotalVisitSignRate() {
		return this.totalVisitSignRate;
	}

	public void setTotalVisitSignRate(Double totalVisitSignRate) {
		this.totalVisitSignRate = totalVisitSignRate;
	}

}