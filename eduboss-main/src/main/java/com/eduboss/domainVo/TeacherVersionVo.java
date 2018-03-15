package com.eduboss.domainVo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eduboss.common.FunctionsLevel;
import com.eduboss.common.PreparationType;
import com.eduboss.common.TeacherFunctions;
import com.eduboss.common.TeacherLevel;
import com.eduboss.common.TeacherType;

public class TeacherVersionVo {

	private int id;
	private String teacherId; // 老师ID
	private String teacherName; // 老师名称
	private Integer enableFlg;
	private String sex; // 性别
	private String birthDay; // 生日
	private int age;
	private String entryDate; // 入职日期
	private int workingYears;
	private String contact; // 联系方式
	private String versionDate; // 版本日期
	private int isCurrentVersion; // 1：当前生效版本，2：非当前生效版本
	private String jobNumber; // 工号
	private String workType;
	private PreparationType preparationType; // 所属编制
	private TeacherType teacherType; // 老师类型
	private TeacherLevel teacherLevel; // 教师级别
	private Set<TeacherFunctionVersionVo> teacherFunctions; // 关联的职能岗位
	private TeacherFunctions mainTeacherFunctions; // 主职能岗位
	private FunctionsLevel mainFunctionsLevel; // 主职能级别
	private String blCampusId; // 所属校区ID
	private String blCampusName; // 所属校区NAME
	private Set<TeacherSubjectVersionVo> teacherSubjectVersions; // 关联的授课老师科目
	private String mainSubjectId; // 主科目ID
	private String mainSubjectName; // 主科目名称
	private String mainGradeId; // 主年级ID
	private String mainGradeName; // 主年级名称
	private String subjectDes; // 授课科目
	private String gradeDes; // 授课年级
	private String teacherFunctionDes; // 职能岗位
	private String functionsLevelDes; // 职能级别
	
	private List<Map<String, Object>> versions; //所有版本
	
	private String subjectId; // 科目ID 传参 
	private String gradeId; // 年级ID 传参
	private String subjectGroupId; //科组ID 传参
	private TeacherFunctions searchTeacherFunctions;
	private FunctionsLevel searchFunctionsLevel;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
    public Integer getEnableFlg() {
        return enableFlg;
    }
    public void setEnableFlg(Integer enableFlg) {
        this.enableFlg = enableFlg;
    }
    public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}
	public int getIsCurrentVersion() {
		return isCurrentVersion;
	}
	public void setIsCurrentVersion(int isCurrentVersion) {
		this.isCurrentVersion = isCurrentVersion;
	}
	public String getJobNumber() {
		return jobNumber;
	}
	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	public PreparationType getPreparationType() {
		return preparationType;
	}
	public void setPreparationType(PreparationType preparationType) {
		this.preparationType = preparationType;
	}
	public TeacherType getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(TeacherType teacherType) {
		this.teacherType = teacherType;
	}
	public TeacherLevel getTeacherLevel() {
		return teacherLevel;
	}
	public void setTeacherLevel(TeacherLevel teacherLevel) {
		this.teacherLevel = teacherLevel;
	}
	public Set<TeacherFunctionVersionVo> getTeacherFunctions() {
		return teacherFunctions;
	}
	public void setTeacherFunctions(Set<TeacherFunctionVersionVo> teacherFunctions) {
		this.teacherFunctions = teacherFunctions;
	}
	public TeacherFunctions getMainTeacherFunctions() {
		return mainTeacherFunctions;
	}
	public void setMainTeacherFunctions(TeacherFunctions mainTeacherFunctions) {
		this.mainTeacherFunctions = mainTeacherFunctions;
	}
	public FunctionsLevel getMainFunctionsLevel() {
		return mainFunctionsLevel;
	}
	public void setMainFunctionsLevel(FunctionsLevel mainFunctionsLevel) {
		this.mainFunctionsLevel = mainFunctionsLevel;
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
	public Set<TeacherSubjectVersionVo> getTeacherSubjectVersions() {
		return teacherSubjectVersions;
	}
	public void setTeacherSubjectVersions(
			Set<TeacherSubjectVersionVo> teacherSubjectVersions) {
		this.teacherSubjectVersions = teacherSubjectVersions;
	}
	public String getMainSubjectId() {
		return mainSubjectId;
	}
	public void setMainSubjectId(String mainSubjectId) {
		this.mainSubjectId = mainSubjectId;
	}
	public String getMainSubjectName() {
		return mainSubjectName;
	}
	public void setMainSubjectName(String mainSubjectName) {
		this.mainSubjectName = mainSubjectName;
	}
	public String getMainGradeId() {
		return mainGradeId;
	}
	public void setMainGradeId(String mainGradeId) {
		this.mainGradeId = mainGradeId;
	}
	public String getMainGradeName() {
		return mainGradeName;
	}
	public void setMainGradeName(String mainGradeName) {
		this.mainGradeName = mainGradeName;
	}
	public String getSubjectDes() {
		return subjectDes;
	}
	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}
	public String getGradeDes() {
		return gradeDes;
	}
	public void setGradeDes(String gradeDes) {
		this.gradeDes = gradeDes;
	}
	public String getTeacherFunctionDes() {
		return teacherFunctionDes;
	}
	public void setTeacherFunctionDes(String teacherFunctionDes) {
		this.teacherFunctionDes = teacherFunctionDes;
	}
	public String getFunctionsLevelDes() {
		return functionsLevelDes;
	}
	public void setFunctionsLevelDes(String functionsLevelDes) {
		this.functionsLevelDes = functionsLevelDes;
	}
	public List<Map<String, Object>> getVersions() {
		return versions;
	}
	public void setVersions(List<Map<String, Object>> versions) {
		this.versions = versions;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getSubjectGroupId() {
		return subjectGroupId;
	}
	public void setSubjectGroupId(String subjectGroupId) {
		this.subjectGroupId = subjectGroupId;
	}
	public TeacherFunctions getSearchTeacherFunctions() {
		return searchTeacherFunctions;
	}
	public void setSearchTeacherFunctions(TeacherFunctions searchTeacherFunctions) {
		this.searchTeacherFunctions = searchTeacherFunctions;
	}
	public FunctionsLevel getSearchFunctionsLevel() {
		return searchFunctionsLevel;
	}
	public void setSearchFunctionsLevel(FunctionsLevel searchFunctionsLevel) {
		this.searchFunctionsLevel = searchFunctionsLevel;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getWorkingYears() {
		return workingYears;
	}
	public void setWorkingYears(int workingYears) {
		this.workingYears = workingYears;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	
}
