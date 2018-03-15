package com.eduboss.domainVo;


public class TeacherSubjectVersionVo {

	private int id;
	private int teacherVersionId; // 授课老师ID
	private String subjectId; // 科目ID
	private String subjectName; // 科目名称
	private String gradeId; // 年级ID
	private String gradeName; // 年级名称
	private String teacherId; // 关联的用户ID
	private String teacherName; // 关联的用户名称
	private String versionDate; // 版本日期
	private int versionMonth; // 月份版本
	private int isMonthVersion; // 1：月份版本，0：非月份版本
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTeacherVersionId() {
		return teacherVersionId;
	}
	public void setTeacherVersionId(int teacherVersionId) {
		this.teacherVersionId = teacherVersionId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
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
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}
	public int getVersionMonth() {
		return versionMonth;
	}
	public void setVersionMonth(int versionMonth) {
		this.versionMonth = versionMonth;
	}
	public int getIsMonthVersion() {
		return isMonthVersion;
	}
	public void setIsMonthVersion(int isMonthVersion) {
		this.isMonthVersion = isMonthVersion;
	}
	
}
