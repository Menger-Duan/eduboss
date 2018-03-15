package com.eduboss.domainVo;

import java.math.BigDecimal;



/**
 * 营运部月结表（学管组）统计报表所用各项指标 (学员、班级构成与服务) 用于写入报表
 * @author qinjingkai
 *
 */
public class OperatStuClsCompInxVo {
	
	/**
	 * 新签学生
	 */
	private Integer newSignStuNum;
	
	/**
	 * 续费学生
	 */
	
	private Integer renewSignStuNum;
	
	/**
	 * 退费学生
	 */
	private Integer refundStuNum;
	/**
	 * 停课学生
	 */
	private Integer suspendStuNum;
	
	/**
	 * 结课学生
	 */
	private Integer endClsStuNum;
	
	/**
	 * 总在读学生
	 */
	private Integer inSchoolStuNum;
	/**
	 * 停课学生比例
	 */
	private BigDecimal suspendStuRat;
	/**
	 * 续生率	
	 */
	private BigDecimal  renewStuRat;
	
	/**
	 * 月总转介绍人数
	 */
	private Integer transIntrodMonthTotal;
	/**
	 * 低于20H学生数
	 */
	private Integer  lowerTwetHStuNum;
	/**
	 * 低于10H学生数
	 */
	private Integer lowerTenHStuNum;
	/**
	 * 续读科次
	 */
	private Integer renewSubNum;
	
	/**
	 * 新签科次
	 */
	private Integer  newSignSubNum;
	/**
	 * 退费科次	

	 */
	private Integer refundSubNum;
	/**
	 * 目标科次	
	 */
	private Integer targSubNum;
	/**
	 * 科次达成率	
	 */
	private  BigDecimal subReachRat;
	/**
	 * 人均科次	
	 */
	private BigDecimal avgSubNum;
	/**
	 * 总开班数	
	 */
	private  Integer openClsTotal;
	/**
	 * 班均人数
	 */
	private BigDecimal clsAvgNum;
	/**
	 * 	新生数量	
	 */
	private  Integer newStuNum;
	/**
	 * 旧生数量
	 */
	private Integer oldStuNum;
	public Integer getNewSignStuNum() {
		return newSignStuNum;
	}
	public void setNewSignStuNum(Integer newSignStuNum) {
		this.newSignStuNum = newSignStuNum;
	}
	public Integer getRenewSignStuNum() {
		return renewSignStuNum;
	}
	public void setRenewSignStuNum(Integer renewSignStuNum) {
		this.renewSignStuNum = renewSignStuNum;
	}
	public Integer getRefundStuNum() {
		return refundStuNum;
	}
	public void setRefundStuNum(Integer refundStuNum) {
		this.refundStuNum = refundStuNum;
	}
	public Integer getSuspendStuNum() {
		return suspendStuNum;
	}
	public void setSuspendStuNum(Integer suspendStuNum) {
		this.suspendStuNum = suspendStuNum;
	}
	public Integer getEndClsStuNum() {
		return endClsStuNum;
	}
	public void setEndClsStuNum(Integer endClsStuNum) {
		this.endClsStuNum = endClsStuNum;
	}
	public Integer getInSchoolStuNum() {
		return inSchoolStuNum;
	}
	public void setInSchoolStuNum(Integer inSchoolStuNum) {
		this.inSchoolStuNum = inSchoolStuNum;
	}
	public BigDecimal getSuspendStuRat() {
		return suspendStuRat;
	}
	public void setSuspendStuRat(BigDecimal suspendStuRat) {
		this.suspendStuRat = suspendStuRat;
	}
	public BigDecimal getRenewStuRat() {
		return renewStuRat;
	}
	public void setRenewStuRat(BigDecimal renewStuRat) {
		this.renewStuRat = renewStuRat;
	}
	public Integer getTransIntrodMonthTotal() {
		return transIntrodMonthTotal;
	}
	public void setTransIntrodMonthTotal(Integer transIntrodMonthTotal) {
		this.transIntrodMonthTotal = transIntrodMonthTotal;
	}
	public Integer getLowerTwetHStuNum() {
		return lowerTwetHStuNum;
	}
	public void setLowerTwetHStuNum(Integer lowerTwetHStuNum) {
		this.lowerTwetHStuNum = lowerTwetHStuNum;
	}
	public Integer getLowerTenHStuNum() {
		return lowerTenHStuNum;
	}
	public void setLowerTenHStuNum(Integer lowerTenHStuNum) {
		this.lowerTenHStuNum = lowerTenHStuNum;
	}
	public Integer getRenewSubNum() {
		return renewSubNum;
	}
	public void setRenewSubNum(Integer renewSubNum) {
		this.renewSubNum = renewSubNum;
	}
	public Integer getNewSignSubNum() {
		return newSignSubNum;
	}
	public void setNewSignSubNum(Integer newSignSubNum) {
		this.newSignSubNum = newSignSubNum;
	}
	public Integer getRefundSubNum() {
		return refundSubNum;
	}
	public void setRefundSubNum(Integer refundSubNum) {
		this.refundSubNum = refundSubNum;
	}
	public Integer getTargSubNum() {
		return targSubNum;
	}
	public void setTargSubNum(Integer targSubNum) {
		this.targSubNum = targSubNum;
	}
	public BigDecimal getSubReachRat() {
		return subReachRat;
	}
	public void setSubReachRat(BigDecimal subReachRat) {
		this.subReachRat = subReachRat;
	}
	public BigDecimal getAvgSubNum() {
		return avgSubNum;
	}
	public void setAvgSubNum(BigDecimal avgSubNum) {
		this.avgSubNum = avgSubNum;
	}
	public Integer getOpenClsTotal() {
		return openClsTotal;
	}
	public void setOpenClsTotal(Integer openClsTotal) {
		this.openClsTotal = openClsTotal;
	}
	public BigDecimal getClsAvgNum() {
		return clsAvgNum;
	}
	public void setClsAvgNum(BigDecimal clsAvgNum) {
		this.clsAvgNum = clsAvgNum;
	}
	public Integer getNewStuNum() {
		return newStuNum;
	}
	public void setNewStuNum(Integer newStuNum) {
		this.newStuNum = newStuNum;
	}
	public Integer getOldStuNum() {
		return oldStuNum;
	}
	public void setOldStuNum(Integer oldStuNum) {
		this.oldStuNum = oldStuNum;
	}
	
	
	
	
}
