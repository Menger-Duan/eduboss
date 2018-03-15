package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INCOME_COUNT_CAMPUS")
public class IncomeCountCampus implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String countDate;
	private String campusId;
	private String campusName;
	private String createTime;
	private String modifyTime;
	private BigDecimal oneOnoneRealAmount;
	private BigDecimal oneOnonePromotionAmount;
	private BigDecimal oneOnOneQuantity;
	private BigDecimal smallClassrealAmount;
	private BigDecimal smallClassPromotionAmount;
	private BigDecimal smallClassQuantity;
	private BigDecimal escClassRealAmount;
	private BigDecimal escClassPromotionAmount;
	private BigDecimal othersRealAmount;
	private BigDecimal othersPromotionAmount;
	private BigDecimal otmRealAmount;
	private BigDecimal otmPromotionAmount;
	private BigDecimal otmQuantity;
	private BigDecimal lectureRealAmount;
	private BigDecimal lecturePromotionAmount;
	private BigDecimal isNormalRealAmount;
	private BigDecimal isNormalPromotionAmount;
	private BigDecimal realTotalAmount;
	private BigDecimal totalAmount;

	private BigDecimal twoTeacherRealAmount;
	private BigDecimal twoTeacherPromotionAmount;
	private BigDecimal twoTeacherQuantity;
	private BigDecimal liveRealAmount;
	private BigDecimal livePromotionAmount;
	private BigDecimal liveQuantity;

	public IncomeCountCampus() {
		oneOnoneRealAmount=BigDecimal.ZERO;
		oneOnonePromotionAmount=BigDecimal.ZERO;
		oneOnOneQuantity=BigDecimal.ZERO;
		smallClassrealAmount=BigDecimal.ZERO;
		smallClassPromotionAmount=BigDecimal.ZERO;
		smallClassQuantity=BigDecimal.ZERO;
		escClassRealAmount=BigDecimal.ZERO;
		escClassPromotionAmount=BigDecimal.ZERO;
		othersRealAmount=BigDecimal.ZERO;
		othersPromotionAmount=BigDecimal.ZERO;
		otmRealAmount=BigDecimal.ZERO;
		otmPromotionAmount=BigDecimal.ZERO;
		otmQuantity=BigDecimal.ZERO;
		lectureRealAmount=BigDecimal.ZERO;
		lecturePromotionAmount=BigDecimal.ZERO;
		isNormalRealAmount=BigDecimal.ZERO;
		isNormalPromotionAmount=BigDecimal.ZERO;
		realTotalAmount=BigDecimal.ZERO;
		totalAmount=BigDecimal.ZERO;

		twoTeacherRealAmount=BigDecimal.ZERO;
		twoTeacherPromotionAmount=BigDecimal.ZERO;
		twoTeacherQuantity=BigDecimal.ZERO;
		liveRealAmount=BigDecimal.ZERO;
		livePromotionAmount=BigDecimal.ZERO;
		liveQuantity=BigDecimal.ZERO;
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

	@Column(name = "CAMPUS_ID", length = 32)
	public String getCampusId() {
		return campusId;
	}


	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	@Column(name = "CAMPUS_NAME", length = 100)
	public String getCampusName() {
		return campusName;
	}


	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	@Column(name = "oneOnoneRealAmount")
	public BigDecimal getOneOnoneRealAmount() {
		return oneOnoneRealAmount;
	}

	public void setOneOnoneRealAmount(BigDecimal oneOnoneRealAmount) {
		this.oneOnoneRealAmount = oneOnoneRealAmount;
	}

	@Column(name = "oneOnonePromotionAmount")
	public BigDecimal getOneOnonePromotionAmount() {
		return oneOnonePromotionAmount;
	}


	public void setOneOnonePromotionAmount(BigDecimal oneOnonePromotionAmount) {
		this.oneOnonePromotionAmount = oneOnonePromotionAmount;
	}

	@Column(name = "oneOnOneQuantity")
	public BigDecimal getOneOnOneQuantity() {
		return oneOnOneQuantity;
	}


	public void setOneOnOneQuantity(BigDecimal oneOnOneQuantity) {
		this.oneOnOneQuantity = oneOnOneQuantity;
	}

	@Column(name = "smallClassrealAmount")
	public BigDecimal getSmallClassrealAmount() {
		return smallClassrealAmount;
	}


	public void setSmallClassrealAmount(BigDecimal smallClassrealAmount) {
		this.smallClassrealAmount = smallClassrealAmount;
	}

	@Column(name = "smallClassPromotionAmount")
	public BigDecimal getSmallClassPromotionAmount() {
		return smallClassPromotionAmount;
	}


	public void setSmallClassPromotionAmount(BigDecimal smallClassPromotionAmount) {
		this.smallClassPromotionAmount = smallClassPromotionAmount;
	}

	@Column(name = "smallClassQuantity")
	public BigDecimal getSmallClassQuantity() {
		return smallClassQuantity;
	}


	public void setSmallClassQuantity(BigDecimal smallClassQuantity) {
		this.smallClassQuantity = smallClassQuantity;
	}

	@Column(name = "escClassRealAmount")
	public BigDecimal getEscClassRealAmount() {
		return escClassRealAmount;
	}


	public void setEscClassRealAmount(BigDecimal escClassRealAmount) {
		this.escClassRealAmount = escClassRealAmount;
	}

	@Column(name = "escClassPromotionAmount")
	public BigDecimal getEscClassPromotionAmount() {
		return escClassPromotionAmount;
	}


	public void setEscClassPromotionAmount(BigDecimal escClassPromotionAmount) {
		this.escClassPromotionAmount = escClassPromotionAmount;
	}

	@Column(name = "othersRealAmount")
	public BigDecimal getOthersRealAmount() {
		return othersRealAmount;
	}


	public void setOthersRealAmount(BigDecimal othersRealAmount) {
		this.othersRealAmount = othersRealAmount;
	}

	@Column(name = "othersPromotionAmount")
	public BigDecimal getOthersPromotionAmount() {
		return othersPromotionAmount;
	}


	public void setOthersPromotionAmount(BigDecimal othersPromotionAmount) {
		this.othersPromotionAmount = othersPromotionAmount;
	}

	@Column(name = "otmRealAmount")
	public BigDecimal getOtmRealAmount() {
		return otmRealAmount;
	}


	public void setOtmRealAmount(BigDecimal otmRealAmount) {
		this.otmRealAmount = otmRealAmount;
	}

	@Column(name = "otmPromotionAmount")
	public BigDecimal getOtmPromotionAmount() {
		return otmPromotionAmount;
	}


	public void setOtmPromotionAmount(BigDecimal otmPromotionAmount) {
		this.otmPromotionAmount = otmPromotionAmount;
	}

	@Column(name = "otmQuantity")
	public BigDecimal getOtmQuantity() {
		return otmQuantity;
	}


	public void setOtmQuantity(BigDecimal otmQuantity) {
		this.otmQuantity = otmQuantity;
	}

	@Column(name = "lectureRealAmount")
	public BigDecimal getLectureRealAmount() {
		return lectureRealAmount;
	}


	public void setLectureRealAmount(BigDecimal lectureRealAmount) {
		this.lectureRealAmount = lectureRealAmount;
	}

	@Column(name = "lecturePromotionAmount")
	public BigDecimal getLecturePromotionAmount() {
		return lecturePromotionAmount;
	}


	public void setLecturePromotionAmount(BigDecimal lecturePromotionAmount) {
		this.lecturePromotionAmount = lecturePromotionAmount;
	}

	@Column(name = "isNormalRealAmount")
	public BigDecimal getIsNormalRealAmount() {
		return isNormalRealAmount;
	}


	public void setIsNormalRealAmount(BigDecimal isNormalRealAmount) {
		this.isNormalRealAmount = isNormalRealAmount;
	}

	@Column(name = "isNormalPromotionAmount")
	public BigDecimal getIsNormalPromotionAmount() {
		return isNormalPromotionAmount;
	}


	public void setIsNormalPromotionAmount(BigDecimal isNormalPromotionAmount) {
		this.isNormalPromotionAmount = isNormalPromotionAmount;
	}

	@Column(name = "realTotalAmount")
	public BigDecimal getRealTotalAmount() {
		return realTotalAmount;
	}


	public void setRealTotalAmount(BigDecimal realTotalAmount) {
		this.realTotalAmount = realTotalAmount;
	}

	@Column(name = "totalAmount")
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "twoTeacherRealAmount")
	public BigDecimal getTwoTeacherRealAmount() {
		return twoTeacherRealAmount;
	}

	public void setTwoTeacherRealAmount(BigDecimal twoTeacherRealAmount) {
		this.twoTeacherRealAmount = twoTeacherRealAmount;
	}
	@Column(name = "twoTeacherPromotionAmount")
	public BigDecimal getTwoTeacherPromotionAmount() {
		return twoTeacherPromotionAmount;
	}

	public void setTwoTeacherPromotionAmount(BigDecimal twoTeacherPromotionAmount) {
		this.twoTeacherPromotionAmount = twoTeacherPromotionAmount;
	}
	@Column(name = "twoTeacherQuantity")
	public BigDecimal getTwoTeacherQuantity() {
		return twoTeacherQuantity;
	}

	public void setTwoTeacherQuantity(BigDecimal twoTeacherQuantity) {
		this.twoTeacherQuantity = twoTeacherQuantity;
	}
	@Column(name = "liveRealAmount")
	public BigDecimal getLiveRealAmount() {
		return liveRealAmount;
	}

	public void setLiveRealAmount(BigDecimal liveRealAmount) {
		this.liveRealAmount = liveRealAmount;
	}
	@Column(name = "livePromotionAmount")
	public BigDecimal getLivePromotionAmount() {
		return livePromotionAmount;
	}

	public void setLivePromotionAmount(BigDecimal livePromotionAmount) {
		this.livePromotionAmount = livePromotionAmount;
	}
	@Column(name = "liveQuantity")
	public BigDecimal getLiveQuantity() {
		return liveQuantity;
	}

	public void setLiveQuantity(BigDecimal liveQuantity) {
		this.liveQuantity = liveQuantity;
	}
}
