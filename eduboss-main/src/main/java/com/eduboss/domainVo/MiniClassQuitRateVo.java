package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 小班满率班退费率条件
 * @author xiaojinwang
 *
 */
public class MiniClassQuitRateVo {

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
	//已缴全款无课消退费学生数  
	private BigDecimal paidZeroConsumeNum;
	//已缴全款实时在班无课消学生数
	private BigDecimal zeroConsumeNum;
	//课前退费率
	private BigDecimal preQuitClassRate;
		
	//已缴全款有课消退费学生数  
	private BigDecimal paidConsumeNum;
	//已缴全款实时在班有课消学生数
	private BigDecimal consumeNum;
	//课后退费率
	private BigDecimal afterQuitClassRate;
	
	public MiniClassQuitRateVo() {
		
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

	public BigDecimal getPreQuitClassRate() {
		return preQuitClassRate;
	}

	public void setPreQuitClassRate(BigDecimal preQuitClassRate) {
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

	public BigDecimal getAfterQuitClassRate() {
		return afterQuitClassRate;
	}

	public void setAfterQuitClassRate(BigDecimal afterQuitClassRate) {
		this.afterQuitClassRate = afterQuitClassRate;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getProductQuarterName() {
		return productQuarterName;
	}

	public void setProductQuarterName(String productQuarterName) {
		this.productQuarterName = productQuarterName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

    

}
