package com.eduboss.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.StudentOneOnManyStatus;
import com.eduboss.common.StudentOneOnOneStatus;
import com.eduboss.common.StudentSmallClassStatus;
import com.eduboss.common.StudentStatus;
import com.eduboss.common.StudentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Student entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "student")
public class Student implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private String sex;
	private String bothday;
	private String contact;
	private String provice;
	private String city;
	private String erea;
	private StudentSchool school;
	private StudentSchoolTemp schoolTemp;
	private String schoolOrTemp;//显示school or schoolTemp   ： school ： 显示 合同的school字段     schoolTemp ： 显示合同 的schoolTemp 字段
	//private String grade;
	private String classNo;
	private String blCampusId;
	private String habit;
	private String gradeLevel;
	
	private DataDict gradeDict;
	
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
	private User studyManeger;
	
	private String address;
	private String log;
	private String lat;
	
	private String appPassword;
	private Organization blCampus;
	private String enrolDate;//入学日期
	
	private String studentStatus;
	
	private String classes;
	
	private StudentType studentType;//学生类型 ，签合同的正式学生和潜在学生
	
	private String relatedStudentNo;
	
	private String finishTime;//'停课/结课时间'
	
	@Column(name = "STU_STATUS", length = 32)
	public String getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}

	/**以下@Transient**/
	
	
	private Set<Customer> customers = new HashSet<Customer>();
	
	// Constructors

	/** default constructor */
	public Student() {
	}
	
	public Student(String id) {
		this.id = id;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 64)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX", length = 2)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "BOTHDAY", length = 10)
	public String getBothday() {
		return this.bothday;
	}

	public void setBothday(String bothday) {
		this.bothday = bothday;
	}

	@Column(name = "PROVICE", length = 8)
	public String getProvice() {
		return this.provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	@Column(name = "CITY", length = 32)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "EREA", length = 32)
	public String getErea() {
		return this.erea;
	}

	public void setErea(String erea) {
		this.erea = erea;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SCHOOL")
	public StudentSchool getSchool() {
		return this.school;
	}

	public void setSchool(StudentSchool school) {
		this.school = school;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SCHOOL_TEMP")
	public StudentSchoolTemp getSchoolTemp() {
		return schoolTemp;
	}

	public void setSchoolTemp(StudentSchoolTemp schoolTemp) {
		this.schoolTemp = schoolTemp;
	}


	//@Column(name = "GRADE")
	//public String getGrade() {
	//	return this.grade;
	//}

	//public void setGrade(String grade) {
	//	this.grade = grade;
	//}

	@Column(name = "CLASS_NO", length = 32)
	public String getClassNo() {
		return this.classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	@Column(name = "HABIT", length = 128)
	public String getHabit() {
		return this.habit;
	}
	
	
	@Column(name = "BL_CAMPUS_ID", length = 32)
	public String getBlCampusId() {
		return blCampusId;
	}


	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}


	public void setHabit(String habit) {
		this.habit = habit;
	}

	@Column(name = "GRADE_LEVEL", length = 32)
	public String getGradeLevel() {
		return this.gradeLevel;
	}

	public void setGradeLevel(String gradeLevel) {
		this.gradeLevel = gradeLevel;
	}

	@Column(name = "FATHER_NAME", length = 32)
	public String getFatherName() {
		return this.fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	@Column(name = "FATHER_PHONE", length = 16)
	public String getFatherPhone() {
		return this.fatherPhone;
	}

	public void setFatherPhone(String fatherPhone) {
		this.fatherPhone = fatherPhone;
	}

	@Column(name = "MOTHER_NAME", length = 32)
	public String getMotherName() {
		return this.motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	@Column(name = "NOTHER_PHONE", length = 16)
	public String getNotherPhone() {
		return this.notherPhone;
	}

	public void setNotherPhone(String notherPhone) {
		this.notherPhone = notherPhone;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 20)
	public StudentStatus getStatus() {
		return this.status;
	}

	public void setStatus(StudentStatus status) {
		this.status = status;
	}

	@Column(name = "STUDY_MANEGER_ID", length = 32)
	public String getStudyManegerId() {
		return this.studyManegerId;
	}

	public void setStudyManegerId(String studyManegerId) {
		this.studyManegerId = studyManegerId;
	}

	@Column(name = "ATTANCE_NO", length = 64)
	public String getAttanceNo() {
		return this.attanceNo;
	}

	public void setAttanceNo(String attanceNo) {
		this.attanceNo = attanceNo;
	}

	@Column(name = "CREATE_USER_ID", length = 20)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 20)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	//@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Grade_id")
	public DataDict getGradeDict() {
		return gradeDict;
	}

	public void setGradeDict(DataDict gradeDict) {
		this.gradeDict = gradeDict;
	}
	
	@JsonIgnore
	@ManyToMany(mappedBy="students")
	public Set<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}

	@Column(name = "IC_CARD_NO", length = 64)
	public String getIcCardNo() {
		return icCardNo;
	}

	public void setIcCardNo(String icCardNo) {
		this.icCardNo = icCardNo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANEGER_ID", insertable=false, updatable=false)
	public User getStudyManeger() {
		return studyManeger;
	}

	public void setStudyManeger(User studyManeger) {
		this.studyManeger = studyManeger;
	}

	@Column(name = "address", length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "log", length = 255)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Column(name = "lat", length = 255)
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@Column(name = "APP_PASSWORD", length = 32)
	public String getAppPassword() {
		return appPassword;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID", insertable=false, updatable=false)
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}
	
	@Column(name = "CONTACT", length = 32)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "ENROL_DATE", length = 20)
	public String getEnrolDate() {
		return enrolDate;
	}

	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ONEONONE_STATUS", length = 20)
	public StudentOneOnOneStatus getOneOnOneStatus() {
		return oneOnOneStatus;
	}

	public void setOneOnOneStatus(StudentOneOnOneStatus oneOnOneStatus) {
		this.oneOnOneStatus = oneOnOneStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "SMALL_CLASS_STATUS", length = 20)
	public StudentSmallClassStatus getSmallClassStatus() {
		return smallClassStatus;
	}

	public void setSmallClassStatus(StudentSmallClassStatus smallClassStatus) {
		this.smallClassStatus = smallClassStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ONEONMANY_SATUS", length = 20)
	public StudentOneOnManyStatus getOneOnManyStatus() {
		return oneOnManyStatus;
	}

	public void setOneOnManyStatus(StudentOneOnManyStatus oneOnManyStatus) {
		this.oneOnManyStatus = oneOnManyStatus;
	}

	
	@Column(name = "CLASSES", length = 100)
	public String getClasses() {
		return classes;
	}

	public void setClasses(String classes) {
		this.classes = classes;
	}

	@Column(name = "ONEONONE_FIRST_TIME",length = 20)
	public String getOneOnOneFirstTime() {
		return oneOnOneFirstTime;
	}

	public void setOneOnOneFirstTime(String oneOnOneFirstTime) {
		this.oneOnOneFirstTime = oneOnOneFirstTime;
	}

	@Column(name = "SMALL_CLASS_FIRST_TIME",length = 20)
	public String getSmallClassFirstTime() {
		return smallClassFirstTime;
	}

	public void setSmallClassFirstTime(String smallClassFirstTime) {
		this.smallClassFirstTime = smallClassFirstTime;
	}

	@Column(name = "ONEONMANY_FIRST_TIME",length = 20)
	public String getOneOnManyFirstTime() {
		return oneOnManyFirstTime;
	}

	public void setOneOnManyFirstTime(String oneOnManyFirstTime) {
		this.oneOnManyFirstTime = oneOnManyFirstTime;
	}

	@Column(name = "ECS_CLASS_FIRST_TIME",length = 20)
	public String getEcsClassFirstTime() {
		return ecsClassFirstTime;
	}

	public void setEcsClassFirstTime(String ecsClassFirstTime) {
		this.ecsClassFirstTime = ecsClassFirstTime;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "STUDENT_TYPE", length = 20)
	public StudentType getStudentType() {
		return studentType;
	}

	public void setStudentType(StudentType studentType) {
		this.studentType = studentType;
	}

	@Column(name = "schoolOrTemp")
	public String getSchoolOrTemp() {
		return schoolOrTemp;
	}

	public void setSchoolOrTemp(String schoolOrTemp) {
		this.schoolOrTemp = schoolOrTemp;
	}

	@Column(name = "RELATED_STUDENT_NO", length = 32)
    public String getRelatedStudentNo() {
        return relatedStudentNo;
    }

    public void setRelatedStudentNo(String relatedStudentNo) {
        this.relatedStudentNo = relatedStudentNo;
    }
    
    @Column(name = "FINISH_TIME", length = 20)
	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
    
    

	//	@Column(name = "FINGER_INFO", length = 50)
//	public String getFingerInfo() {
//		return fingerInfo;
//	}
//
//	public void setFingerInfo(String fingerInfo) {
//		this.fingerInfo = fingerInfo;
//	}


}