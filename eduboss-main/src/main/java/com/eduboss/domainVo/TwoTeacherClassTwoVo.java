package com.eduboss.domainVo;

import com.eduboss.common.MiniClassStatus;

public class TwoTeacherClassTwoVo {
	private int id;
	private String twoTeacherClassId;
	private String twoTeacherClassName;
	private String name;
	private String blCampusId;
	private String blCampusName;
	private String classroomId;
	private String classroomName;
	private String teacherId;
	private String teacherName;
	private Integer peopleQuantity;
	private MiniClassStatus status;

	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String remark;
	private String qq;
	private String webChat;

	private String subjectName;
	private String subject;
	private String gradeName;
	private String gradeId;
	private String mainTeacherName;
	private String mainTeacherId;
	private String productName;

	private String startDate;
	private String endDate;


	private String blBrenchName;
	private String brenchId;
	private String classTypeName;
	private String classTypeId;
	private String totalHours;
	private String startCourseDate;
	private String courseTime;
	private String statusName;

	private Integer realPeople;
	private String oper;

	private Integer classTimeLength;
	private Double everyCourseClassNum;

	private int twoTeacherClassPeopleNum; //已报读人数

	private Double consume;//已上课时

	private String nextTwoTeacherClassTime;//下次上课时间
	
	private String productVersion;
	private String productQuarter;

	private String productCourseSeriesId;//产品系列

	public String getProductCourseSeriesId() {
		return productCourseSeriesId;
	}

	public void setProductCourseSeriesId(String productCourseSeriesId) {
		this.productCourseSeriesId = productCourseSeriesId;
	}

	public String getOper() {
		return oper;
	}

	public void setOper(String oper) {
		this.oper = oper;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getMainTeacherName() {
		return mainTeacherName;
	}

	public void setMainTeacherName(String mainTeacherName) {
		this.mainTeacherName = mainTeacherName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTwoTeacherClassId() {
		return twoTeacherClassId;
	}

	public void setTwoTeacherClassId(String twoTeacherClassId) {
		this.twoTeacherClassId = twoTeacherClassId;
	}

	public String getTwoTeacherClassName() {
		return twoTeacherClassName;
	}

	public void setTwoTeacherClassName(String twoTeacherClassName) {
		this.twoTeacherClassName = twoTeacherClassName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public MiniClassStatus getStatus() {
		return status;
	}

	public void setStatus(MiniClassStatus status) {
		this.status = status;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWebChat() {
		return webChat;
	}

	public void setWebChat(String webChat) {
		this.webChat = webChat;
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


	public String getBlBrenchName() {
		return blBrenchName;
	}

	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public String getStartCourseDate() {
		return startCourseDate;
	}

	public void setStartCourseDate(String startCourseDate) {
		this.startCourseDate = startCourseDate;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	public Integer getRealPeople() {
		return realPeople;
	}

	public void setRealPeople(Integer realPeople) {
		this.realPeople = realPeople;
	}

	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}

	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}

	public int getTwoTeacherClassPeopleNum() {
		return twoTeacherClassPeopleNum;
	}

	public void setTwoTeacherClassPeopleNum(int twoTeacherClassPeopleNum) {
		this.twoTeacherClassPeopleNum = twoTeacherClassPeopleNum;
	}

	public Double getConsume() {
		return consume;
	}

	public void setConsume(Double consume) {
		this.consume = consume;
	}

	public String getNextTwoTeacherClassTime() {
		return nextTwoTeacherClassTime;
	}

	public void setNextTwoTeacherClassTime(String nextTwoTeacherClassTime) {
		this.nextTwoTeacherClassTime = nextTwoTeacherClassTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getMainTeacherId() {
		return mainTeacherId;
	}

	public void setMainTeacherId(String mainTeacherId) {
		this.mainTeacherId = mainTeacherId;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(String brenchId) {
		this.brenchId = brenchId;
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
	
	
}
