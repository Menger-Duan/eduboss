package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

//客户资源导入，转换表
@Entity
@Table(name="customer_import_transform")
public class CustomerImportTransform {
	private String id;
	private String cusName;
	private String cusContact;
	private String cusType; //资源来源
	private String cusTypeName;
	private String cusOrg;  //来源细分
	private String cusOrgName;
	private String resEntrance;
	private String resEntranceName;
	private String deliverTarget;
	private String deliverTargetName;
	private String deliverType; //PERSONAL_POOL:个人;CUSTOMER_RESOURCE_POOL:公共池;
	private String deliverStatus;
	private String blSchool;
	private String createTime;
	private String createUser;
	private String lastDeliverName;
	private String studentInfoJson;
	private String cusStatus; //初始化 0；失败：-1;成功：1;
	private String remark; //跟进备注 
	private String failReason;//导入失败原因	
	private String cusId; //客户id
	
	
	//学生信息
	private String studentId;
	private String studentName;
	private String studentContact;
	private String blSchoolName;
	private String schoolId;
	private String schoolRegionId;
	private String schoolRegionName;
	private String motherName;
	private String notherPhone;
	private String fatherName;
	private String fatherPhone;
	private String classes;
	private String gradeId;
	private String gradeName; //学生年级
	private String schoolLevel; //学校级别
	
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "CUS_NAME", length = 32)
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	
	@Column(name = "CONTACT", length = 32)
	public String getCusContact() {
		return cusContact;
	}
	public void setCusContact(String cusContact) {
		this.cusContact = cusContact;
	}
	
	@Column(name = "CUS_TYPE", length = 32)
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	
	@Column(name = "CUS_ORG", length = 32)
	public String getCusOrg() {
		return cusOrg;
	}
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}
	
	@Column(name = "RES_ENTRANCE", length = 32)
	public String getResEntrance() {
		return resEntrance;
	}
	public void setResEntrance(String resEntrance) {
		this.resEntrance = resEntrance;
	}
	
	@Column(name = "DELIVER_TARGET", length = 32)
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	
	@Column(name = "DELIVER_TYPE", length = 32)
	public String getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	
	@Column(name = "DELIVER_STATUS", length = 32)
	public String getDeliverStatus() {
		return deliverStatus;
	}
	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}
	
	@Column(name = "BL_SCHOOL", length = 32)
	public String getBlSchool() {
		return blSchool;
	}
	public void setBlSchool(String blSchool) {
		this.blSchool = blSchool;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "CREATE_USER", length = 32)
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Column(name = "LAST_DELIVER_NAME", length = 32)
	public String getLastDeliverName() {
		return lastDeliverName;
	}
	public void setLastDeliverName(String lastDeliverName) {
		this.lastDeliverName = lastDeliverName;
	}
	
	@Column(name = "STUDENT_INFO_JSON", length = 4096)	
	public String getStudentInfoJson() {
		return studentInfoJson;
	}
	public void setStudentInfoJson(String studentInfoJson) {
		this.studentInfoJson = studentInfoJson;
	}
	
	@Column(name = "CUS_STATUS", length = 10)		
	public String getCusStatus() {
		return cusStatus;
	}
	public void setCusStatus(String cusStatus) {
		this.cusStatus = cusStatus;
	}
	
	@Column(name = "REMARK", length = 1000)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "FAIL_REASON", length = 300)
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
	@Column(name = "DELIVER_TARGET_NAME", length = 32)
	public String getDeliverTargetName() {
		return deliverTargetName;
	}
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	
	@Column(name = "GRADE_NAME", length = 30)
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	
	@Column(name = "CUS_TYPE_NAME", length = 50)
	public String getCusTypeName() {
		return cusTypeName;
	}
	public void setCusTypeName(String cusTypeName) {
		this.cusTypeName = cusTypeName;
	}
	
	@Column(name = "CUS_ORG_NAME", length = 50)
	public String getCusOrgName() {
		return cusOrgName;
	}
	public void setCusOrgName(String cusOrgName) {
		this.cusOrgName = cusOrgName;
	}
	
	@Column(name = "RES_ENTRANCE_NAME", length = 50)
	public String getResEntranceName() {
		return resEntranceName;
	}
	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}
	
	@Column(name = "CUS_ID", length = 32)
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	@Column(name = "SCHOOL_NAME", length = 100)
	public String getBlSchoolName() {
		return blSchoolName;
	}
	public void setBlSchoolName(String blSchoolName) {
		this.blSchoolName = blSchoolName;
	}
	
	@Column(name = "SCHOOL_ID", length = 100)
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	@Column(name = "STUDENT_ID", length = 32)
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Column(name = "STUDENT_NAME", length = 50)
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	@Column(name = "STUDENT_CONTACT", length = 50)
	public String getStudentContact() {
		return studentContact;
	}
	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
	
	@Column(name = "SCHOOL_REGION_ID", length = 32)
	public String getSchoolRegionId() {
		return schoolRegionId;
	}
	public void setSchoolRegionId(String schoolRegionId) {
		this.schoolRegionId = schoolRegionId;
	}
	
	@Column(name = "SCHOOL_REGION_NAME", length = 50)
	public String getSchoolRegionName() {
		return schoolRegionName;
	}
	public void setSchoolRegionName(String schoolRegionName) {
		this.schoolRegionName = schoolRegionName;
	}
	
	@Column(name = "MOTHER_NAME", length = 50)
	public String getMotherName() {
		return motherName;
	}
	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	@Column(name = "NOTHER_PHONE", length = 50)
	public String getNotherPhone() {
		return notherPhone;
	}
	public void setNotherPhone(String notherPhone) {
		this.notherPhone = notherPhone;
	}
	@Column(name = "FATHER_NAME", length = 50)
	public String getFatherName() {
		return fatherName;
	}
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	@Column(name = "FATHE_PHONE", length = 50)
	public String getFatherPhone() {
		return fatherPhone;
	}
	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}
	@Column(name = "CLASSES", length = 50)
	public String getClasses() {
		return classes;
	}
	public void setClasses(String classes) {
		this.classes = classes;
	}
	
	@Column(name = "GRADE_ID", length = 32)
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	
	@Column(name = "SCHOOL_LEVEL", length = 32)
	public String getSchoolLevel() {
		return schoolLevel;
	}
	public void setSchoolLevel(String schoolLevel) {
		this.schoolLevel = schoolLevel;
	}
	
	
	
}
