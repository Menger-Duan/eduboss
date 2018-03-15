package com.eduboss.domainVo;


public class StudentDocumentVo {
	
	private String id;
	private String studentId;
	private String studentName;
	private String documentPath;
	private String documentType;
	private String documentTypeName;
	private String createUserId;
	private String createTime;
	private String documentRealPath;
	private String aliName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	public String getDocumentRealPath() {
		return documentRealPath;
	}
	public void setDocumentRealPath(String documentRealPath) {
		this.documentRealPath = documentRealPath;
	}
	public String getAliName() {
		return aliName;
	}
	public void setAliName(String aliName) {
		this.aliName = aliName;
	}

}
