package com.eduboss.domainVo;

import com.eduboss.common.MiniClassRelationType;

public class ContinueExtendMiniClassVo {

	private String miniClassId;
	private String studentId;
	private String miniClassName;
	private String startDate;
	private String teacherName;
	private String studyManegerName;
	private MiniClassRelationType relationType;
	
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getMiniClassName() {
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudyManegerName() {
		return studyManegerName;
	}
	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
	}
	public MiniClassRelationType getRelationType() {
		return relationType;
	}
	public void setRelationType(MiniClassRelationType relationType) {
		this.relationType = relationType;
	}
	
}
