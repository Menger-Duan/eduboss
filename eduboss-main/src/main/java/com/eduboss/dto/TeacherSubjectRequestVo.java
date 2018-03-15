package com.eduboss.dto;

import java.util.Set;

public class TeacherSubjectRequestVo {
	
	private String teacherId;
	private String gradeId;
	private String subjectId;
	private Set<TeacherSubjectRequestVo> teachersrv = null;
	
	
	public TeacherSubjectRequestVo(){
		
	}
	
	public TeacherSubjectRequestVo(String teacherId, String gradeId,
			String subjectId) {
		super();
		this.teacherId = teacherId;
		this.gradeId = gradeId;
		this.subjectId = subjectId;
	}
	
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Set<TeacherSubjectRequestVo> getTeachersrv() {
		return teachersrv;
	}

	public void setTeachersrv(Set<TeacherSubjectRequestVo> teachersrv) {
		this.teachersrv = teachersrv;
	}

	
}
