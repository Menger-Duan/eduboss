package com.eduboss.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "user_info")
public class UserInfo {

	// Fields
	private String Id;
	private String userId; //关联user表ID
	private String userName;
	private String userSex;
	private String organizationId;
	private String roleId;
	private String department; //部门
	private String userAccount; // 系统帐号	
	private String birthDate; // 出生年月	
	private DataDict nationality; // 民族 
	private DataDict politicsStatus; // 政治面貌	
	private DataDict nativePlace; // 籍贯	
	private DataDict maritalStatus; // 婚姻状况
	private String school; // 毕业院校
	private DataDict highestEducation; // 最高学历
	private String graduateTime; // 毕业时间
	private String specialty; // 所学专业
	private String domicilePlace; // 户口所在地
	private String idCard; // 身份证号码
	private String idAddress; // 身份证地址
	private String age; // 年龄
	private String hiredate; // 入职时间	
	private String conversionDate; // 转正日期
	private String contractTime; // 合同到期时间
	private DataDict workType; // 员工类型
	private String leaveDate; // 离职日期
	private DataDict leaveType;	// 离职形式
	private String leaveCause; // 离职原因
	private String specialtyHobbies; // 特长及爱好	
	private String headPortrait; // 头像	
	private String companyWorkingYears; // 入司工龄（年）
	private DataDict positioning; // 人员定位
	private String firstPrincipal; // 校区第一负责人
	private String professionOneOne; // 业务模块_1对1
	private String professionMinclass; // 业务模块_小班
	private String professionElite; // 业务模块_目标班
	private String professionOneN; // 业务模块_1对N
	private String workDate; // 教龄开始日期
	private String workingYears; // 教龄（年）
	private DataDict teacherStar; // 教师星级
	private DataDict jobLevel; // 职级
	private String primarySchool; // 学段_小学   注：1表示选中，0或null表示非选中
	private String middleSchool; // 学段_初中   注：1表示选中，0或null表示非选中
	private String highSchool; // 学段_高中  注：1表示选中，0或null表示非选中
	private String workMachine; // 工作座机
	private String mobilePhone; // 手机号
	private String homeTelephone; // 家庭电话
	private String workEmail; // 工作邮箱	
	private String qq;
	private String presentAddress; // 现住地址
	private String emergencyContact; // 紧急联络人
	private String relationship; // 与本人关系	
	private String emergencyTelephone; // 紧急联系电话	
	private String emergencyContactAddress; // 紧急联系人地址
	private String educationCertificate; // 学历证
	private String graduationCertificate; // 毕业证
	private String idCardPic; // 身份证证明
	private String bankCardPic; // 银行卡
	private String medicaExaminationReport; // 体检报告
	private String inchPhotos; // 白底彩色寸照
	private String leavingCertificate; // 离职证明
	private String leavesRequest; // 职位申请表
	private String userResume; // 个人简历
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime; 
	
	private List<Organization> organization = new ArrayList<Organization>();
	private List<Role> role = new ArrayList<Role>();
	/** default constructor */
	public UserInfo() {
	}
	
