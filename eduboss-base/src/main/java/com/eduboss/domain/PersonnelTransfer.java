package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author lixuejun
 * 人事调动表
 */
@Entity
@Table(name = "user_info_personnel_transfer")
public class PersonnelTransfer {

	private String id;
	private String userInfoId; //员工信息ID
	private Organization originalOrganization; //原所归属组织架构
	private String originalDepartment; //原部门
	private String originalPosition; //原岗位
	private Organization transferOrganization; //调往归属组织架构
	private String transferDepartment; //调往部门
	private String transferPosition; //调往岗位
	private String transferTime; //调往时间
	private String transferCause; //调往原因
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime; 
	
	/** default constructor */
	public PersonnelTransfer() {
	}
	
	public PersonnelTransfer(String id) {
		this.id = id;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "USER_INFO_ID", length = 32)
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ORIGINAL_ORGANIZATION_ID")
	public Organization getOriginalOrganization() {
		return originalOrganization;
	}
	public void setOriginalOrganization(Organization originalOrganization) {
		this.originalOrganization = originalOrganization;
	}
	
	@Column(name = "ORIGINAL_DEPARTMENT", length = 20)
	public String getOriginalDepartment() {
		return originalDepartment;
	}
	public void setOriginalDepartment(String originalDepartment) {
		this.originalDepartment = originalDepartment;
	}
	
	@Column(name = "ORIGINAL_POSITION", length = 20)
	public String getOriginalPosition() {
		return originalPosition;
	}
	public void setOriginalPosition(String originalPosition) {
		this.originalPosition = originalPosition;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRANSFER_ORGANIZATION_ID")
	public Organization getTransferOrganization() {
		return transferOrganization;
	}
	public void setTransferOrganization(Organization transferOrganization) {
		this.transferOrganization = transferOrganization;
	}
	
	@Column(name = "TRANSFER_DEPARTMENT", length = 20)
	public String getTransferDepartment() {
		return transferDepartment;
	}
	public void setTransferDepartment(String transferDepartment) {
		this.transferDepartment = transferDepartment;
	}
	
	@Column(name = "TRANSFER_POSITION", length = 20)
	public String getTransferPosition() {
		return transferPosition;
	}
	public void setTransferPosition(String transferPosition) {
		this.transferPosition = transferPosition;
	}
	
	@Column(name = "TRANSFER_TIME", length = 32)
	public String getTransferTime() {
		return transferTime;
	}
	public void setTransferTime(String transferTime) {
		this.transferTime = transferTime;
	}
	
	@Column(name = "TRANSFER_CAUSE", length = 100)
	public String getTransferCause() {
		return transferCause;
	}
	public void setTransferCause(String transferCause) {
		this.transferCause = transferCause;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
