package com.eduboss.domain;

import com.eduboss.common.UserType;
import com.eduboss.common.UserWorkType;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user")
//@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","organization","jobs","dept"})
public class User implements java.io.Serializable,NameValue {

	// Fields

	private String userId;
	private String name;
	private String account;
	private String password;
	private Integer enableFlg;
	private String organizationId;
	private String organizationName;   // 用于mobileUser 展示
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
	private String token;//登录令牌
	private Integer mailStatus;  //邮箱状态
	private String mailAddr; //企业邮箱地址
	
	
	private List<GrantedAuthority> authorities;
	private String orgLevelId;
	private String roleCode;
	private String roleName;  // 用于mobileUser 展示
	private String roleIds;//接收前端的角色列表Id
	private String orgIds;//接收前端的组织架构列表Id

	private String deptIds;//接收前端的部门列表Id
	private String jobIds;//接收前端的职位列表Id
	
	private Short roleLevel;//职位等级
	
	private String archivesPath;
	
	private String realName;//真实名字

	private List<Organization> organization = new ArrayList<Organization>();
	
//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "userID") }, inverseJoinColumns = { @JoinColumn(name = "roleID") })
	private List<Role> role = new ArrayList<Role>();
	
	private List<UserJob> jobs = new ArrayList<UserJob>();
	private List<Organization> dept = new ArrayList<Organization>();
	private String jobName;
	private String jobId;
	private String deptName;
	private String deptId;
	
	
	private String ccpAccount;//荣联帐号
	private String ccpPwd;//密码
	private Integer ccpStatus;//荣联帐号   0：有效，1：无效

	private String loginAddress; //访问服务器的地址 用于开通用户时的短信通知
	
	private String employeeNo;
	
	private int teacherSubjectStatus; // 是否编制老师  1：已编制，0待编制

	private UserType type;


	
	
	public User(String userId) {
		this.userId = userId;
	}

	// Constructors
	@Transient
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	
	@Transient
	public String getOrgLevelId() {
		return orgLevelId;
	}

	public void setOrgLevelId(String orgLevelId) {
		this.orgLevelId = orgLevelId;
	}
	
	@Transient
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	@Transient
	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	@Transient
	public String getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}

	
	@Transient
	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	@Transient
	public String getJobIds() {
		return jobIds;
	}

	public void setJobIds(String jobIds) {
		this.jobIds = jobIds;
	}

