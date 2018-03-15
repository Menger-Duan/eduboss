package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;

import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;



public class UserInfoVo {

	// Fields
	private String Id;
	private String userId;
	private String userName;
	private String userSex;
	private String organizationId;
	private String roleId;
	private String department;
	private String userAccount;		
	private String birthDate;	
	private String nationalityId;
	private String nationalityName;  
	private String politicsStatusId;
	private String politicsStatusName;
	private String nativePlaceId;
	private String nativePlaceName;
	private String maritalStatusId;
	private String maritalStatusName;
	private String school;
	private String highestEducationId;
	private String highestEducationName;
	private String graduateTime;
	private String specialty;
	private String domicilePlace;
	private String idCard;
	private String idAddress;
	private String age;
	private String hiredate;
	private String startHiredate;
	private String endHiredate;
	private String conversionDate;
	private String startConversionDate;
	private String endConversionDate;
	private String contractTime;
	private String startContractTime;
	private String endContractTime;
	private String workTypeId;
	private String workTypeName;
	private String leaveDate;	
	private String startLeaveDate;
	private String endLeaveDate;	
	private String leaveTypeId;
	private String leaveTypeName;
	private String leaveCause;
	private String specialtyHobbies;	
	private String headPortrait;	
	private String companyWorkingYears;
	private String positioningId;
	private String positioningName;
	private String firstPrincipal;
	private String professionOneOne;
	private String professionMinclass;
	private String professionElite;
	private String professionOneN;
	private String workDate;
	private String workingYears;
	private String teacherStarId;
	private String teacherStarName;
	private String jobLevelId;
	private String jobLevelName;
	private String primarySchool;
	private String middleSchool;
	private String highSchool;
	private String workMachine;
	private String mobilePhone;
	private String homeTelephone;
	private String workEmail;	
	private String qq;
	private String presentAddress;	
	private String emergencyContact;
	private String relationship;	
	private String emergencyTelephone;	
	private String emergencyContactAddress;
	private String educationCertificate;
	private String graduationCertificate;
	private String idCardPic;
	private String bankCardPic;
	private String medicaExaminationReport;
	private String inchPhotos;
	private String leavingCertificate;
	private String leavesRequest;
	private String userResume;	
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	private List<Organization> organization = new ArrayList<Organization>();
	private List<Role> role = new ArrayList<Role>();
	
