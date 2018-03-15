package com.eduboss.domainVo;

import java.math.BigDecimal;


public class StudnetAccMvVo {

	private String studentId;
	private BigDecimal totalAmount;//总金额
	private BigDecimal paidAmount;//已付总金额
	private BigDecimal realPaidAmount;// 除了电子账户的支付
	private BigDecimal electronicPaidAmount;//电子账户支付
	private BigDecimal planAmount;//代付金额
	private BigDecimal remainingAmount;//剩余总金额（总基本账户）
	private BigDecimal consumeAmount;//消费总金额
	private BigDecimal oneOnOnePaidAmount;//一对一已分配金额
	private BigDecimal oneOnOneConsumeAmount;//一对一消费金额
	private BigDecimal oneOnOneRemainingAmount;//一对一剩余金额
	private BigDecimal oneOnOneRemainingHour;//一对一剩余课时数
	private BigDecimal oneOnOneArrangedHour;//一对一可排课时
	private BigDecimal oneOnOneCompletedHour;//一对一已上课时 
	private BigDecimal oneOnOneFreePaidHour;//一对一赠送剩余课时
	private BigDecimal oneOnOneFreeCompletedHour;//一对一已上赠送课时
	private BigDecimal oneOnOnePromotinAmount;//一对一优惠金额
	private BigDecimal miniPaidAmount;//小班已分配金额
	private BigDecimal miniConsumeAmount;//小班消费金额
	private BigDecimal miniRemainingAmount;//小班剩余金额
	private BigDecimal miniRemainingHour;//小班剩余课时
	private BigDecimal otherPaidAmount;//其他已分配金额
	private BigDecimal otherConsumeAmount;//其他消费金额
	private BigDecimal otherRemainingAmount;//其他剩余金额
	private BigDecimal ecsRemainingAmount;//目标班剩余金额
	private BigDecimal promotinAmount;//赠送总金额
	
	private BigDecimal electronicAccount;//学生电子账户
	
	private BigDecimal availableAmount = BigDecimal.ZERO;
	private BigDecimal arrangedAmount = BigDecimal.ZERO;
	
	private BigDecimal oooRemainingPromotion = BigDecimal.ZERO;//一对一剩余金额
	private BigDecimal oooRemainingPromotionHour = BigDecimal.ZERO;//一对一剩余课时
	private BigDecimal miniRemainingPromotion = BigDecimal.ZERO;//小班剩余金额
	private BigDecimal miniRemainingPromotionHour = BigDecimal.ZERO;//小班剩余课时
	private BigDecimal lectureRemainingAmount = BigDecimal.ZERO;  //讲座剩余资金(元)
	private BigDecimal lectureRemainingHour = BigDecimal.ZERO;  //讲座剩余课时
	private BigDecimal ecsPromotinAmount = BigDecimal.ZERO;//目标班优惠金额
	
	private BigDecimal otmPaidAmount;//一对多已分配金额
	private BigDecimal otmConsumeAmount;//一对多消费金额
	private BigDecimal otmRemainingAmount;//一对多剩余金额
	private BigDecimal otmRemainingHour;//一对多剩余课时
	private BigDecimal otmRemainingPromotion;//一对多剩余优惠金额 
	private BigDecimal otmRemainingPromotionHour;//一对多剩余优惠课时
	
	private BigDecimal ecsPaidAmount;//目标班已分配金额
	private BigDecimal ecsConsumeAmount;//目标班消费金额
	
