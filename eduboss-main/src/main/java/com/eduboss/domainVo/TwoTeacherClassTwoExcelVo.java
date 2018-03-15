package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.math.BigInteger;



/**
 * 用于导出班课Vo
 * @author xiaojinwang
 *
 */
public class TwoTeacherClassTwoExcelVo {
	
	//双师辅班班名
	private String classTwoName;//
	//年份
	private String productVersionName;//
	//季度
	private String productQuarterName;//
	//期 双师辅班对应主班期数
	private String phaseName;//
	//班级类型
	private String classTypeName;//
	//计划招生人数
    private Integer peopleQuantity;
    //已报名人数
    private BigInteger applyNum;
	//退费人数
	private BigInteger closeProductNum;//
	//所属校区
	private String campusName;//
	//教室
	private String classRoomName;//
	//年级
	private String gradeName;//
	//科目
	private String subjectName;//
	//主讲教师
	private String mainTeacherName;//
	//辅导老师
	private String teacherName;//
	//开课日期
	private String classDate;//
	//结束日期
	private String classEndDate;
	//上课时间
	private String classTime;//
	//课时长度
	private BigDecimal courseClassNum;//
	//课时时长
	private Integer classTimeLength;//
	//单价
	private BigDecimal unitPrice;//
	//金额（不含资料费）
	private BigDecimal classAmount;//
	//双师课程状态
	private String classStatus;//
	//总课时
	private BigDecimal totalClassHours;//
	//已排课时
	private BigDecimal alreadyClassHours;
	//已上课时
	private BigDecimal consumeClassHours;//
	//建班时间
	private String createTime;//

	/**
	 * 所属分公司id
	 */
	private String blBrenchId;

	/**
	 * 双师辅班所属校区
	 */
	private String blCampusId;


	private String gradeId;

	private String subjectId;

	private String classTypeId;

	private String twoTeacherId;

	private String blBrenchName;

	private String blCampusName;



	
	public TwoTeacherClassTwoExcelVo(){}


	public String getClassTwoName() {
		return classTwoName;
	}


	public void setClassTwoName(String classTwoName) {
		this.classTwoName = classTwoName;
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


	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}


	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}


	public BigInteger getApplyNum() {
		return applyNum;
	}


	public void setApplyNum(BigInteger applyNum) {
		this.applyNum = applyNum;
	}


	public BigInteger getCloseProductNum() {
		return closeProductNum;
	}


	public void setCloseProductNum(BigInteger closeProductNum) {
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


	public String getMainTeacherName() {
		return mainTeacherName;
	}


	public void setMainTeacherName(String mainTeacherName) {
		this.mainTeacherName = mainTeacherName;
	}


	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


	public String getClassDate() {
		return classDate;
	}


	public void setClassDate(String classDate) {
		this.classDate = classDate;
	}


	public String getClassEndDate() {
		return classEndDate;
	}


	public void setClassEndDate(String classEndDate) {
		this.classEndDate = classEndDate;
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


	public Integer getClassTimeLength() {
		return classTimeLength;
	}


	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}


	public BigDecimal getUnitPrice() {
		return unitPrice;
	}


	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
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


	public String getBlBrenchId() {
		return blBrenchId;
	}

	public void setBlBrenchId(String blBrenchId) {
		this.blBrenchId = blBrenchId;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getTwoTeacherId() {
		return twoTeacherId;
	}

	public void setTwoTeacherId(String twoTeacherId) {
		this.twoTeacherId = twoTeacherId;
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
}