//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(name = "user_organization", joinColumns = { @JoinColumn(name = "userID") }, inverseJoinColumns = { @JoinColumn(name = "organizationID") })
	@Transient
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

	
	@Transient
	public List<UserJob> getJobs() {
		return jobs;
	}

	public void setJobs(List<UserJob> jobs) {
		this.jobs = jobs;
	}

	@Transient
	public List<Organization> getDept() {
		return dept;
	}

	public void setDept(List<Organization> dept) {
		this.dept = dept;
	}
	
	
	@Transient
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	@Transient
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	@Transient
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	@Transient
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Transient
	public String getLoginAddress() {
		return loginAddress;
	}

	public void setLoginAddress(String loginAddress) {
		this.loginAddress = loginAddress;
	}

	/** default constructor */
	public User() {
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "USER_ID", unique = true, nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ACCOUNT", length = 32)
	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@JsonIgnore
	@Column(name = "PSW", length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "ENABLE_FLAG")
	public Integer getEnableFlg() {
		return enableFlg;
	}

	public void setEnableFlg(Integer enableFlg) {
		this.enableFlg = enableFlg;
	}

	@Column(name = "organizationID", length = 32)
	public String getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@Column(name = "accountExpired")
	public Integer getAccountExpired() {
		return this.accountExpired;
	}

	public void setAccountExpired(Integer accountExpired) {
		this.accountExpired = accountExpired;
	}

	@Column(name = "accountLocked")
	public Integer getAccountLocked() {
		return this.accountLocked;
	}

	public void setAccountLocked(Integer accountLocked) {
		this.accountLocked = accountLocked;
	}

	@Column(name = "credentialsExpired")
	public Integer getCredentialsExpired() {
		return this.credentialsExpired;
	}

	public void setCredentialsExpired(Integer credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	@Column(name = "role_id", length = 32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name = "SEX", length = 4)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "CONTACT", length = 15)
	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "IDCORD_NO", length = 32)
	public String getIdcordNo() {
		return this.idcordNo;
	}

	public void setIdcordNo(String idcordNo) {
		this.idcordNo = idcordNo;
	}

	@Column(name = "SALARY_ACCOUNT", length = 32)
	public String getSalaryAccount() {
		return this.salaryAccount;
	}

	public void setSalaryAccount(String salaryAccount) {
		this.salaryAccount = salaryAccount;
	}

	@Column(name = "SALARY_ACCOUNT_BREND", length = 64)
	public String getSalaryAccountBrend() {
		return this.salaryAccountBrend;
	}

	public void setSalaryAccountBrend(String salaryAccountBrend) {
		this.salaryAccountBrend = salaryAccountBrend;
	}

	@Column(name = "ENTRY_DATE", length = 10)
	public String getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "BORTHDAY", length = 10)
	public String getBorthday() {
		return this.borthday;
	}

	public void setBorthday(String borthday) {
		this.borthday = borthday;
	}

	@Column(name = "AGE", precision = 3, scale = 0)
	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "SENIORITY", precision = 3, scale = 0)
	public Integer getSeniority() {
		return this.seniority;
	}

	public void setSeniority(Integer seniority) {
		this.seniority = seniority;
	}

	@Column(name = "TEACH_YEARS", precision = 3, scale = 0)
	public Integer getTeachYears() {
		return this.teachYears;
	}

	public void setTeachYears(Integer teachYears) {
		this.teachYears = teachYears;
	}

	@Column(name = "GRADUATE_SCHOOL", length = 64)
	public String getGraduateSchool() {
		return this.graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	@Column(name = "EDUCATION", length = 64)
	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@Column(name = "CERTIFICATION", length = 512)
	public String getCertification() {
		return this.certification;
	}

	public void setCertification(String certification) {
		this.certification = certification;
	}

	@Column(name = "EMENGENT_PERSON", length = 32)
	public String getEmengentPerson() {
		return this.emengentPerson;
	}

	public void setEmengentPerson(String emengentPerson) {
		this.emengentPerson = emengentPerson;
	}

	@Column(name = "EMENGENT_PERSON_CONTACT", length = 32)
	public String getEmengentPersonContact() {
		return this.emengentPersonContact;
	}

	public void setEmengentPersonContact(String emengentPersonContact) {
		this.emengentPersonContact = emengentPersonContact;
	}

	@Column(name = "ATTANCE_NO", length = 32)
	public String getAttanceNo() {
		return this.attanceNo;
	}

	public void setAttanceNo(String attanceNo) {
		this.attanceNo = attanceNo;
	}

	@Column(name = "RESIGN_DATE", length = 20)
	public String getResignDate() {
		return this.resignDate;
	}

	public void setResignDate(String resignDate) {
		this.resignDate = resignDate;
	}

	@Column(name = "RESIGN_RESULT", length = 512)
	public String getResignResult() {
		return this.resignResult;
	}

	public void setResignResult(String resignResult) {
		this.resignResult = resignResult;
	}
	
	@Transient
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return this.userId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "work_type", length = 32)
	public UserWorkType getWorkType() {
		return workType;
	}

	public void setWorkType(UserWorkType workType) {
		this.workType = workType;
	}
	
	@Column(name = "ADDRESS", length = 512)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
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

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Transient
	public Short getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(Short roleLevel) {
		this.roleLevel = roleLevel;
	}

	@Column(name = "USE_ALL_ORGANIZATION", length = 1)
	public String getUseAllOrganization() {
		return useAllOrganization;
	}

	public void setUseAllOrganization(String useAllOrganization) {
		this.useAllOrganization = useAllOrganization;
	}

	@Column(name = "ARCHIVES_PATH")
	public String getArchivesPath() {
		return archivesPath;
	}

	public void setArchivesPath(String archivesPath) {
		this.archivesPath = archivesPath;
	}

	@Transient
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Transient
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	@Column(name = "token", length = 40)
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	@Column(name = "MAIL_STATUS", length = 1)
	public Integer getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(Integer mailStatus) {
		this.mailStatus = mailStatus;
	}
	
	@Column(name = "MAIL_ADDR", length = 40)
	public String getMailAddr() {
		return mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	@Column(name = "CCP_ACCOUNT", length = 40)
	public String getCcpAccount() {
		return ccpAccount;
	}

	public void setCcpAccount(String ccpAccount) {
		this.ccpAccount = ccpAccount;
	}

	@Column(name = "CCP_PWD", length = 40)
	public String getCcpPwd() {
		return ccpPwd;
	}

	public void setCcpPwd(String ccpPwd) {
		this.ccpPwd = ccpPwd;
	}

	@Column(name = "REAL_NAME", length = 32)
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	@Column(name = "CCP_STATUS")
	public Integer getCcpStatus() {
		return ccpStatus;
	}

	public void setCcpStatus(Integer ccpStatus) {
		this.ccpStatus = ccpStatus;
	}

	@Column(name = "employee_No")
	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	@Column(name = "TEACHER_SUBJECT_STATUS")
	public int getTeacherSubjectStatus() {
		return teacherSubjectStatus;
	}

	public void setTeacherSubjectStatus(int teacherSubjectStatus) {
		this.teacherSubjectStatus = teacherSubjectStatus;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}
}