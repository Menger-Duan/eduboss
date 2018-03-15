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
@Table(name = "ods_month_income_campus")
public class OdsMonthIncomeCampus {

	private String id;
	private String groupId;
	private String brenchId;
	private String campusId;
	private BigDecimal oneOnOneRealAmount;
	private BigDecimal oneOnOnePromotionAmount;
	private BigDecimal oneOnOneRealWashAmount;
	private BigDecimal oneOnOnePromotionWashAmount;
	private BigDecimal smallClassRealAmount;
	private BigDecimal smallClassPromotionAmount;
	private BigDecimal smallClassRealWashAmount;
	private BigDecimal smallClassPromotionWashAmount;
	private BigDecimal twoTeacherRealAmount;
    private BigDecimal twoTeacherPromotionAmount;
    private BigDecimal twoTeacherRealWashAmount;
    private BigDecimal twoTeacherPromotionWashAmount;
    private BigDecimal liveRealAmount;
    private BigDecimal livePromotionAmount;
    private BigDecimal liveRealWashAmount;
    private BigDecimal livePromotionWashAmount;
	private BigDecimal ecsClassRealAmount;
	private BigDecimal ecsClassPromotionAmount;
	private BigDecimal ecsClassRealWashAmount;
	private BigDecimal ecsClassPromotionWashAmount;
	private BigDecimal otmClassRealAmount;
	private BigDecimal otmClassPromotionAmount;
	private BigDecimal otmClassRealWashAmount;
	private BigDecimal otmClassPromotionWashAmount;
	private BigDecimal otherRealAmount;
	private BigDecimal otherPromotionAmount;
	private BigDecimal otherRealWashAmount;
	private BigDecimal otherPromotionWashAmount;
	private BigDecimal lectureRealAmount;
	private BigDecimal lecturePromotionAmount;
	private BigDecimal lectureRealWashAmount;
	private BigDecimal lecturePromotionWashAmount;
	private BigDecimal isNormalRealAmount;
	private BigDecimal isNormalPromotionAmount;
	private BigDecimal isNormalHistoryWashAmount;
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
	
	public OdsMonthIncomeCampus() {
		super();
	}

