package com.eduboss.domainVo;

public class PersonnelTransferVo {

	//Field
	private String id;
	private String userInfoId; //员工信息ID
	private String originalOrganizationId; //原所归属组织架构ID
	private String originalOrganizationName; //原所归属组织架构NAME
	private String originalDepartment; //原部门
	private String originalPosition; //原岗位
	private String transferOrganizationId; //调往归属组织架构ID
	private String transferOrganizationName; //调往归属组织架构NAME
	private String transferDepartment; //调往部门
	private String transferPosition; //调往岗位
	private String transferTime; //调往时间
	private String transferCause; //调往原因
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	public String getOriginalOrganizationId() {
		return originalOrganizationId;
	}
	public void setOriginalOrganizationId(String originalOrganizationId) {
		this.originalOrganizationId = originalOrganizationId;
	}
	public String getOriginalOrganizationName() {
		return originalOrganizationName;
	}
	public void setOriginalOrganizationName(String originalOrganizationName) {
		this.originalOrganizationName = originalOrganizationName;
	}
	public String getOriginalDepartment() {
		return originalDepartment;
	}
	public void setOriginalDepartment(String originalDepartment) {
		this.originalDepartment = originalDepartment;
	}
	public String getOriginalPosition() {
		return originalPosition;
	}
	public void setOriginalPosition(String originalPosition) {
		this.originalPosition = originalPosition;
	}
	public String getTransferOrganizationId() {
		return transferOrganizationId;
	}
	public void setTransferOrganizationId(String transferOrganizationId) {
		this.transferOrganizationId = transferOrganizationId;
	}
	public String getTransferOrganizationName() {
		return transferOrganizationName;
	}
	public void setTransferOrganizationName(String transferOrganizationName) {
		this.transferOrganizationName = transferOrganizationName;
	}
	public String getTransferDepartment() {
		return transferDepartment;
	}
	public void setTransferDepartment(String transferDepartment) {
		this.transferDepartment = transferDepartment;
	}
	public String getTransferPosition() {
		return transferPosition;
	}
	public void setTransferPosition(String transferPosition) {
		this.transferPosition = transferPosition;
	}
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	public String getTransferCause() {
		return transferCause;
	}
	public void setTransferCause(String transferCause) {
		this.transferCause = transferCause;
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
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	} 
	
}
