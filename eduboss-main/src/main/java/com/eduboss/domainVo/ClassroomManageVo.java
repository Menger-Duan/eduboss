package com.eduboss.domainVo;

import java.math.BigDecimal;

public class ClassroomManageVo {

private String id;
	
	/* 教室名称*/
	private String classroom;
	
	/* 组织机构（校区）*/
	private String organizationId;
	private String organizationName;
	
	/* 教室类型*/
	private String classTypeId;
	private String classTypeName;
	
	/* 教室面积（平方米）*/
	private BigDecimal classArea;
	
	/* 适用人数*/
	private int classMember;
	
	/* 教室设备（数据字典中ID的组合字段，用逗号隔开）*/
	private String classEquitment;
	
	/* 备注*/
	private String remark;
	
	/* 状态：0 无效 1 有效*/
	private int status;
	private String statusName;
	
	/* 创建人*/
	private String creatorId;
	private String creatorName;
	
	/* 创建时间*/
	private String createTime;
	
	/* 修改人*/
	private String modifierId;
	private String modifierName;
	
	/* 修改时间*/
	private String modifyTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public BigDecimal getClassArea() {
		return classArea;
	}

	public void setClassArea(BigDecimal classArea) {
		this.classArea = classArea;
	}

	public int getClassMember() {
		return classMember;
	}

	public void setClassMember(int classMember) {
		this.classMember = classMember;
	}

	public String getClassEquitment() {
		return classEquitment;
	}

	public void setClassEquitment(String classEquitment) {
		this.classEquitment = classEquitment;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifierId() {
		return modifierId;
	}

	public void setModifierId(String modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	
}