	public OdsMonthIncomeCampus(String id, String groupId, String brenchId,
            String campusId, BigDecimal oneOnOneRealAmount,
            BigDecimal oneOnOnePromotionAmount,
            BigDecimal oneOnOneRealWashAmount,
            BigDecimal oneOnOnePromotionWashAmount,
            BigDecimal smallClassRealAmount,
            BigDecimal smallClassPromotionAmount,
            BigDecimal smallClassRealWashAmount,
            BigDecimal smallClassPromotionWashAmount,
            BigDecimal twoTeacherRealAmount,
            BigDecimal twoTeacherPromotionAmount,
            BigDecimal twoTeacherRealWashAmount,
            BigDecimal twoTeacherPromotionWashAmount,
            BigDecimal liveRealAmount, BigDecimal livePromotionAmount,
            BigDecimal liveRealWashAmount, BigDecimal livePromotionWashAmount,
            BigDecimal ecsClassRealAmount, BigDecimal ecsClassPromotionAmount,
            BigDecimal ecsClassRealWashAmount,
            BigDecimal ecsClassPromotionWashAmount,
            BigDecimal otmClassRealAmount, BigDecimal otmClassPromotionAmount,
            BigDecimal otmClassRealWashAmount,
            BigDecimal otmClassPromotionWashAmount, BigDecimal otherRealAmount,
            BigDecimal otherPromotionAmount, BigDecimal otherRealWashAmount,
            BigDecimal otherPromotionWashAmount, BigDecimal lectureRealAmount,
            BigDecimal lecturePromotionAmount,
            BigDecimal lectureRealWashAmount,
            BigDecimal lecturePromotionWashAmount,
            BigDecimal isNormalRealAmount, BigDecimal isNormalPromotionAmount,
            BigDecimal isNormalHistoryWashAmount, String countDate,
            String mappingDate, EvidenceAuditStatus evidenceAuditStatus,
            User currentAuditUser, String currentAuditTime,
            User campusConfirmUser, String campusConfirmTime,
            User financeFirstAuditUser, String financeFirstAuditTime,
            User brenchConfirmUser, String brenchConfirmTime,
            User financeEndAuditUser, String financeEndAuditTime) {
        super();
        this.id = id;
        this.groupId = groupId;
        this.brenchId = brenchId;
        this.campusId = campusId;
        this.oneOnOneRealAmount = oneOnOneRealAmount;
        this.oneOnOnePromotionAmount = oneOnOnePromotionAmount;
        this.oneOnOneRealWashAmount = oneOnOneRealWashAmount;
        this.oneOnOnePromotionWashAmount = oneOnOnePromotionWashAmount;
        this.smallClassRealAmount = smallClassRealAmount;
        this.smallClassPromotionAmount = smallClassPromotionAmount;
        this.smallClassRealWashAmount = smallClassRealWashAmount;
        this.smallClassPromotionWashAmount = smallClassPromotionWashAmount;
        this.twoTeacherRealAmount = twoTeacherRealAmount;
        this.twoTeacherPromotionAmount = twoTeacherPromotionAmount;
        this.twoTeacherRealWashAmount = twoTeacherRealWashAmount;
        this.twoTeacherPromotionWashAmount = twoTeacherPromotionWashAmount;
        this.liveRealAmount = liveRealAmount;
        this.livePromotionAmount = livePromotionAmount;
        this.liveRealWashAmount = liveRealWashAmount;
        this.livePromotionWashAmount = livePromotionWashAmount;
        this.ecsClassRealAmount = ecsClassRealAmount;
        this.ecsClassPromotionAmount = ecsClassPromotionAmount;
        this.ecsClassRealWashAmount = ecsClassRealWashAmount;
        this.ecsClassPromotionWashAmount = ecsClassPromotionWashAmount;
        this.otmClassRealAmount = otmClassRealAmount;
        this.otmClassPromotionAmount = otmClassPromotionAmount;
        this.otmClassRealWashAmount = otmClassRealWashAmount;
        this.otmClassPromotionWashAmount = otmClassPromotionWashAmount;
        this.otherRealAmount = otherRealAmount;
        this.otherPromotionAmount = otherPromotionAmount;
        this.otherRealWashAmount = otherRealWashAmount;
        this.otherPromotionWashAmount = otherPromotionWashAmount;
        this.lectureRealAmount = lectureRealAmount;
        this.lecturePromotionAmount = lecturePromotionAmount;
        this.lectureRealWashAmount = lectureRealWashAmount;
        this.lecturePromotionWashAmount = lecturePromotionWashAmount;
        this.isNormalRealAmount = isNormalRealAmount;
        this.isNormalPromotionAmount = isNormalPromotionAmount;
        this.isNormalHistoryWashAmount = isNormalHistoryWashAmount;
        this.countDate = countDate;
        this.mappingDate = mappingDate;
        this.evidenceAuditStatus = evidenceAuditStatus;
        this.currentAuditUser = currentAuditUser;
        this.currentAuditTime = currentAuditTime;
        this.campusConfirmUser = campusConfirmUser;
        this.campusConfirmTime = campusConfirmTime;
        this.financeFirstAuditUser = financeFirstAuditUser;
        this.financeFirstAuditTime = financeFirstAuditTime;
        this.brenchConfirmUser = brenchConfirmUser;
        this.brenchConfirmTime = brenchConfirmTime;
        this.financeEndAuditUser = financeEndAuditUser;
        this.financeEndAuditTime = financeEndAuditTime;
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

	@Column(name = "ONE_ON_ONE_REAL_AMOUNT", precision = 10)
	public BigDecimal getOneOnOneRealAmount() {
		return oneOnOneRealAmount;
	}

	public void setOneOnOneRealAmount(BigDecimal oneOnOneRealAmount) {
		this.oneOnOneRealAmount = oneOnOneRealAmount;
	}

	@Column(name = "ONE_ON_ONE_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getOneOnOnePromotionAmount() {
		return oneOnOnePromotionAmount;
	}

	public void setOneOnOnePromotionAmount(BigDecimal oneOnOnePromotionAmount) {
		this.oneOnOnePromotionAmount = oneOnOnePromotionAmount;
	}

	@Column(name = "ONE_ON_ONE_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getOneOnOneRealWashAmount() {
		return oneOnOneRealWashAmount;
	}
	
	public void setOneOnOneRealWashAmount(BigDecimal oneOnOneRealWashAmount) {
		this.oneOnOneRealWashAmount = oneOnOneRealWashAmount;
	}
	
	@Column(name = "ONE_ON_ONE_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getOneOnOnePromotionWashAmount() {
		return oneOnOnePromotionWashAmount;
	}
	
	public void setOneOnOnePromotionWashAmount(
			BigDecimal oneOnOnePromotionWashAmount) {
		this.oneOnOnePromotionWashAmount = oneOnOnePromotionWashAmount;
	}

	@Column(name = "SMALL_CLASS_REAL_AMOUNT", precision = 10)
	public BigDecimal getSmallClassRealAmount() {
		return smallClassRealAmount;
	}

	public void setSmallClassRealAmount(BigDecimal smallClassRealAmount) {
		this.smallClassRealAmount = smallClassRealAmount;
	}

	@Column(name = "SMALL_CLASS_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getSmallClassPromotionAmount() {
		return smallClassPromotionAmount;
	}

	public void setSmallClassPromotionAmount(BigDecimal smallClassPromotionAmount) {
		this.smallClassPromotionAmount = smallClassPromotionAmount;
	}

	@Column(name = "SMALL_CLASS_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getSmallClassRealWashAmount() {
		return smallClassRealWashAmount;
	}

	public void setSmallClassRealWashAmount(BigDecimal smallClassRealWashAmount) {
		this.smallClassRealWashAmount = smallClassRealWashAmount;
	}

	@Column(name = "SMALL_CLASS_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getSmallClassPromotionWashAmount() {
		return smallClassPromotionWashAmount;
	}

	public void setSmallClassPromotionWashAmount(
			BigDecimal smallClassPromotionWashAmount) {
		this.smallClassPromotionWashAmount = smallClassPromotionWashAmount;
	}
	
	@Column(name = "TWO_TEACHER_REAL_AMOUNT", precision = 10)
	public BigDecimal getTwoTeacherRealAmount() {
        return twoTeacherRealAmount;
    }

    public void setTwoTeacherRealAmount(BigDecimal twoTeacherRealAmount) {
        this.twoTeacherRealAmount = twoTeacherRealAmount;
    }

    @Column(name = "TWO_TEACHER_PROMOTION_AMOUNT", precision = 10)
    public BigDecimal getTwoTeacherPromotionAmount() {
        return twoTeacherPromotionAmount;
    }

    public void setTwoTeacherPromotionAmount(BigDecimal twoTeacherPromotionAmount) {
        this.twoTeacherPromotionAmount = twoTeacherPromotionAmount;
    }

    @Column(name = "TWO_TEACHER_REAL_WASH_AMOUNT", precision = 10)
    public BigDecimal getTwoTeacherRealWashAmount() {
        return twoTeacherRealWashAmount;
    }

    public void setTwoTeacherRealWashAmount(BigDecimal twoTeacherRealWashAmount) {
        this.twoTeacherRealWashAmount = twoTeacherRealWashAmount;
    }

    @Column(name = "TWO_TEACHER_PROMOTION_WASH_AMOUNT", precision = 10)
    public BigDecimal getTwoTeacherPromotionWashAmount() {
        return twoTeacherPromotionWashAmount;
    }

    public void setTwoTeacherPromotionWashAmount(
            BigDecimal twoTeacherPromotionWashAmount) {
        this.twoTeacherPromotionWashAmount = twoTeacherPromotionWashAmount;
    }

    @Column(name = "LIVE_REAL_AMOUNT", precision = 10)
    public BigDecimal getLiveRealAmount() {
        return liveRealAmount;
    }

    public void setLiveRealAmount(BigDecimal liveRealAmount) {
        this.liveRealAmount = liveRealAmount;
    }

    @Column(name = "LIVE_PROMOTION_AMOUNT", precision = 10)
    public BigDecimal getLivePromotionAmount() {
        return livePromotionAmount;
    }

    public void setLivePromotionAmount(BigDecimal livePromotionAmount) {
        this.livePromotionAmount = livePromotionAmount;
    }

    @Column(name = "LIVE_REAL_WASH_AMOUNT", precision = 10)
    public BigDecimal getLiveRealWashAmount() {
        return liveRealWashAmount;
    }

    public void setLiveRealWashAmount(BigDecimal liveRealWashAmount) {
        this.liveRealWashAmount = liveRealWashAmount;
    }

    @Column(name = "LIVE_PROMOTION_WASH_AMOUNT", precision = 10)
    public BigDecimal getLivePromotionWashAmount() {
        return livePromotionWashAmount;
    }

    public void setLivePromotionWashAmount(BigDecimal livePromotionWashAmount) {
        this.livePromotionWashAmount = livePromotionWashAmount;
    }

    @Column(name = "ECS_CLASS_REAL_AMOUNT", precision = 10)
	public BigDecimal getEcsClassRealAmount() {
		return ecsClassRealAmount;
	}

	public void setEcsClassRealAmount(BigDecimal ecsClassRealAmount) {
		this.ecsClassRealAmount = ecsClassRealAmount;
	}

	@Column(name = "ECS_CLASS_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getEcsClassPromotionAmount() {
		return ecsClassPromotionAmount;
	}

	public void setEcsClassPromotionAmount(BigDecimal ecsClassPromotionAmount) {
		this.ecsClassPromotionAmount = ecsClassPromotionAmount;
	}

	@Column(name = "ECS_CLASS_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getEcsClassRealWashAmount() {
		return ecsClassRealWashAmount;
	}
	
	public void setEcsClassRealWashAmount(BigDecimal ecsClassRealWashAmount) {
		this.ecsClassRealWashAmount = ecsClassRealWashAmount;
	}
	
	@Column(name = "ECS_CLASS_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getEcsClassPromotionWashAmount() {
		return ecsClassPromotionWashAmount;
	}
	
	public void setEcsClassPromotionWashAmount(
			BigDecimal ecsClassPromotionWashAmount) {
		this.ecsClassPromotionWashAmount = ecsClassPromotionWashAmount;
	}

	@Column(name = "OTM_CLASS_REAL_AMOUNT", precision = 10)
	public BigDecimal getOtmClassRealAmount() {
		return otmClassRealAmount;
	}

	public void setOtmClassRealAmount(BigDecimal otmClassRealAmount) {
		this.otmClassRealAmount = otmClassRealAmount;
	}

	@Column(name = "OTM_CLASS_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getOtmClassPromotionAmount() {
		return otmClassPromotionAmount;
	}

	public void setOtmClassPromotionAmount(BigDecimal otmClassPromotionAmount) {
		this.otmClassPromotionAmount = otmClassPromotionAmount;
	}

	@Column(name = "OTM_CLASS_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getOtmClassRealWashAmount() {
		return otmClassRealWashAmount;
	}
	
	public void setOtmClassRealWashAmount(BigDecimal otmClassRealWashAmount) {
		this.otmClassRealWashAmount = otmClassRealWashAmount;
	}
	
	@Column(name = "OTM_CLASS_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getOtmClassPromotionWashAmount() {
		return otmClassPromotionWashAmount;
	}
	
	public void setOtmClassPromotionWashAmount(
			BigDecimal otmClassPromotionWashAmount) {
		this.otmClassPromotionWashAmount = otmClassPromotionWashAmount;
	}

	@Column(name = "OTHERS_REAL_AMOUNT", precision = 10)
	public BigDecimal getOtherRealAmount() {
		return otherRealAmount;
	}

	public void setOtherRealAmount(BigDecimal otherRealAmount) {
		this.otherRealAmount = otherRealAmount;
	}

	@Column(name = "OTHERS_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getOtherPromotionAmount() {
		return otherPromotionAmount;
	}

	public void setOtherPromotionAmount(BigDecimal otherPromotionAmount) {
		this.otherPromotionAmount = otherPromotionAmount;
	}

	@Column(name = "OTHERS_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getOtherRealWashAmount() {
		return otherRealWashAmount;
	}
	
	public void setOtherRealWashAmount(BigDecimal otherRealWashAmount) {
		this.otherRealWashAmount = otherRealWashAmount;
	}
	
	@Column(name = "OTHERS_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getOtherPromotionWashAmount() {
		return otherPromotionWashAmount;
	}
	
	public void setOtherPromotionWashAmount(BigDecimal otherPromotionWashAmount) {
		this.otherPromotionWashAmount = otherPromotionWashAmount;
	}

	@Column(name = "LECTURE_REAL_AMOUNT", precision = 10)
	public BigDecimal getLectureRealAmount() {
		return lectureRealAmount;
	}

	public void setLectureRealAmount(BigDecimal lectureRealAmount) {
		this.lectureRealAmount = lectureRealAmount;
	}

	@Column(name = "LECTURE_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getLecturePromotionAmount() {
		return lecturePromotionAmount;
	}

	public void setLecturePromotionAmount(BigDecimal lecturePromotionAmount) {
		this.lecturePromotionAmount = lecturePromotionAmount;
	}

	@Column(name = "LECTURE_REAL_WASH_AMOUNT", precision = 10)
	public BigDecimal getLectureRealWashAmount() {
		return lectureRealWashAmount;
	}
	
	public void setLectureRealWashAmount(BigDecimal lectureRealWashAmount) {
		this.lectureRealWashAmount = lectureRealWashAmount;
	}
	
	@Column(name = "LECTURE_PROMOTION_WASH_AMOUNT", precision = 10)
	public BigDecimal getLecturePromotionWashAmount() {
		return lecturePromotionWashAmount;
	}
	
	public void setLecturePromotionWashAmount(BigDecimal lecturePromotionWashAmount) {
		this.lecturePromotionWashAmount = lecturePromotionWashAmount;
	}

	@Column(name = "IS_NORMAL_REAL_AMOUNT", precision = 10)
	public BigDecimal getIsNormalRealAmount() {
		return isNormalRealAmount;
	}

	public void setIsNormalRealAmount(BigDecimal isNormalRealAmount) {
		this.isNormalRealAmount = isNormalRealAmount;
	}

	@Column(name = "IS_NORMAL_PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getIsNormalPromotionAmount() {
		return isNormalPromotionAmount;
	}

	public void setIsNormalPromotionAmount(BigDecimal isNormalPromotionAmount) {
		this.isNormalPromotionAmount = isNormalPromotionAmount;
	}

	@Column(name = "IS_NORMAL_HISTORY_WASH_AMOUNT", precision = 10)
	public BigDecimal getIsNormalHistoryWashAmount() {
		return isNormalHistoryWashAmount;
	}

	public void setIsNormalHistoryWashAmount(BigDecimal isNormalHistoryWashAmount) {
		this.isNormalHistoryWashAmount = isNormalHistoryWashAmount;
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
