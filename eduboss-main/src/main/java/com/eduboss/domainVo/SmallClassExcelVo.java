package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * 用于导出班课Vo
 * @author xiaojinwang
 *
 */
public class SmallClassExcelVo {

	//班名
	private String className;//
	//年份
	private String productVersionName;//
	//季度
	private String productQuarterName;//
	//星期/期
	private String phaseName;//
	//班型
	private String classTypeName;//
	//限额
	private BigDecimal  peopleQuantity;//
	//已报名人数
	private BigDecimal peopleNum;//
	//未缴费人数
	private BigDecimal unPayNum;//
	//部分缴费人数
	private BigDecimal payingNum;//
	//已缴费人数
	private BigDecimal paidNum;//
	//退费人数
	private BigDecimal closeProductNum;//

	private String branchName;
	//教学点
	private String campusName;//
	//教室
	private String classRoomName;//
	//年级
	private String gradeName;//
	//科目
	private String subjectName;//
	//任课教师
	private String teacherName;//
	//辅导老师
	private String studyManagerName;//
	//开课日期
	private String classDate;//
	//结束日期
	private String classEndDate;
	//上课时间
	private String classTime;//
	//课时长度
	private BigDecimal courseClassNum;//
	//课时时长
	private BigDecimal classTimeLength;//
	//每讲长度（小时）
	private BigDecimal courseTimeLength;// 
	//单价
	private BigDecimal unitPrice;//
	//每讲单价
	private BigDecimal coursePrice;//
	//课次
	private BigDecimal courseCount;
	//金额（不含资料费）
	private BigDecimal classAmount;//
	//小班状态
	private String classStatus;//
	//总课时
	private BigDecimal totalClassHours;//
	//已排课时
	private BigDecimal alreadyClassHours;
	//已上课时
	private BigDecimal consumeClassHours;//
	//建班时间
	private String createTime;//
	//绑定教材
	private String textBookName;//
	
	public SmallClassExcelVo(){}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public BigDecimal getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(BigDecimal peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	public BigDecimal getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(BigDecimal peopleNum) {
		this.peopleNum = peopleNum;
	}

	public BigDecimal getUnPayNum() {
		return unPayNum;
	}

	public void setUnPayNum(BigDecimal unPayNum) {
		this.unPayNum = unPayNum;
	}

	public BigDecimal getPayingNum() {
		return payingNum;
	}

	public void setPayingNum(BigDecimal payingNum) {
		this.payingNum = payingNum;
	}

	public BigDecimal getPaidNum() {
		return paidNum;
	}

	public void setPaidNum(BigDecimal paidNum) {
		this.paidNum = paidNum;
	}

	public BigDecimal getCloseProductNum() {
		return closeProductNum;
	}

	public void setCloseProductNum(BigDecimal closeProductNum) {
		this.closeProductNum = closeProductNum;
	}

	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}

	public String getClassRoomName() {
		return classRoomName;
	}

	public void setClassRoomName(String classRoomName) {
		this.classRoomName = classRoomName;
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

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getStudyManagerName() {
		return studyManagerName;
	}

	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}

	public String getClassDate() {
		return classDate;
	}

	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public BigDecimal getCourseClassNum() {
		return courseClassNum;
	}

	public void setCourseClassNum(BigDecimal courseClassNum) {
		this.courseClassNum = courseClassNum;
	}

	public BigDecimal getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(BigDecimal classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	public BigDecimal getCourseTimeLength() {
		return courseTimeLength;
	}

	public void setCourseTimeLength(BigDecimal courseTimeLength) {
		this.courseTimeLength = courseTimeLength;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getCoursePrice() {
		return coursePrice;
	}

	public void setCoursePrice(BigDecimal coursePrice) {
		this.coursePrice = coursePrice;
	}

	public BigDecimal getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(BigDecimal courseCount) {
		this.courseCount = courseCount;
	}

	public BigDecimal getClassAmount() {
		return classAmount;
	}

	public void setClassAmount(BigDecimal classAmount) {
		this.classAmount = classAmount;
	}

	public String getClassStatus() {
		return classStatus;
	}

	public void setClassStatus(String classStatus) {
		this.classStatus = classStatus;
	}

	public BigDecimal getTotalClassHours() {
		return totalClassHours;
	}

	public void setTotalClassHours(BigDecimal totalClassHours) {
		this.totalClassHours = totalClassHours;
	}

	public BigDecimal getAlreadyClassHours() {
		return alreadyClassHours;
	}

	public void setAlreadyClassHours(BigDecimal alreadyClassHours) {
		this.alreadyClassHours = alreadyClassHours;
	}

	public BigDecimal getConsumeClassHours() {
		return consumeClassHours;
	}

	public void setConsumeClassHours(BigDecimal consumeClassHours) {
		this.consumeClassHours = consumeClassHours;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTextBookName() {
		return textBookName;
	}

	public void setTextBookName(String textBookName) {
		this.textBookName = textBookName;
	}

	public String getClassEndDate() {
		return classEndDate;
	}

	public void setClassEndDate(String classEndDate) {
		this.classEndDate = classEndDate;
	}


	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
}
