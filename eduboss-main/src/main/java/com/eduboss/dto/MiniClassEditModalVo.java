package com.eduboss.dto;

public class MiniClassEditModalVo {
	private String miniClassId;
	private String classTime;
	private int modalId;
	private String classRoomId;
	private String teacherId;
	private String classEndTime;

	public String getClassEndTime() {
		return classEndTime;
	}

	public void setClassEndTime(String classEndTime) {
		this.classEndTime = classEndTime;
	}

	public String getClassRoomId() {
		return classRoomId;
	}

	public void setClassRoomId(String classRoomId) {
		this.classRoomId = classRoomId;
	}

	public String getMiniClassId() {
		return miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public int getModalId() {
		return modalId;
	}

	public void setModalId(int modalId) {
		this.modalId = modalId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
}