	public UserInfo(String Id) {
		this.Id = Id;
	}
	
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	@Column(name = "USER_ID", length = 32)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "USER_NAME", length = 32)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "USER_SEX", length = 2)
	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	@Column(name = "ORGANIZATION_ID", length = 32)
	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	@Column(name = "ROLE_ID", length = 32)
	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "DEPARTMENT", length = 20)
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "USER_ACCOUNT", length = 32)
	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	@Column(name = "BIRTH_DATE", length = 20)
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "NATIONALITY")
	public DataDict getNationality() {
		return nationality;
	}

	public void setNationality(DataDict nationality) {
		this.nationality = nationality;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "POLITICS_STATUS")
	public DataDict getPoliticsStatus() {
		return politicsStatus;
	}

	public void setPoliticsStatus(DataDict politicsStatus) {
		this.politicsStatus = politicsStatus;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "NATIVE_PLACE")
	public DataDict getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(DataDict nativePlace) {
		this.nativePlace = nativePlace;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MARITAL_STATUS")
	public DataDict getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(DataDict maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	@Column(name = "SCHOOL", length = 50)
	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "HIGHEST_EDUCATION")
	public DataDict getHighestEducation() {
		return highestEducation;
	}

	public void setHighestEducation(DataDict highestEducation) {
		this.highestEducation = highestEducation;
	}
	@Column(name = "GRADUATE_TIME", length = 20)
	public String getGraduateTime() {
		return graduateTime;
	}

	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}
	@Column(name = "SPECIALTY", length = 30)
	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
	@Column(name = "DOMICILE_PLACE", length = 30)
	public String getDomicilePlace() {
		return domicilePlace;
	}
	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}
	@Column(name = "ID_CARD", length = 30)
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	@Column(name = "ID_ADDRESS", length = 100)
	public String getIdAddress() {
		return idAddress;
	}

	public void setIdAddress(String idAddress) {
		this.idAddress = idAddress;
	}
	@Column(name = "AGE", length = 10)
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	@Column(name = "HIREDATE", length = 20)
	public String getHiredate() {
		return hiredate;
	}

	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}
	@Column(name = "CONVERSION_DATE", length = 20)
	public String getConversionDate() {
		return conversionDate;
	}

	public void setConversionDate(String conversionDate) {
		this.conversionDate = conversionDate;
	}
	@Column(name = "CONTRACT_TIME", length = 20)
	public String getContractTime() {
		return contractTime;
	}

	public void setContractTime(String contractTime) {
		this.contractTime = contractTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "WORK_TYPE")
	public DataDict getWorkType() {
		return workType;
	}

	public void setWorkType(DataDict workType) {
		this.workType = workType;
	}
	@Column(name = "LEAVE_DATE", length = 20)
	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LEAVE_TYPE")
	public DataDict getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(DataDict leaveType) {
		this.leaveType = leaveType;
	}
	@Column(name = "LEAVE_CAUSE", length = 500)
	public String getLeaveCause() {
		return leaveCause;
	}

	public void setLeaveCause(String leaveCause) {
		this.leaveCause = leaveCause;
	}
	@Column(name = "SPECIALTY_HOBBIES", length = 2000)
	public String getSpecialtyHobbies() {
		return specialtyHobbies;
	}

	public void setSpecialtyHobbies(String specialtyHobbies) {
		this.specialtyHobbies = specialtyHobbies;
	}
	@Column(name = "HEAD_PORTRAIT", length = 100)
	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	
	@Column(name = "COMPANY_WORKING_YEARS", length = 10)
	public String getCompanyWorkingYears() {
		return companyWorkingYears;
	}

	
	public void setCompanyWorkingYears(String companyWorkingYears) {
		this.companyWorkingYears = companyWorkingYears;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "POSITIONING")
	public DataDict getPositioning() {
		return positioning;
	}

	public void setPositioning(DataDict positioning) {
		this.positioning = positioning;
	}
	@Column(name = "FIRST_PRINCIPAL", length = 10)
	public String getFirstPrincipal() {
		return firstPrincipal;
	}

	public void setFirstPrincipal(String firstPrincipal) {
		this.firstPrincipal = firstPrincipal;
	}
	
	@Column(name = "PROFESSION_ONE_ONE", length = 2)
	public String getProfessionOneOne() {
		return professionOneOne;
	}

	public void setProfessionOneOne(String professionOneOne) {
		this.professionOneOne = professionOneOne;
	}

	@Column(name = "PROFESSION_MINCLASS", length = 2)
	public String getProfessionMinclass() {
		return professionMinclass;
	}

	public void setProfessionMinclass(String professionMinclass) {
		this.professionMinclass = professionMinclass;
	}

	@Column(name = "PROFESSION_ELITE", length = 2)
	public String getProfessionElite() {
		return professionElite;
	}

	public void setProfessionElite(String professionElite) {
		this.professionElite = professionElite;
	}
	@Column(name = "PROFESSION_ONE_N", length = 2)
	public String getProfessionOneN() {
		return professionOneN;
	}

	public void setProfessionOneN(String professionOneN) {
		this.professionOneN = professionOneN;
	}

	@Column(name = "WORK_DATE", length = 20)
	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	@Column(name = "WORKING_YEARS", length = 10)
	public String getWorkingYears() {
		return workingYears;
	}

	public void setWorkingYears(String workingYears) {
		this.workingYears = workingYears;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_STAR")
	public DataDict getTeacherStar() {
		return teacherStar;
	}

	public void setTeacherStar(DataDict teacherStar) {
		this.teacherStar = teacherStar;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "JOB_LEVEL")
	public DataDict getJobLevel() {
		return jobLevel;
	}

	public void setJobLevel(DataDict jobLevel) {
		this.jobLevel = jobLevel;
	}
	
	@Column(name = "PRIMARY_SCHOOL", length = 2)
	public String getPrimarySchool() {
		return primarySchool;
	}

	public void setPrimarySchool(String primarySchool) {
		this.primarySchool = primarySchool;
	}
	@Column(name = "MIDDLE_SCHOOL", length = 2)
	public String getMiddleSchool() {
		return middleSchool;
	}

	public void setMiddleSchool(String middleSchool) {
		this.middleSchool = middleSchool;
	}
	@Column(name = "HIGH_SCHOOL", length = 2)
	public String getHighSchool() {
		return highSchool;
	}

	public void setHighSchool(String highSchool) {
		this.highSchool = highSchool;
	}

	@Column(name = "WORK_MACHINE", length = 50)
	public String getWorkMachine() {
		return workMachine;
	}

	public void setWorkMachine(String workMachine) {
		this.workMachine = workMachine;
	}
	@Column(name = "MOBILE_PHONE", length = 20)
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	@Column(name = "HOME_TELEPHONE", length = 20)	
	public String getHomeTelephone() {
		return homeTelephone;
	}

	public void setHomeTelephone(String homeTelephone) {
		this.homeTelephone = homeTelephone;
	}
	@Column(name = "WORK_EMAIL", length = 50)	
	public String getWorkEmail() {
		return workEmail;
	}

	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}
	@Column(name = "QQ", length = 20)	
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	@Column(name = "PRESENT_ADDRESS", length = 100)	
	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}
	@Column(name = "EMERGENCY_CONTACT", length = 20)	
	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	@Column(name = "RELATIONSHIP", length = 20)	
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	@Column(name = "EMERGENCY_TELEPHONE", length = 20)	
	public String getEmergencyTelephone() {
		return emergencyTelephone;
	}

	public void setEmergencyTelephone(String emergencyTelephone) {
		this.emergencyTelephone = emergencyTelephone;
	}
	@Column(name = "EMERGENCY_CONTACT_ADDRESS", length = 20)		
	public String getEmergencyContactAddress() {
		return emergencyContactAddress;
	}

	public void setEmergencyContactAddress(String emergencyContactAddress) {
		this.emergencyContactAddress = emergencyContactAddress;
	}
	@Column(name = "EDUCATION_CERTIFICATE", length = 2)
	public String getEducationCertificate() {
		return educationCertificate;
	}

	public void setEducationCertificate(String educationCertificate) {
		this.educationCertificate = educationCertificate;
	}
	@Column(name = "GRADUATION_CERTIFICATE", length = 2)
	public String getGraduationCertificate() {
		return graduationCertificate;
	}

	public void setGraduationCertificate(String graduationCertificate) {
		this.graduationCertificate = graduationCertificate;
	}
	@Column(name = "ID_CARD_PIC", length = 50)
	public String getIdCardPic() {
		return idCardPic;
	}

	public void setIdCardPic(String idCardPic) {
		this.idCardPic = idCardPic;
	}
	@Column(name = "BANK_CARD_PIC", length = 50)
	public String getBankCardPic() {
		return bankCardPic;
	}

	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
	}
	@Column(name = "MEDICA_EXAMINATION_REPORT", length = 2)
	public String getMedicaExaminationReport() {
		return medicaExaminationReport;
	}

	public void setMedicaExaminationReport(String medicaExaminationReport) {
		this.medicaExaminationReport = medicaExaminationReport;
	}
	@Column(name = "INCH_PHOTOS", length = 2)
	public String getInchPhotos() {
		return inchPhotos;
	}

	public void setInchPhotos(String inchPhotos) {
		this.inchPhotos = inchPhotos;
	}
	@Column(name = "LEAVING_CERTIFICATE", length = 2)
	public String getLeavingCertificate() {
		return leavingCertificate;
	}

	public void setLeavingCertificate(String leavingCertificate) {
		this.leavingCertificate = leavingCertificate;
	}
	@Column(name = "LEAVES_REQUEST", length = 2)
	public String getLeavesRequest() {
		return leavesRequest;
	}

	public void setLeavesRequest(String leavesRequest) {
		this.leavesRequest = leavesRequest;
	}
	@Column(name = "USER_RESUME", length = 2)
	public String getUserResume() {
		return userResume;
	}

	public void setUserResume(String userResume) {
		this.userResume = userResume;
	}
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_organization", joinColumns = { @JoinColumn(name = "userID") }, inverseJoinColumns = { @JoinColumn(name = "organizationID") })
	public List<Organization> getOrganization() {
		return organization;
	}

	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}

	@Transient
	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}
	
	
		
}