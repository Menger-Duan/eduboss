package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 小班满率班退费率条件
 * @author xiaojinwang
 *
 */
public class MiniClassQuitRateProductExcelVo {

	//教材年份
	private String productVersion;
	//教材季度
	private String productQuarter;
	//分公司名称
	private String blBrenchName;
	//校区名称
	private String blCampusName;
	//年级
	private String grade;
	//科目
	private String subject;
	//已缴全款无课消退费学生数  
	private BigDecimal paidZeroConsumeNum;
	//已缴全款实时在班无课消学生数
	private BigDecimal zeroConsumeNum;
	//课前退费率
	private String preQuitClassRate;		
	//已缴全款有课消退费学生数  
	private BigDecimal paidConsumeNum;
	//已缴全款实时在班有课消学生数
	private BigDecimal consumeNum;
	//课后退费率
	private String afterQuitClassRate;
	
	public MiniClassQuitRateProductExcelVo() {
		
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getProductQuarter() {
		return productQuarter;
	}

	public void setProductQuarter(String productQuarter) {
		this.productQuarter = productQuarter;
	}

	public String getBlBrenchName() {
		return blBrenchName;
	}

	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public BigDecimal getPaidZeroConsumeNum() {
		return paidZeroConsumeNum;
	}

	public void setPaidZeroConsumeNum(BigDecimal paidZeroConsumeNum) {
		this.paidZeroConsumeNum = paidZeroConsumeNum;
	}

	public BigDecimal getZeroConsumeNum() {
		return zeroConsumeNum;
	}

	public void setZeroConsumeNum(BigDecimal zeroConsumeNum) {
		this.zeroConsumeNum = zeroConsumeNum;
	}

	public String getPreQuitClassRate() {
		return preQuitClassRate;
	}

	public void setPreQuitClassRate(String preQuitClassRate) {
		this.preQuitClassRate = preQuitClassRate;
	}

	public BigDecimal getPaidConsumeNum() {
		return paidConsumeNum;
	}

	public void setPaidConsumeNum(BigDecimal paidConsumeNum) {
		this.paidConsumeNum = paidConsumeNum;
	}

	public BigDecimal getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(BigDecimal consumeNum) {
		this.consumeNum = consumeNum;
	}

	public String getAfterQuitClassRate() {
		return afterQuitClassRate;
	}

	public void setAfterQuitClassRate(String afterQuitClassRate) {
		this.afterQuitClassRate = afterQuitClassRate;
	}

    

}
