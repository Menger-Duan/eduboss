package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 营运部月结表（学管组）统计报表所用各项指标 (-业绩、课时) 用于写入报表
 * 
 * @author qinjingkai
 *
 */
public class OperatBonusAndClsHrInxVo {

	/**
	 * 学生姓名
	 */
	private String name;

	/**
	 * 实签预收额(一对一)
	 */
	private BigDecimal oooRealSignPreFundAmt;

	/**
	 * 签单人数(一对一)
	 */
	private Integer oooSignNum;

	/**
	 * 签单科次(一对一)
	 */
	private Integer oooSignSubNum;

	/**
	 * 实签课时 (一对一)
	 */
	private Integer oooRealSignHr;

	/**
	 * 赠送课时 ( 一对一)
	 */
	private BigDecimal oooFreeSignHr;

	/**
	 * 均单( 一对一)
	 */
	private BigDecimal oooAvgSign;

	/**
	 * 赠送比例( 一对一)
	 */
	private String oooFreeRat;

	/**
	 * 人均科次( 一对一)
	 */
	private BigDecimal oooAvgSubNum;

	/**
	 * 实签预收额(小班)
	 */
	private BigDecimal smclsRealSignPreFundAmt;

	/**
	 * 签单人数(小班)
	 */
	private Integer smclsSignNum;

	/**
	 * 签单科次(小班)
	 */
	private Integer smclsSignSubNum;

	/**
	 * 均单(小班)
	 */
	private BigDecimal smclsAvgSign;

	/**
	 * 人均科次(小班)
	 */
	private BigDecimal smclsAvgSubNum;

	/**
	 * 实签预收额(目标班)
	 */
	private BigDecimal permclsRealSignPreFundAmt;

	/**
	 * 签单人数(目标班)
	 */
	private Integer permclsSignNum;

	/**
	 * 个人总预收款
	 */
	private BigDecimal preFundTotal;

	/**
	 * 预收目标
	 */
	private BigDecimal preFundTarg;

	/**
	 * 一对一退费
	 */
	private BigDecimal oooReFund;
	/**
	 * 小班退费
	 */
	private BigDecimal smReFund;
	/**
	 * 预收目标达成率
	 */
	private String preFundTargRat;
	/**
	 * 一对一预收目标
	 */
	private String oooPreFundTarg;
	/**
	 * 一对一预收达成率
	 */
	private String oooPreFundRat;

	/**
	 * 小班退费科次
	 */
	private BigDecimal smclsReFundSubNum;
	/**
	 * 小班科次目标
	 */
	private String smClsSubTarg;
	/**
	 * 小班科次达成率
	 */
	private String smclsSubNumRat;

	/**
	 * 小班预收目标
	 */
	private String smclsPreFundTarg;
	/**
	 * 小班预收达成率
	 */
	private String smclsPreFundRat;
	/**
	 * 目标班预收目标
	 */
	private String permclsPreFundTarg;
	/**
	 * 目标班预收达成率
	 */
	private String permclsPreFundTargRat;

	/**
	 * 转介绍个数
	 */
	private Integer transIntrod;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getOooRealSignPreFundAmt() {
		return oooRealSignPreFundAmt;
	}

	public void setOooRealSignPreFundAmt(BigDecimal oooRealSignPreFundAmt) {
		this.oooRealSignPreFundAmt = oooRealSignPreFundAmt;
	}

	public Integer getOooSignNum() {
		return oooSignNum;
	}

	public void setOooSignNum(Integer oooSignNum) {
		this.oooSignNum = oooSignNum;
	}

	public Integer getOooSignSubNum() {
		return oooSignSubNum;
	}

	public void setOooSignSubNum(Integer oooSignSubNum) {
		this.oooSignSubNum = oooSignSubNum;
	}

	public Integer getOooRealSignHr() {
		return oooRealSignHr;
	}

	public void setOooRealSignHr(Integer oooRealSignHr) {
		this.oooRealSignHr = oooRealSignHr;
	}

	public BigDecimal getOooFreeSignHr() {
		return oooFreeSignHr;
	}

	public void setOooFreeSignHr(BigDecimal oooFreeSignHr) {
		this.oooFreeSignHr = oooFreeSignHr;
	}

	public BigDecimal getOooAvgSign() {
		return oooAvgSign;
	}

	public void setOooAvgSign(BigDecimal oooAvgSign) {
		this.oooAvgSign = oooAvgSign;
	}

	public String getOooFreeRat() {
		return oooFreeRat;
	}

	public void setOooFreeRat(String oooFreeRat) {
		this.oooFreeRat = oooFreeRat;
	}

	public BigDecimal getOooAvgSubNum() {
		return oooAvgSubNum;
	}

	public void setOooAvgSubNum(BigDecimal oooAvgSubNum) {
		this.oooAvgSubNum = oooAvgSubNum;
	}