	public String getId() {
		return Id;
	}
	public void setId(String Id) {
		this.Id = Id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getNationalityId() {
		return nationalityId;
	}
	public void setNationalityId(String nationalityId) {
		this.nationalityId = nationalityId;
	}
	public String getNationalityName() {
		return nationalityName;
	}
	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}
	public String getPoliticsStatusId() {
		return politicsStatusId;
	}
	public void setPoliticsStatusId(String politicsStatusId) {
		this.politicsStatusId = politicsStatusId;
	}
	public String getPoliticsStatusName() {
		return politicsStatusName;
	}
	public void setPoliticsStatusName(String politicsStatusName) {
		this.politicsStatusName = politicsStatusName;
	}
	public String getNativePlaceId() {
		return nativePlaceId;
	}
	public void setNativePlaceId(String nativePlaceId) {
		this.nativePlaceId = nativePlaceId;
	}
	public String getNativePlaceName() {
		return nativePlaceName;
	}
	public void setNativePlaceName(String nativePlaceName) {
		this.nativePlaceName = nativePlaceName;
	}
	public String getMaritalStatusId() {
		return maritalStatusId;
	}
	public void setMaritalStatusId(String maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
	}
	public String getMaritalStatusName() {
		return maritalStatusName;
	}
	public void setMaritalStatusName(String maritalStatusName) {
		this.maritalStatusName = maritalStatusName;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getHighestEducationId() {
		return highestEducationId;
	}
	public void setHighestEducationId(String highestEducationId) {
		this.highestEducationId = highestEducationId;
	}
	public String getHighestEducationName() {
		return highestEducationName;
	}
	public void setHighestEducationName(String highestEducationName) {
		this.highestEducationName = highestEducationName;
	}
	public String getGraduateTime() {
		return graduateTime;
	}
	public void setGraduateTime(String graduateTime) {
		this.graduateTime = graduateTime;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getDomicilePlace() {
		return domicilePlace;
	}
	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getIdAddress() {
		return idAddress;
	}
	public void setIdAddress(String idAddress) {
		this.idAddress = idAddress;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getHiredate() {
		return hiredate;
	}
	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}
	public String getStartHiredate() {
		return startHiredate;
	}
	public void setStartHiredate(String startHiredate) {
		this.startHiredate = startHiredate;
	}
	public String getEndHiredate() {
		return endHiredate;
	}
	public void setEndHiredate(String endHiredate) {
		this.endHiredate = endHiredate;
	}
	public String getConversionDate() {
		return conversionDate;
	}
	public void setConversionDate(String conversionDate) {
		this.conversionDate = conversionDate;
	}
	public String getStartConversionDate() {
		return startConversionDate;
	}
	public void setStartConversionDate(String startConversionDate) {
		this.startConversionDate = startConversionDate;
	}
	public String getEndConversionDate() {
		return endConversionDate;
	}
	public void setEndConversionDate(String endConversionDate) {
		this.endConversionDate = endConversionDate;
	}
	public String getContractTime() {
		return contractTime;
	}
	public void setContractTime(String contractTime) {
		this.contractTime = contractTime;
	}
	public String getStartContractTime() {
		return startContractTime;
	}
	public void setStartContractTime(String startContractTime) {
		this.startContractTime = startContractTime;
	}
	public String getEndContractTime() {
		return endContractTime;
	}
	public void setEndContractTime(String endContractTime) {
		this.endContractTime = endContractTime;
	}
	public String getWorkTypeId() {
		return workTypeId;
	}
	public void setWorkTypeId(String workTypeId) {
		this.workTypeId = workTypeId;
	}
	public String getWorkTypeName() {
		return workTypeName;
	}
	public void setWorkTypeName(String workTypeName) {
		this.workTypeName = workTypeName;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getStartLeaveDate() {
		return startLeaveDate;
	}
	public void setStartLeaveDate(String startLeaveDate) {
		this.startLeaveDate = startLeaveDate;
	}
	public String getEndLeaveDate() {
		return endLeaveDate;
	}
	public void setEndLeaveDate(String endLeaveDate) {
		this.endLeaveDate = endLeaveDate;
	}
	public String getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(String leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveTypeName() {
		return leaveTypeName;
	}
	public void setLeaveTypeName(String leaveTypeName) {
		this.leaveTypeName = leaveTypeName;
	}
	public String getLeaveCause() {
		return leaveCause;
	}
	public void setLeaveCause(String leaveCause) {
		this.leaveCause = leaveCause;
	}
	public String getSpecialtyHobbies() {
		return specialtyHobbies;
	}
	public void setSpecialtyHobbies(String specialtyHobbies) {
		this.specialtyHobbies = specialtyHobbies;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getCompanyWorkingYears() {
		return companyWorkingYears;
	}
	public void setCompanyWorkingYears(String companyWorkingYears) {
		this.companyWorkingYears = companyWorkingYears;
	}
	public String getPositioningId() {
		return positioningId;
	}
	public void setPositioningId(String positioningId) {
		this.positioningId = positioningId;
	}
	public String getPositioningName() {
		return positioningName;
	}
	public void setPositioningName(String positioningName) {
		this.positioningName = positioningName;
	}
	public String getFirstPrincipal() {
		return firstPrincipal;
	}
	public void setFirstPrincipal(String firstPrincipal) {
		this.firstPrincipal = firstPrincipal;
	}
	public String getProfessionOneOne() {
		return professionOneOne;
	}
	public void setProfessionOneOne(String professionOneOne) {
		this.professionOneOne = professionOneOne;
	}
	public String getProfessionMinclass() {
		return professionMinclass;
	}
	public void setProfessionMinclass(String professionMinclass) {
		this.professionMinclass = professionMinclass;
	}
	public String getProfessionElite() {
		return professionElite;
	}
	public void setProfessionElite(String professionElite) {
		this.professionElite = professionElite;
	}
	public String getProfessionOneN() {
		return professionOneN;
	}
	public void setProfessionOneN(String professionOneN) {
		this.professionOneN = professionOneN;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getWorkingYears() {
		return workingYears;
	}
	public void setWorkingYears(String workingYears) {
		this.workingYears = workingYears;
	}
	public String getTeacherStarId() {
		return teacherStarId;
	}
	public void setTeacherStarId(String teacherStarId) {
		this.teacherStarId = teacherStarId;
	}
	public String getTeacherStarName() {
		return teacherStarName;
	}
	public void setTeacherStarName(String teacherStarName) {
		this.teacherStarName = teacherStarName;
	}
	public String getJobLevelId() {
		return jobLevelId;
	}
	public void setJobLevelId(String jobLevelId) {
		this.jobLevelId = jobLevelId;
	}
	public String getJobLevelName() {
		return jobLevelName;
	}
	public void setJobLevelName(String jobLevelName) {
		this.jobLevelName = jobLevelName;
	}
	public String getPrimarySchool() {
		return primarySchool;
	}
	public void setPrimarySchool(String primarySchool) {
		this.primarySchool = primarySchool;
	}
	public String getMiddleSchool() {
		return middleSchool;
	}
	public void setMiddleSchool(String middleSchool) {
		this.middleSchool = middleSchool;
	}
	public String getHighSchool() {
		return highSchool;
	}
	public void setHighSchool(String highSchool) {
		this.highSchool = highSchool;
	}
	public String getWorkMachine() {
		return workMachine;
	}
	public void setWorkMachine(String workMachine) {
		this.workMachine = workMachine;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getHomeTelephone() {
		return homeTelephone;
	}
	public void setHomeTelephone(String homeTelephone) {
		this.homeTelephone = homeTelephone;
	}
	public String getWorkEmail() {
		return workEmail;
	}
	public void setWorkEmail(String workEmail) {
		this.workEmail = workEmail;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getPresentAddress() {
		return presentAddress;
	}
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}
	public String getEmergencyContact() {
		return emergencyContact;
	}
	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public String getEmergencyTelephone() {
		return emergencyTelephone;
	}
	public void setEmergencyTelephone(String emergencyTelephone) {
		this.emergencyTelephone = emergencyTelephone;
	}
	public String getEmergencyContactAddress() {
		return emergencyContactAddress;
	}
	public void setEmergencyContactAddress(String emergencyContactAddress) {
		this.emergencyContactAddress = emergencyContactAddress;
	}
	public String getEducationCertificate() {
		return educationCertificate;
	}
	public void setEducationCertificate(String educationCertificate) {
		this.educationCertificate = educationCertificate;
	}
	public String getGraduationCertificate() {
		return graduationCertificate;
	}
	public void setGraduationCertificate(String graduationCertificate) {
		this.graduationCertificate = graduationCertificate;
	}
	public String getIdCardPic() {
		return idCardPic;
	}
	public void setIdCardPic(String idCardPic) {
		this.idCardPic = idCardPic;
	}
	public String getBankCardPic() {
		return bankCardPic;
	}
	public void setBankCardPic(String bankCardPic) {
		this.bankCardPic = bankCardPic;
	}
	public String getMedicaExaminationReport() {
		return medicaExaminationReport;
	}
	public void setMedicaExaminationReport(String medicaExaminationReport) {
		this.medicaExaminationReport = medicaExaminationReport;
	}
	public String getInchPhotos() {
		return inchPhotos;
	}
	public void setInchPhotos(String inchPhotos) {
		this.inchPhotos = inchPhotos;
	}
	public String getLeavingCertificate() {
		return leavingCertificate;
	}
	public void setLeavingCertificate(String leavingCertificate) {
		this.leavingCertificate = leavingCertificate;
	}
	public String getLeavesRequest() {
		return leavesRequest;
	}
	public void setLeavesRequest(String leavesRequest) {
		this.leavesRequest = leavesRequest;
	}
	public String getUserResume() {
		return userResume;
	}
	public void setUserResume(String userResume) {
		this.userResume = userResume;
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
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public List<Organization> getOrganization() {
		return organization;
	}
	public void setOrganization(List<Organization> organization) {
		this.organization = organization;
	}
	public List<Role> getRole() {
		return role;
	}
	public void setRole(List<Role> role) {
		this.role = role;
	}
		
}