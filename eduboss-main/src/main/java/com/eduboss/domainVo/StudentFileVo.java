package com.eduboss.domainVo;

import com.eduboss.common.StudentFileType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class StudentFileVo {

	private String id;
	
	private StudentFileType studentFileType; //档案类型
	
	private String studentFileTypeName;
	
	private String studentFileTypeValue;
	
	/* 描述*/
	private String docDescription;	
	
	/* 创建者ID*/
	private String creatorId;
	
	/* 创建者名称*/
	private String creatorName;
	
	/* 学生ID*/
	private String studentId;
	
	/* 学生名称*/
	private String studentName;
	
	/* 提交时间*/
	private String createTime;

	private String realPath;

	private String fileName; //文件名

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StudentFileType getStudentFileType() {
		return studentFileType;
	}

	public void setStudentFileType(StudentFileType studentFileType) {
		this.studentFileType = studentFileType;
	}

	public String getStudentFileTypeName() {
		return studentFileTypeName;
	}

	public void setStudentFileTypeName(String studentFileTypeName) {
		this.studentFileTypeName = studentFileTypeName;
	}

	public String getStudentFileTypeValue() {
		return studentFileTypeValue;
	}

	public void setStudentFileTypeValue(String studentFileTypeValue) {
		this.studentFileTypeValue = studentFileTypeValue;
	}	

	public String getDocDescription() {
		return docDescription;
	}

	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRealPath() {
		return realPath;
	}

	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