	public BigDecimal getSmclsRealSignPreFundAmt() {
		return smclsRealSignPreFundAmt;
	}

	public void setSmclsRealSignPreFundAmt(BigDecimal smclsRealSignPreFundAmt) {
		this.smclsRealSignPreFundAmt = smclsRealSignPreFundAmt;
	}

	public Integer getSmclsSignNum() {
		return smclsSignNum;
	}

	public void setSmclsSignNum(Integer smclsSignNum) {
		this.smclsSignNum = smclsSignNum;
	}

	public Integer getSmclsSignSubNum() {
		return smclsSignSubNum;
	}

	public void setSmclsSignSubNum(Integer smclsSignSubNum) {
		this.smclsSignSubNum = smclsSignSubNum;
	}

	public BigDecimal getSmclsAvgSign() {
		return smclsAvgSign;
	}

	public void setSmclsAvgSign(BigDecimal smclsAvgSign) {
		this.smclsAvgSign = smclsAvgSign;
	}

	public BigDecimal getSmclsAvgSubNum() {
		return smclsAvgSubNum;
	}

	public void setSmclsAvgSubNum(BigDecimal smclsAvgSubNum) {
		this.smclsAvgSubNum = smclsAvgSubNum;
	}

	public BigDecimal getPermclsRealSignPreFundAmt() {
		return permclsRealSignPreFundAmt;
	}

	public void setPermclsRealSignPreFundAmt(
			BigDecimal permclsRealSignPreFundAmt) {
		this.permclsRealSignPreFundAmt = permclsRealSignPreFundAmt;
	}

	public Integer getPermclsSignNum() {
		return permclsSignNum;
	}

	public void setPermclsSignNum(Integer permclsSignNum) {
		this.permclsSignNum = permclsSignNum;
	}

	public BigDecimal getPreFundTotal() {
		return preFundTotal;
	}

	public void setPreFundTotal(BigDecimal preFundTotal) {
		this.preFundTotal = preFundTotal;
	}

	public BigDecimal getPreFundTarg() {
		return preFundTarg;
	}

	public void setPreFundTarg(BigDecimal preFundTarg) {
		this.preFundTarg = preFundTarg;
	}

	public BigDecimal getOooReFund() {
		return oooReFund;
	}

	public void setOooReFund(BigDecimal oooReFund) {
		this.oooReFund = oooReFund;
	}

	public BigDecimal getSmReFund() {
		return smReFund;
	}

	public void setSmReFund(BigDecimal smReFund) {
		this.smReFund = smReFund;
	}

	public String getPreFundTargRat() {
		return preFundTargRat;
	}

	public void setPreFundTargRat(String preFundTargRat) {
		this.preFundTargRat = preFundTargRat;
	}

	public String getOooPreFundTarg() {
		return oooPreFundTarg;
	}

	public void setOooPreFundTarg(String oooPreFundTarg) {
		this.oooPreFundTarg = oooPreFundTarg;
	}

	public String getOooPreFundRat() {
		return oooPreFundRat;
	}

	public void setOooPreFundRat(String oooPreFundRat) {
		this.oooPreFundRat = oooPreFundRat;
	}

	public BigDecimal getSmclsReFundSubNum() {
		return smclsReFundSubNum;
	}

	public void setSmclsReFundSubNum(BigDecimal smclsReFundSubNum) {
		this.smclsReFundSubNum = smclsReFundSubNum;
	}

	public String getSmClsSubTarg() {
		return smClsSubTarg;
	}

	public void setSmClsSubTarg(String smClsSubTarg) {
		this.smClsSubTarg = smClsSubTarg;
	}

	public String getSmclsSubNumRat() {
		return smclsSubNumRat;
	}

	public void setSmclsSubNumRat(String smclsSubNumRat) {
		this.smclsSubNumRat = smclsSubNumRat;
	}

	public String getSmclsPreFundTarg() {
		return smclsPreFundTarg;
	}

	public void setSmclsPreFundTarg(String smclsPreFundTarg) {
		this.smclsPreFundTarg = smclsPreFundTarg;
	}

	public String getSmclsPreFundRat() {
		return smclsPreFundRat;
	}

	public void setSmclsPreFundRat(String smclsPreFundRat) {
		this.smclsPreFundRat = smclsPreFundRat;
	}

	public String getPermclsPreFundTarg() {
		return permclsPreFundTarg;
	}

	public void setPermclsPreFundTarg(String permclsPreFundTarg) {
		this.permclsPreFundTarg = permclsPreFundTarg;
	}

	public String getPermclsPreFundTargRat() {
		return permclsPreFundTargRat;
	}

	public void setPermclsPreFundTargRat(String permclsPreFundTargRat) {
		this.permclsPreFundTargRat = permclsPreFundTargRat;
	}

	public Integer getTransIntrod() {
		return transIntrod;
	}

	public void setTransIntrod(Integer transIntrod) {
		this.transIntrod = transIntrod;
	}

}
