package com.eduboss.domainJdbc;

import com.eduboss.common.StudentOneOnManyStatus;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.StudentPromiseClassStatus;
import com.eduboss.common.StudentSmallClassStatus;
import com.eduboss.common.StudentStatus;
import com.eduboss.common.StudentType;

public class StudentJdbc {

	// Fields

	private String id;
	private String name;
	private String sex;
	private String bothday;
	private String contact;
	private String provice;
	private String city;
	private String erea;
	private String schoolId;
	private String schoolName;
	private String studentSchoolTempId;  //待审核学校
	private String classNo;
	private String blCampusId;
	private String blCampusName;
	private String habit;
	private String gradeLevel;
	
	private String gradeId;
	private String gradeName;
	
	private String fatherName;
	private String fatherPhone;
	private String motherName;
	private String notherPhone;
	private StudentStatus status;
	private StudentOneOnOneStatus oneOnOneStatus;//一对一状态
	private String oneOnOneFirstTime; //学生一对一合同产品最早日期
	private StudentSmallClassStatus smallClassStatus;//小班状态
	private String smallClassFirstTime;//学生小班合同产品最早日期
	private StudentOneOnManyStatus oneOnManyStatus; //一对多状态
	private StudentPromiseClassStatus ecsClassStatus; //目标班状态
	private String oneOnManyFirstTime;//学生一对多合同产品最早日期
	private String ecsClassFirstTime;//学生目标班合同产品最早日期
	private String studyManegerId;
	private String attanceNo;
	private String icCardNo;
	//private String fingerInfo;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	private String modifyUserId;
	private String remark;
	private String studyManagerId;
	private String studyManagerName;
	private String address;
	private String log;
	private String lat;
	
	private String appPassword;
	private String enrolDate;//入学日期
	
	private String studentStatus;
	private String classes;
	private StudentType studentType;//学生类型 ，签合同的正式学生和潜在学生
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBothday() {
		return bothday;
	}
	public void setBothday(String bothday) {
		this.bothday = bothday;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getProvice() {
		return provice;
	}
	public void setProvice(String provice) {
		this.provice = provice;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getErea() {
		return erea;
	}
	public void setErea(String erea) {
		this.erea = erea;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getClassNo() {
		return classNo;
	}
	public void setClassNo(String classNo) {
		this.classNo = classNo;
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
	public String getHabit() {
		return habit;
	}
	public void setHabit(String habit) {
		this.habit = habit;
	}
	public String getGradeLevel() {
		return gradeLevel;
	}
	public void setGradeLevel(String gradeLevel) {
		this.gradeLevel = gradeLevel;
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
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	public String getFatherPhone() {
		return fatherPhone;
	}
	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}
	public String getNotherPhone() {
		return notherPhone;
	}
	public void setNotherPhone(String notherPhone) {
		this.notherPhone = notherPhone;
	}
	public StudentStatus getStatus() {
		return status;
	}
	public void setStatus(StudentStatus status) {
		this.status = status;
	}
	public StudentOneOnOneStatus getOneOnOneStatus() {
		return oneOnOneStatus;
	}
	public void setOneOnOneStatus(StudentOneOnOneStatus oneOnOneStatus) {
		this.oneOnOneStatus = oneOnOneStatus;
	}
	public String getOneOnOneFirstTime() {
		return oneOnOneFirstTime;
	}
	public void setOneOnOneFirstTime(String oneOnOneFirstTime) {
		this.oneOnOneFirstTime = oneOnOneFirstTime;
	}
	public StudentSmallClassStatus getSmallClassStatus() {
		return smallClassStatus;
	}
	public void setSmallClassStatus(StudentSmallClassStatus smallClassStatus) {
		this.smallClassStatus = smallClassStatus;
	}
	public String getSmallClassFirstTime() {
		return smallClassFirstTime;
	}
	public void setSmallClassFirstTime(String smallClassFirstTime) {
		this.smallClassFirstTime = smallClassFirstTime;
	}
	public StudentOneOnManyStatus getOneOnManyStatus() {
		return oneOnManyStatus;
	}
	public void setOneOnManyStatus(StudentOneOnManyStatus oneOnManyStatus) {
		this.oneOnManyStatus = oneOnManyStatus;
	}
	public StudentPromiseClassStatus getEcsClassStatus() {
		return ecsClassStatus;
	}
	public void setEcsClassStatus(StudentPromiseClassStatus ecsClassStatus) {
		this.ecsClassStatus = ecsClassStatus;
	}
	public String getOneOnManyFirstTime() {
		return oneOnManyFirstTime;
	}
	public void setOneOnManyFirstTime(String oneOnManyFirstTime) {
		this.oneOnManyFirstTime = oneOnManyFirstTime;
	}
	public String getEcsClassFirstTime() {
		return ecsClassFirstTime;
	}
	public void setEcsClassFirstTime(String ecsClassFirstTime) {
		this.ecsClassFirstTime = ecsClassFirstTime;
	}
	public String getStudyManegerId() {
		return studyManegerId;
	}
	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}
	public String getAttanceNo() {
		return attanceNo;
	}
	public void setAttanceNo(String attanceNo) {
		this.attanceNo = attanceNo;
	}
	public String getIcCardNo() {
		return icCardNo;
	}
	public void setIcCardNo(String icCardNo) {
		this.icCardNo = icCardNo;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getAppPassword() {
		return appPassword;
	}
	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	public String getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}
	public String getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	public StudentType getStudentType() {
		return studentType;
	}
	public void setStudentType(StudentType studentType) {
		this.studentType = studentType;
	}

	public String getStudentSchoolTempId() {
		return studentSchoolTempId;
	}

	public void setStudentSchoolTempId(String studentSchoolTempId) {
		this.studentSchoolTempId = studentSchoolTempId;
	}
}