package com.eduboss.domainVo;

import java.util.Set;

public class SubjectGroupVo {

	private int id;
	private String nameId; // 名称Id
	private String nameName; // 名称Name
	private String blBrenchId; // 所属分公司ID
	private String blBrenchName; // 所属分公司名称
	private String blCampusId; // 所属校区ID
	private String blCampusName; // 所属校区名称
	private int version; // 版本
	private int rorder; // 排序
	private Set<RefSubjectGroupVo> refSubjectGroups;
	private String subjectDes; // 下属科目
	private String teacherDes; // 下属老师
	private Set<RefSubjectGroupVo> moveRefSubjectGroups;
	private int isUpToNextMonth;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameId() {
		return nameId;
	}
	public void setNameId(String nameId) {
		this.nameId = nameId;
	}
	public String getNameName() {
		return nameName;
	}
	public void setNameName(String nameName) {
		this.nameName = nameName;
	}
	public String getBlBrenchId() {
		return blBrenchId;
	}
	public void setBlBrenchId(String blBrenchId) {
		this.blBrenchId = blBrenchId;
	}
	public String getBlBrenchName() {
		return blBrenchName;
	}
	public void setBlBrenchName(String blBrenchName) {
		this.blBrenchName = blBrenchName;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getRorder() {
		return rorder;
	}
	public void setRorder(int rorder) {
		this.rorder = rorder;
	}
	public Set<RefSubjectGroupVo> getRefSubjectGroups() {
		return refSubjectGroups;
	}
	public void setRefSubjectGroups(Set<RefSubjectGroupVo> refSubjectGroups) {
		this.refSubjectGroups = refSubjectGroups;
	}
	public String getSubjectDes() {
		return subjectDes;
	}
	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}
	public String getTeacherDes() {
		return teacherDes;
	}
	public void setTeacherDes(String teacherDes) {
		this.teacherDes = teacherDes;
	}
	public Set<RefSubjectGroupVo> getMoveRefSubjectGroups() {
		return moveRefSubjectGroups;
	}
	public void setMoveRefSubjectGroups(Set<RefSubjectGroupVo> moveRefSubjectGroups) {
		this.moveRefSubjectGroups = moveRefSubjectGroups;
	}
	public int getIsUpToNextMonth() {
		return isUpToNextMonth;
	}
	public void setIsUpToNextMonth(int isUpToNextMonth) {
		this.isUpToNextMonth = isUpToNextMonth;
	}
	
}
