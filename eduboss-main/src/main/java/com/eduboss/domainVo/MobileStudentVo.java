package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.MobileType;
import com.eduboss.common.MobileUserType;
import com.eduboss.domain.User;

public class MobileStudentVo {
	

    private String id;
    private String name;
    private String contact;
    private String schoolId;
    private String schoolName;
    private String blCampusId;
    private String blCampusName;
    private String gradeName;
    private String gradeId;
    private String oneOnOneStatus;
    private String oneOnOneStatusName;
    private String studyManegerId;
    private String studyManegerName;
    private BigDecimal remainingAmount; // 剩余金额
    private BigDecimal oneOnOneRemainingHour;// 剩余课时
    private BigDecimal miniRemainingHour;//小班剩余课时
    private String smallClassStatus;
    private String smallClassStatusName;
    private String status;
    private String statusName;
	private String sex;
    private String bothday;//生日
    private String enrolDate;//入学日期
    private String fatherName;
    private String fatherPhone;
    private String motherName;
    private String notherPhone;
    private String latestCustomerId;
    private String latestCustomerName;
    private String latestCustomerContact;
    private String otmRemainingHour;
    
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
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
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
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getOneOnOneStatus() {
		return oneOnOneStatus;
	}
	public void setOneOnOneStatus(String oneOnOneStatus) {
		this.oneOnOneStatus = oneOnOneStatus;
	}
	public String getOneOnOneStatusName() {
		return oneOnOneStatusName;
	}
	public void setOneOnOneStatusName(String oneOnOneStatusName) {
		this.oneOnOneStatusName = oneOnOneStatusName;
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
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}
	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	public BigDecimal getMiniRemainingHour() {
		return miniRemainingHour;
	}
	public void setMiniRemainingHour(BigDecimal miniRemainingHour) {
		this.miniRemainingHour = miniRemainingHour;
	}
	public String getSmallClassStatus() {
		return smallClassStatus;
	}
	public void setSmallClassStatus(String smallClassStatus) {
		this.smallClassStatus = smallClassStatus;
	}
	public String getSmallClassStatusName() {
		return smallClassStatusName;
	}
	public void setSmallClassStatusName(String smallClassStatusName) {
		this.smallClassStatusName = smallClassStatusName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
	public String getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
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
	public String getLatestCustomerId() {
		return latestCustomerId;
	}
	public void setLatestCustomerId(String latestCustomerId) {
		this.latestCustomerId = latestCustomerId;
	}
	public String getLatestCustomerName() {
		return latestCustomerName;
	}
	public void setLatestCustomerName(String latestCustomerName) {
		this.latestCustomerName = latestCustomerName;
	}
	public String getLatestCustomerContact() {
		return latestCustomerContact;
	}
	public void setLatestCustomerContact(String latestCustomerContact) {
		this.latestCustomerContact = latestCustomerContact;
	}

	public String getOtmRemainingHour() {
		return otmRemainingHour;
	}

	public void setOtmRemainingHour(String otmRemainingHour) {
		this.otmRemainingHour = otmRemainingHour;
	}
}
