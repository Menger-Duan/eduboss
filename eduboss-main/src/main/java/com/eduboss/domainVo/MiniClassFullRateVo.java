package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 小班满率班退费率条件
 * @author xiaojinwang
 *
 */
public class MiniClassFullRateVo {

	//教材年份
	private String productVersion;
	private String productVersionName;
	//教材季度
	private String productQuarter;
	private String productQuarterName;
	//分公司
	private String blBrenchId;
	//分公司名称
	private String blBrenchName;
	//校区id
	private String blCampusId;
	//校区名称
	private String blCampusName;
	//年级
	private String grade;
	private String gradeName;
	//科目
	private String subject;
	private String subjectName;
	//班级类型
	private String classType;
	private String classTypeName;
	//老师id
	private String teacherId;
	//老师
	private String teacherName;

	//已缴全款学生数
	private BigDecimal paidFullAmount;
	//计划招生学生数
	private Integer planAmount;

	//比例
	private BigDecimal fullClassRate;
		
	//小班名称
	private String miniClassName;
	
	
	public MiniClassFullRateVo() {
		
	}


	public String getProductVersion() {
		return productVersion;
	}


	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}


	public String getProductVersionName() {
		return productVersionName;
	}


	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}


	public String getProductQuarter() {
		return productQuarter;
	}


	public void setProductQuarter(String productQuarter) {
		this.productQuarter = productQuarter;
	}


	public String getProductQuarterName() {
		return productQuarterName;
	}


	public void setProductQuarterName(String productQuarterName) {
		this.productQuarterName = productQuarterName;
	}


	public String getBlBrenchId() {
		return blBrenchId;
	}


	public void setBlBrenchId(String blBrenchId) {
		this.blBrenchId = blBrenchId;
	}


	public String getBlBrenchName() {
		return blBrenchName;
	}


	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}


	public String getBlCampusId() {
		return blCampusId;
	}


	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
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


	public String getGradeName() {
		return gradeName;
	}


	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}


	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public String getSubjectName() {
		return subjectName;
	}


	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}


	public String getClassType() {
		return classType;
	}


	public void setClassType(String classType) {
		this.classType = classType;
	}


	public String getClassTypeName() {
		return classTypeName;
	}


	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}


	public String getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
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


	public BigDecimal getFullClassRate() {
		return fullClassRate;
	}


	public void setFullClassRate(BigDecimal fullClassRate) {
		this.fullClassRate = fullClassRate;
	}


	public String getMiniClassName() {
		return miniClassName;
	}


	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	
    
	

}
