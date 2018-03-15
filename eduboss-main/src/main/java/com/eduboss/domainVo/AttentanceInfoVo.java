package com.eduboss.domainVo;

import java.io.Serializable;

public class AttentanceInfoVo implements Serializable{

	private String studentId;	
	private String courseId;
	private String status;
	private Integer groupCode;
	
	public AttentanceInfoVo(){
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(Integer groupCode) {
		this.groupCode = groupCode;
	}
	
	
	
	
}
