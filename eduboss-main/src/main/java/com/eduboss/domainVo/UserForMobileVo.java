package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.eduboss.common.UserWorkType;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;

public class UserForMobileVo {

	// Fields
	private String userId;
	private String name;
	private String account;
	private String password;
	private Integer enableFlg;
	private String organizationId;
	private Integer accountExpired;
	private Integer accountLocked;
	private Integer credentialsExpired;
	private String roleId;
	private String sex;
	private String contact;
	private String idcordNo;
	private String salaryAccount;
	private String salaryAccountBrend;
	private String entryDate;
	private String borthday;
	private Integer age;
	private Integer seniority;
	private Integer teachYears;
	private String graduateSchool;
	private String education;
	private String certification;
	private String emengentPerson;
	private String emengentPersonContact;
	private String attanceNo;
	private String resignDate;
	private String resignResult;
	private UserWorkType workType;
	private String address;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String useAllOrganization;

	private List<GrantedAuthority> authorities;
	private String orgLevelId;
	private String roleCode;
	private String roleIds;// 接收前端的角色列表Id
	private String orgIds;// 接收前端的组织架构列表Id

	private Short roleLevel;// 职位等级

	private String archivesPath;

	private List<Organization> organization = new ArrayList<Organization>();

	private List<Role> role = new ArrayList<Role>();

	private String mobileUserId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getEnableFlg() {
		return enableFlg;
	}

	public void setEnableFlg(Integer enableFlg) {
		this.enableFlg = enableFlg;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getAccountExpired() {
		return accountExpired;
	}

	public void setAccountExpired(Integer accountExpired) {
		this.accountExpired = accountExpired;
	}

	public Integer getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Integer accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Integer getCredentialsExpired() {
		return credentialsExpired;
	}

	public void setCredentialsExpired(Integer credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getIdcordNo() {
		return idcordNo;
	}

	public void setIdcordNo(String idcordNo) {
		this.idcordNo = idcordNo;
	}

	public String getSalaryAccount() {
		return salaryAccount;
	}

	public void setSalaryAccount(String salaryAccount) {
		this.salaryAccount = salaryAccount;
	}

	public String getSalaryAccountBrend() {
		return salaryAccountBrend;
	}

	public void setSalaryAccountBrend(String salaryAccountBrend) {
		this.salaryAccountBrend = salaryAccountBrend;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getBorthday() {
		return borthday;
	}

	public void setBorthday(String borthday) {
		this.borthday = borthday;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSeniority() {
		return seniority;
	}

	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}

	public Integer getTeachYears() {
		return teachYears;
	}

	public void setTeachYears(Integer teachYears) {
		this.teachYears = teachYears;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getCertification() {
		return certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	public String getEmengentPerson() {
		return emengentPerson;
	}

	public void setEmengentPerson(String emengentPerson) {
		this.emengentPerson = emengentPerson;
	}

	public String getEmengentPersonContact() {
		return emengentPersonContact;
	}

	public void setEmengentPersonContact(String emengentPersonContact) {
		this.emengentPersonContact = emengentPersonContact;
	}

	public String getAttanceNo() {
		return attanceNo;
	}

	public void setAttanceNo(String attanceNo) {
		this.attanceNo = attanceNo;
	}

	public String getResignDate() {
		return resignDate;
	}

	public void setResignDate(String resignDate) {
		this.resignDate = resignDate;
	}

	public String getResignResult() {
		return resignResult;
	}

	public void setResignResult(String resignResult) {
		this.resignResult = resignResult;
	}

	public UserWorkType getWorkType() {
		return workType;
	}

	public void setWorkType(UserWorkType workType) {
		this.workType = workType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getUseAllOrganization() {
		return useAllOrganization;
	}

	public void setUseAllOrganization(String useAllOrganization) {
		this.useAllOrganization = useAllOrganization;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public String getOrgLevelId() {
		return orgLevelId;
	}

	public void setOrgLevelId(String orgLevelId) {
		this.orgLevelId = orgLevelId;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	public Short getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Short roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getArchivesPath() {
		return archivesPath;
	}

	public void setArchivesPath(String archivesPath) {
		this.archivesPath = archivesPath;
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

	public String getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(String mobileUserId) {
		this.mobileUserId = mobileUserId;
	}
	
}
