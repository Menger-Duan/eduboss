package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.eduboss.common.FunctionsLevel;
import com.eduboss.common.PreparationType;
import com.eduboss.common.TeacherFunctions;
import com.eduboss.common.TeacherLevel;
import com.eduboss.common.TeacherType;

/**
 * 2016-12-15 授课老师
 * @author lixuejun
 *
 */
@Entity
@Table(name = "teacher_version")
public class TeacherVersion {

	private int id;
	private User teacher; // 关联user
	private String versionDate; // 版本日期
	private int isCurrentVersion; // 1：当前生效版本，0：非当前生效版本
	private String jobNumber; // 工号
	private PreparationType preparationType; // 所属编制
	private TeacherType teacherType; // 老师类型
	private TeacherLevel teacherLevel; // 教师级别
	private Set<TeacherFunctionVersion> teacherFunctions; // 关联的职能岗位
	private TeacherFunctions mainTeacherFunctions; // 主职能岗位
	private FunctionsLevel mainFunctionsLevel; // 主职能级别
	private Organization blCampus; // 所属校区
	private Set<TeacherSubjectVersion> teacherSubjectVersions; // 关联的授课老师科目
	private DataDict mainSubject; // 主科目ID
	private DataDict mainGrade; // 主年级ID
	private String subjectDes; // 授课科目
	private String gradeDes; // 授课年级
	private String teacherFunctionDes; // 职能岗位
	private String functionsLevelDes; // 职能级别
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public TeacherVersion() {
		super();
	}

	public TeacherVersion(int id) {
		super();
		this.id = id;
	}

	public TeacherVersion(int id, User teacher, String versionDate,
			int isCurrentVersion, String jobNumber,
			PreparationType preparationType, TeacherType teacherType,
			TeacherLevel teacherLevel,
			Set<TeacherFunctionVersion> teacherFunctions,
			TeacherFunctions mainTeacherFunctions,
			FunctionsLevel mainFunctionsLevel, Organization blCampus,
			Set<TeacherSubjectVersion> teacherSubjectVersions,
			DataDict mainSubject, DataDict mainGrade, String subjectDes,
			String gradeDes, String teacherFunctionDes, String functionsLevelDes, 
			String createTime, String createUserId, String modifyTime, String modifyUserId) {
		super();
		this.id = id;
		this.teacher = teacher;
		this.versionDate = versionDate;
		this.isCurrentVersion = isCurrentVersion;
		this.jobNumber = jobNumber;
		this.preparationType = preparationType;
		this.teacherType = teacherType;
		this.teacherLevel = teacherLevel;
		this.teacherFunctions = teacherFunctions;
		this.mainTeacherFunctions = mainTeacherFunctions;
		this.mainFunctionsLevel = mainFunctionsLevel;
		this.blCampus = blCampus;
		this.teacherSubjectVersions = teacherSubjectVersions;
		this.mainSubject = mainSubject;
		this.mainGrade = mainGrade;
		this.subjectDes = subjectDes;
		this.gradeDes = gradeDes;
		this.teacherFunctionDes = teacherFunctionDes;
		this.functionsLevelDes =functionsLevelDes;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "VERSION_DATE", length = 10)
	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

	@Column(name = "IS_CURRENT_VERSION", length = 1)
	public int getIsCurrentVersion() {
		return isCurrentVersion;
	}

	public void setIsCurrentVersion(int isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}

	@Column(name = "JOB_NUMBER", length = 30)
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PREPARATION_TYPE", length = 32)
	public PreparationType getPreparationType() {
		return preparationType;
	}

	public void setPreparationType(PreparationType preparationType) {
		this.preparationType = preparationType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TEACHER_TYPE", length = 32)
	public TeacherType getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(TeacherType teacherType) {
		this.teacherType = teacherType;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TEACHER_LEVEL", length = 32)
	public TeacherLevel getTeacherLevel() {
		return teacherLevel;
	}

	public void setTeacherLevel(TeacherLevel teacherLevel) {
		this.teacherLevel = teacherLevel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacherVersion")
	@OrderBy("id ASC")
	public Set<TeacherFunctionVersion> getTeacherFunctions() {
		return teacherFunctions;
	}

	public void setTeacherFunctions(Set<TeacherFunctionVersion> teacherFunctions) {
		this.teacherFunctions = teacherFunctions;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MAIN_TEACHER_FUNCTIONS", length = 32)
	public TeacherFunctions getMainTeacherFunctions() {
		return mainTeacherFunctions;
	}

	public void setMainTeacherFunctions(TeacherFunctions mainTeacherFunctions) {
		this.mainTeacherFunctions = mainTeacherFunctions;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MAIN_FUNCTIONS_LEVEL", length = 32)
	public FunctionsLevel getMainFunctionsLevel() {
		return mainFunctionsLevel;
	}

	public void setMainFunctionsLevel(FunctionsLevel mainFunctionsLevel) {
		this.mainFunctionsLevel = mainFunctionsLevel;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "teacherVersion")
	@OrderBy("id ASC")
	public Set<TeacherSubjectVersion> getTeacherSubjectVersions() {
		return teacherSubjectVersions;
	}

	public void setTeacherSubjectVersions(
			Set<TeacherSubjectVersion> teacherSubjectVersions) {
		this.teacherSubjectVersions = teacherSubjectVersions;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_SUBJECT_ID")
	public DataDict getMainSubject() {
		return mainSubject;
	}

	public void setMainSubject(DataDict mainSubject) {
		this.mainSubject = mainSubject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_GRADE_ID")
	public DataDict getMainGrade() {
		return mainGrade;
	}

	public void setMainGrade(DataDict mainGrade) {
		this.mainGrade = mainGrade;
	}

	@Column(name = "SUBJECT_DES", length = 500)
	public String getSubjectDes() {
		return subjectDes;
	}

	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}

	@Column(name = "GRADE_DES", length = 500)
	public String getGradeDes() {
		return gradeDes;
	}

	public void setGradeDes(String gradeDes) {
		this.gradeDes = gradeDes;
	}

	@Column(name = "TEACHER_FUNCTION_DES", length = 500)
	public String getTeacherFunctionDes() {
		return teacherFunctionDes;
	}

	public void setTeacherFunctionDes(String teacherFunctionDes) {
		this.teacherFunctionDes = teacherFunctionDes;
	}
	
	@Column(name = "FUNCTIONS_LEVEL_DES", length = 500)
	public String getFunctionsLevelDes() {
		return functionsLevelDes;
	}

	public void setFunctionsLevelDes(String functionsLevelDes) {
		this.functionsLevelDes = functionsLevelDes;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
