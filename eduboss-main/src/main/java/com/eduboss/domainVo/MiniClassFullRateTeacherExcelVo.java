package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 小班满率班退费率条件
 * @author xiaojinwang
 *
 */
public class MiniClassFullRateTeacherExcelVo {

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
	//班级类型
	private String classType;
	//老师
	private String teacherName;

	//已缴全款学生数
	private BigDecimal paidFullAmount;

	//计划招生学生数
	private Integer planAmount;
	//比例
	private String fullClassRate;
	
	public MiniClassFullRateTeacherExcelVo() {
		
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

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}


	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(Integer planAmount) {
		this.planAmount = planAmount;
	}

	public BigDecimal getPaidFullAmount() {
		return paidFullAmount;
	}

	public void setPaidFullAmount(BigDecimal paidFullAmount) {
		this.paidFullAmount = paidFullAmount;
	}

	public String getFullClassRate() {
		return fullClassRate;
	}

	public void setFullClassRate(String fullClassRate) {
		this.fullClassRate = fullClassRate;
	}

}
