package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.SaleChannel;
import com.eduboss.common.MiniClassStatus;


public class MiniClassVo {
	private String miniClassId;
	private String productId;
	private String productName;
	private String name;
	private String blCampusId;
	private String blCampusName;
	private String miniClassTypeId;
	private String miniClassTypeName;
	private String gradeId;
	private String gradeName;
	private String gradeSegmentId;
	private String gradeSegmentName;
	private String subjectId;
	private String subjectName;
	private String teacherId;
	private String teacherName;
	private String startDate;
	private String endDate;
	private String classTime;
//	private String classTimeId;
//	private String classTimeName;
	private Double totalClassHours;
	private Double consume;
	private Double unitPrice;
	private String remark;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String studyManegerId;
	private String studyManegerName;
	private String courseWeekday;
	private int miniClassPeopleNum; //已报读人数
	private Double everyCourseClassNum;
	private MiniClassStatus status;
	private String statusName;
//	private String classroom;
	private String classroomId;
	private String classroomName;
	private BaseStatus constraintPeopleQuantity;
	private Integer peopleQuantity;
	private String nextMiniClassTime;
	private BigDecimal totalRemainCourseHour;// 小班总剩余课时
	private BigDecimal totalChargedMoneyQuantity;// 小班累计总扣款
	private Integer classTimeLength;
	private Double alreadyTotalClassHours;
	private String miniClassCourseType;
	private String miniClassCourseTypeName;
	
	private String productGroupId;
    private String courseSeriesId;
    private String productVersionId;
    private String productQuarterId;
    private String classTypeId;
    private String productSubjectId;
    
	private String extendSubjectRate;
	private String recruitStudentRate;
	private String continueClassRate;
	
	private String recruitStudentStatusId; //招生状态ID
	private String recruitStudentStatusName; //招生状态Name
	
	
	private Integer minClassMember;//最低开班人数

	private Integer profitMember;//盈利点人数
	
	private BigDecimal miniRemainingHour;//小班剩余课时
private Double canceledHours; //已取消课程
	//最少课时，最多课时，条件查询
	private String minCourseHours;
	private String maxCourseHours;

	private Double arrangedHours; //已排课时
	 
    private String phaseId;
	private String phaseName;
	
	private String stuFirstCourseDate; //指定学生在此班首次上课日期
	
	//新增字段 教材id 教材名字   和产品一对一关系   add 2016-11-08
	private Integer textBookId;
	private String textBookName;
	
	private Boolean bindTextBook;//true 绑定  false没绑定
	
	private SaleChannel channelType; // 销售渠道

	private int campusSale;
	private int onlineSale;
	private String campusContact;
	private int onShelves;
	private String saleType;
	
	//允许超额人数
	private Integer allowedExcess;
	//课程模板id
	private Integer modalId;
	//课程模板名字
	private String modalName;
	
	private String courseSeriesName;
	private String productVersionName;
	private String productQuarterName;
	private String classTypeName;
	private String productGroupName;
	private String productSubjectName;
	

	private int isModal;

	private String productIdArray;

	public int getIsModal() {
		return isModal;
	}

	public void setIsModal(int isModal) {
		this.isModal = isModal;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getNextMiniClassTime() {
		return nextMiniClassTime;
	}

	public void setNextMiniClassTime(String nextMiniClassTime) {
		this.nextMiniClassTime = nextMiniClassTime;
	}

	public MiniClassStatus getStatus() {
		return status;
	}

	public void setStatus(MiniClassStatus status) {
		this.status = status;
	}

	public int getMiniClassPeopleNum() {
		return miniClassPeopleNum;
	}

	public void setMiniClassPeopleNum(int miniClassPeopleNum) {
		this.miniClassPeopleNum = miniClassPeopleNum;
	}

	public String getStudyManegerId() {
		return studyManegerId;
	}

	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}