	private Integer isFrozen; // 0：冻结，1：不冻结
	private BigDecimal frozenAmount; //冻结的金额
	private BigDecimal twoTeacherRemainingAmount; //双师剩余资金
	private BigDecimal twoTeacherRemainingHour; //双师剩余课时
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public BigDecimal getRemainingAmount() {
		return this.remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}
	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	public BigDecimal getOneOnOnePaidAmount() {
		return oneOnOnePaidAmount;
	}
	public void setOneOnOnePaidAmount(BigDecimal oneOnOnePaidAmount) {
		this.oneOnOnePaidAmount = oneOnOnePaidAmount;
	}
	public BigDecimal getOneOnOneConsumeAmount() {
		return oneOnOneConsumeAmount;
	}
	public void setOneOnOneConsumeAmount(BigDecimal oneOnOneConsumeAmount) {
		this.oneOnOneConsumeAmount = oneOnOneConsumeAmount;
	}
	public BigDecimal getOneOnOneRemainingAmount() {
		return oneOnOneRemainingAmount;
	}
	public void setOneOnOneRemainingAmount(BigDecimal oneOnOneRemainingAmount) {
		this.oneOnOneRemainingAmount = oneOnOneRemainingAmount;
	}
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}
	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	public BigDecimal getOneOnOneArrangedHour() {
		return oneOnOneArrangedHour;
	}
	public void setOneOnOneArrangedHour(BigDecimal oneOnOneArrangedHour) {
		this.oneOnOneArrangedHour = oneOnOneArrangedHour;
	}
	public BigDecimal getOneOnOneCompletedHour() {
		return oneOnOneCompletedHour;
	}
	public void setOneOnOneCompletedHour(BigDecimal oneOnOneCompletedHour) {
		this.oneOnOneCompletedHour = oneOnOneCompletedHour;
	}
	
	public BigDecimal getOneOnOneFreePaidHour() {
		return oneOnOneFreePaidHour;
	}
	public void setOneOnOneFreePaidHour(BigDecimal oneOnOneFreePaidHour) {
		this.oneOnOneFreePaidHour = oneOnOneFreePaidHour;
	}
	public BigDecimal getOneOnOneFreeCompletedHour() {
		return oneOnOneFreeCompletedHour;
	}
	public void setOneOnOneFreeCompletedHour(BigDecimal oneOnOneFreeCompletedHour) {
		this.oneOnOneFreeCompletedHour = oneOnOneFreeCompletedHour;
	}
	public BigDecimal getMiniPaidAmount() {
		return miniPaidAmount;
	}
	public void setMiniPaidAmount(BigDecimal miniPaidAmount) {
		this.miniPaidAmount = miniPaidAmount;
	}
	public BigDecimal getMiniConsumeAmount() {
		return miniConsumeAmount;
	}
	public void setMiniConsumeAmount(BigDecimal miniConsumeAmount) {
		this.miniConsumeAmount = miniConsumeAmount;
	}
	public BigDecimal getMiniRemainingAmount() {
		return miniRemainingAmount;
	}
	public void setMiniRemainingAmount(BigDecimal miniRemainingAmount) {
		this.miniRemainingAmount = miniRemainingAmount;
	}
	public BigDecimal getOtherPaidAmount() {
		return otherPaidAmount;
	}
	public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
		this.otherPaidAmount = otherPaidAmount;
	}
	public BigDecimal getOtherConsumeAmount() {
		return otherConsumeAmount;
	}
	public void setOtherConsumeAmount(BigDecimal otherConsumeAmount) {
		this.otherConsumeAmount = otherConsumeAmount;
	}
	public BigDecimal getOtherRemainingAmount() {
		return otherRemainingAmount;
	}
	public void setOtherRemainingAmount(BigDecimal otherRemainingAmount) {
		this.otherRemainingAmount = otherRemainingAmount;
	}
	public BigDecimal getPlanAmount() {
		return planAmount;
	}
	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	public BigDecimal getEcsRemainingAmount() {
		return ecsRemainingAmount;
	}
	public void setEcsRemainingAmount(BigDecimal ecsRemainingAmount) {
		this.ecsRemainingAmount = ecsRemainingAmount;
	}
	public BigDecimal getPromotinAmount() {
		return promotinAmount;
	}
	public void setPromotinAmount(BigDecimal promotinAmount) {
		this.promotinAmount = promotinAmount;
	}
	public BigDecimal getOneOnOnePromotinAmount() {
		return oneOnOnePromotinAmount;
	}
	public void setOneOnOnePromotinAmount(BigDecimal oneOnOnePromotinAmount) {
		this.oneOnOnePromotinAmount = oneOnOnePromotinAmount;
	}
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	public BigDecimal getArrangedAmount() {
		return arrangedAmount;
	}
	public void setArrangedAmount(BigDecimal arrangedAmount) {
		this.arrangedAmount = arrangedAmount;
	}
	public BigDecimal getElectronicAccount() {
		return electronicAccount;
	}
	public void setElectronicAccount(BigDecimal electronicAccount) {
		this.electronicAccount = electronicAccount;
	}
	public BigDecimal getMiniRemainingHour() {
		return miniRemainingHour;
	}
	public void setMiniRemainingHour(BigDecimal miniRemainingHour) {
		this.miniRemainingHour = miniRemainingHour;
	}
	public BigDecimal getOooRemainingPromotion() {
		return oooRemainingPromotion;
	}
	public void setOooRemainingPromotion(BigDecimal oooRemainingPromotion) {
		this.oooRemainingPromotion = oooRemainingPromotion;
	}
	public BigDecimal getOooRemainingPromotionHour() {
		return oooRemainingPromotionHour;
	}
	public void setOooRemainingPromotionHour(BigDecimal oooRemainingPromotionHour) {
		this.oooRemainingPromotionHour = oooRemainingPromotionHour;
	}
	public BigDecimal getMiniRemainingPromotion() {
		return miniRemainingPromotion;
	}
	public void setMiniRemainingPromotion(BigDecimal miniRemainingPromotion) {
		this.miniRemainingPromotion = miniRemainingPromotion;
	}
	public BigDecimal getMiniRemainingPromotionHour() {
		return miniRemainingPromotionHour;
	}
	public void setMiniRemainingPromotionHour(BigDecimal miniRemainingPromotionHour) {
		this.miniRemainingPromotionHour = miniRemainingPromotionHour;
	}
	public BigDecimal getEcsPromotinAmount() {
		return ecsPromotinAmount;
	}
	public void setEcsPromotinAmount(BigDecimal ecsPromotinAmount) {
		this.ecsPromotinAmount = ecsPromotinAmount;
	}
	public BigDecimal getOtmPaidAmount() {
		return otmPaidAmount;
	}
	public void setOtmPaidAmount(BigDecimal otmPaidAmount) {
		this.otmPaidAmount = otmPaidAmount;
	}
	public BigDecimal getOtmConsumeAmount() {
		return otmConsumeAmount;
	}
	public void setOtmConsumeAmount(BigDecimal otmConsumeAmount) {
		this.otmConsumeAmount = otmConsumeAmount;
	}
	public BigDecimal getOtmRemainingAmount() {
		return otmRemainingAmount;
	}
	public void setOtmRemainingAmount(BigDecimal otmRemainingAmount) {
		this.otmRemainingAmount = otmRemainingAmount;
	}
	public BigDecimal getOtmRemainingHour() {
		return otmRemainingHour;
	}
	public void setOtmRemainingHour(BigDecimal otmRemainingHour) {
		this.otmRemainingHour = otmRemainingHour;
	}
	public BigDecimal getEcsPaidAmount() {
		return ecsPaidAmount;
	}
	public void setEcsPaidAmount(BigDecimal ecsPaidAmount) {
		this.ecsPaidAmount = ecsPaidAmount;
	}
	public BigDecimal getEcsConsumeAmount() {
		return ecsConsumeAmount;
	}
	public void setEcsConsumeAmount(BigDecimal ecsConsumeAmount) {
		this.ecsConsumeAmount = ecsConsumeAmount;
	}
	public BigDecimal getOtmRemainingPromotion() {
		return otmRemainingPromotion;
	}
	public void setOtmRemainingPromotion(BigDecimal otmRemainingPromotion) {
		this.otmRemainingPromotion = otmRemainingPromotion;
	}
	public BigDecimal getOtmRemainingPromotionHour() {
		return otmRemainingPromotionHour;
	}
	public void setOtmRemainingPromotionHour(BigDecimal otmRemainingPromotionHour) {
		this.otmRemainingPromotionHour = otmRemainingPromotionHour;
	}
	public Integer getIsFrozen() {
		return isFrozen;
	}
	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}
	public BigDecimal getFrozenAmount() {
		return frozenAmount;
	}
	public void setFrozenAmount(BigDecimal frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public BigDecimal getLectureRemainingAmount() {
		return lectureRemainingAmount;
	}

	public void setLectureRemainingAmount(BigDecimal lectureRemainingAmount) {
		this.lectureRemainingAmount = lectureRemainingAmount;
	}

	public BigDecimal getLectureRemainingHour() {
		return lectureRemainingHour;
	}

	public void setLectureRemainingHour(BigDecimal lectureRemainingHour) {
		this.lectureRemainingHour = lectureRemainingHour;
	}

	public BigDecimal getRealPaidAmount() {
		return realPaidAmount;
	}

	public void setRealPaidAmount(BigDecimal realPaidAmount) {
		this.realPaidAmount = realPaidAmount;
	}

	public BigDecimal getElectronicPaidAmount() {
		return electronicPaidAmount;
	}

	public void setElectronicPaidAmount(BigDecimal electronicPaidAmount) {
		this.electronicPaidAmount = electronicPaidAmount;
	}

	public BigDecimal getTwoTeacherRemainingAmount() {
		return twoTeacherRemainingAmount;
	}

	public void setTwoTeacherRemainingAmount(BigDecimal twoTeacherRemainingAmount) {
		this.twoTeacherRemainingAmount = twoTeacherRemainingAmount;
	}

	public BigDecimal getTwoTeacherRemainingHour() {
		return twoTeacherRemainingHour;
	}

	public void setTwoTeacherRemainingHour(BigDecimal twoTeacherRemainingHour) {
		this.twoTeacherRemainingHour = twoTeacherRemainingHour;
	}
}
