package com.eduboss.domainVo;


public class RefSubjectGroupVo {

	private int id;
	private int subjectGroupId; // 科组Id
	private String subjectGroupName; // 科组名称
	private String subjectId; // 科目ID
	private String subjectName; // 科目名称
	private int version; // 版本
	private String teacherDes; // 下属老师
	private int moveToSubjectGroupId; // 转移到的科组Id
	private String blCampusId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSubjectGroupId() {
		return subjectGroupId;
	}
	public void setSubjectGroupId(int subjectGroupId) {
		this.subjectGroupId = subjectGroupId;
	}
	public String getSubjectGroupName() {
		return subjectGroupName;
	}
	public void setSubjectGroupName(String subjectGroupName) {
		this.subjectGroupName = subjectGroupName;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getTeacherDes() {
		return teacherDes;
	}
	public void setTeacherDes(String teacherDes) {
		this.teacherDes = teacherDes;
	}
	public int getMoveToSubjectGroupId() {
		return moveToSubjectGroupId;
	}
	public void setMoveToSubjectGroupId(int moveToSubjectGroupId) {
		this.moveToSubjectGroupId = moveToSubjectGroupId;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	
}
