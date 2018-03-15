package com.eduboss.dto;

import com.eduboss.common.MiniClassRelationType;

public class MiniClassRelationSearchVo {

	private String studentId;
	private MiniClassRelationType relationType;
	private String oldMiniClassId;
	private String newMiniClassId;
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public MiniClassRelationType getRelationType() {
		return relationType;
	}
	public void setRelationType(MiniClassRelationType relationType) {
		this.relationType = relationType;
	}
	public String getOldMiniClassId() {
		return oldMiniClassId;
	}
	public void setOldMiniClassId(String oldMiniClassId) {
		this.oldMiniClassId = oldMiniClassId;
	}
	public String getNewMiniClassId() {
		return newMiniClassId;
	}
	public void setNewMiniClassId(String newMiniClassId) {
		this.newMiniClassId = newMiniClassId;
	}
	
}