	public String getStudyManegerName() {
		return studyManegerName;
	}

	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
	}

	public String getMiniClassId() {
		return miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String produceId) {
		this.productId = produceId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGradeSegmentId() {
		return gradeSegmentId;
	}

	public void setGradeSegmentId(String gradeSegmentId) {
		this.gradeSegmentId = gradeSegmentId;
	}

	public String getGradeSegmentName() {
		return gradeSegmentName;
	}

	public void setGradeSegmentName(String gradeSegmentName) {
		this.gradeSegmentName = gradeSegmentName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Double getTotalClassHours() {
		return totalClassHours;
	}

	public void setTotalClassHours(Double totalClassHours) {
		this.totalClassHours = totalClassHours;
	}

	public Double getConsume() {
		return consume;
	}

	public void setConsume(Double consume) {
		this.consume = consume;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getMiniClassTypeId() {
		return miniClassTypeId;
	}

	public void setMiniClassTypeId(String miniClassTypeId) {
		this.miniClassTypeId = miniClassTypeId;
	}

	public String getMiniClassTypeName() {
		return miniClassTypeName;
	}

	public void setMiniClassTypeName(String miniClassTypeName) {
		this.miniClassTypeName = miniClassTypeName;
	}

//	public String getClassTimeId() {
//		return classTimeId;
//	}
//
//	public void setClassTimeId(String classTimeId) {
//		this.classTimeId = classTimeId;
//	}
//
//	public String getClassTimeName() {
//		return classTimeName;
//	}
//
//	public void setClassTimeName(String classTimeName) {
//		this.classTimeName = classTimeName;
//	}

	public String getCourseWeekday() {
		return courseWeekday;
	}

	public void setCourseWeekday(String courseWeekday) {
		this.courseWeekday = courseWeekday;
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

	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}

	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}

//	public String getClassroom() {
//		return classroom;
//	}
//
//	public void setClassroom(String classroom) {
//		this.classroom = classroom;
//	}

	public BaseStatus getConstraintPeopleQuantity() {
		return constraintPeopleQuantity;
	}

	public void setConstraintPeopleQuantity(BaseStatus constraintPeopleQuantity) {
		this.constraintPeopleQuantity = constraintPeopleQuantity;
	}

	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	public BigDecimal getTotalRemainCourseHour() {
		return totalRemainCourseHour;
	}

	public void setTotalRemainCourseHour(BigDecimal totalRemainCourseHour) {
		this.totalRemainCourseHour = totalRemainCourseHour;
	}

	public BigDecimal getTotalChargedMoneyQuantity() {
		return totalChargedMoneyQuantity;
	}

	public void setTotalChargedMoneyQuantity(BigDecimal totalChargedMoneyQuantity) {
		this.totalChargedMoneyQuantity = totalChargedMoneyQuantity;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	public Double getAlreadyTotalClassHours() {
		return alreadyTotalClassHours;
	}

	public void setAlreadyTotalClassHours(Double alreadyTotalClassHours) {
		this.alreadyTotalClassHours = alreadyTotalClassHours;
	}

	public String getMiniClassCourseType() {
		return miniClassCourseType;
	}

	public void setMiniClassCourseType(String miniClassCourseType) {
		this.miniClassCourseType = miniClassCourseType;
	}

	public String getMiniClassCourseTypeName() {
		return miniClassCourseTypeName;
	}

	public void setMiniClassCourseTypeName(String miniClassCourseTypeName) {
		this.miniClassCourseTypeName = miniClassCourseTypeName;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassroomName() {
		return classroomName;
	}

	public void setClassroomName(String classroomName) {
		this.classroomName = classroomName;
	}

	public String getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(String productGroupId) {
		this.productGroupId = productGroupId;
	}

	public String getCourseSeriesId() {
		return courseSeriesId;
	}

	public void setCourseSeriesId(String courseSeriesId) {
		this.courseSeriesId = courseSeriesId;
	}

	public String getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(String productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductQuarterId() {
		return productQuarterId;
	}

	public void setProductQuarterId(String productQuarterId) {
		this.productQuarterId = productQuarterId;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getProductSubjectId() {
		return productSubjectId;
	}

	public void setProductSubjectId(String productSubjectId) {
		this.productSubjectId = productSubjectId;
	}

	public String getExtendSubjectRate() {
		return extendSubjectRate;
	}

	public void setExtendSubjectRate(String extendSubjectRate) {
		this.extendSubjectRate = extendSubjectRate;
	}

	public String getRecruitStudentRate() {
		return recruitStudentRate;
	}

	public void setRecruitStudentRate(String recruitStudentRate) {
		this.recruitStudentRate = recruitStudentRate;
	}

	public String getContinueClassRate() {
		return continueClassRate;
	}

	public void setContinueClassRate(String continueClassRate) {
		this.continueClassRate = continueClassRate;
	}

	public String getRecruitStudentStatusId() {
		return recruitStudentStatusId;
	}

	public void setRecruitStudentStatusId(String recruitStudentStatusId) {
		this.recruitStudentStatusId = recruitStudentStatusId;
	}

	public String getRecruitStudentStatusName() {
		return recruitStudentStatusName;
	}

	public void setRecruitStudentStatusName(String recruitStudentStatusName) {
		this.recruitStudentStatusName = recruitStudentStatusName;
	}

	public Integer getMinClassMember() {
		return minClassMember;
	}

	public void setMinClassMember(Integer minClassMember) {
		this.minClassMember = minClassMember;
	}

	public Integer getProfitMember() {
		return profitMember;
	}

	public void setProfitMember(Integer profitMember) {
		this.profitMember = profitMember;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public BigDecimal getMiniRemainingHour() {
		return miniRemainingHour;
	}

	public void setMiniRemainingHour(BigDecimal miniRemainingHour) {
		this.miniRemainingHour = miniRemainingHour;
	}

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	public Double getArrangedHours() {
		return arrangedHours;
	}

	public void setArrangedHours(Double arrangedHours) {
		this.arrangedHours = arrangedHours;
	}
	public String getMinCourseHours() {
	return minCourseHours;
	}

	public void setMinCourseHours(String minCourseHours) {
	this.minCourseHours = minCourseHours;
	}

	public String getMaxCourseHours() {
	return maxCourseHours;
	}

	public void setMaxCourseHours(String maxCourseHours) {
	this.maxCourseHours = maxCourseHours;
	}
public Double getCanceledHours() {
		return canceledHours;
	}

	public void setCanceledHours(Double canceledHours) {
		this.canceledHours = canceledHours;
	}

	public String getStuFirstCourseDate() {
		return stuFirstCourseDate;
	}

	public void setStuFirstCourseDate(String stuFirstCourseDate) {
		this.stuFirstCourseDate = stuFirstCourseDate;
	}

	public Integer getTextBookId() {
		return textBookId;
	}

	public void setTextBookId(Integer textBookId) {
		this.textBookId = textBookId;
	}

	public String getTextBookName() {
		return textBookName;
	}

	public void setTextBookName(String textBookName) {
		this.textBookName = textBookName;
	}

	public Boolean getBindTextBook() {
		return bindTextBook;
	}

	public void setBindTextBook(Boolean bindTextBook) {
		this.bindTextBook = bindTextBook;
	}
	
	public SaleChannel getChannelType() {
		return channelType;
	}

	public void setChannelType(SaleChannel channelType) {
		this.channelType = channelType;
	}

	public int getCampusSale() {
		return campusSale;
	}

	public void setCampusSale(int campusSale) {
		this.campusSale = campusSale;
	}

	public int getOnlineSale() {
		return onlineSale;
	}

	public void setOnlineSale(int onlineSale) {
		this.onlineSale = onlineSale;
	}

	public String getCampusContact() {
		return campusContact;
	}

	public void setCampusContact(String campusContact) {
		this.campusContact = campusContact;
	}

	public int getOnShelves() {
		return onShelves;
	}

	public void setOnShelves(int onShelves) {
		this.onShelves = onShelves;
	}

	public Integer getAllowedExcess() {
		return allowedExcess;
	}

	public void setAllowedExcess(Integer allowedExcess) {
		this.allowedExcess = allowedExcess;
	}

	public Integer getModalId() {
		return modalId;
	}

	public void setModalId(Integer modalId) {
		this.modalId = modalId;
	}

	public String getModalName() {
		return modalName;
	}

	public void setModalName(String modalName) {
		this.modalName = modalName;
	}

	public String getCourseSeriesName() {
		return courseSeriesName;
	}

	public void setCourseSeriesName(String courseSeriesName) {
		this.courseSeriesName = courseSeriesName;
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

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getProductGroupName() {
		return productGroupName;
	}

	public void setProductGroupName(String productGroupName) {
		this.productGroupName = productGroupName;
	}

	public String getProductSubjectName() {
		return productSubjectName;
	}

	public void setProductSubjectName(String productSubjectName) {
		this.productSubjectName = productSubjectName;
	}

	public String getProductIdArray() {
		return productIdArray;
	}

	public void setProductIdArray(String productIdArray) {
		this.productIdArray = productIdArray;
	}
}
