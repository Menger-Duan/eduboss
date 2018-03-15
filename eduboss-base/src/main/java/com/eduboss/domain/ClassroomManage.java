package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CLASSROOM_MANAGE")
public class ClassroomManage {

	private String id;
	
	/* 教室名称*/
	private String classroom;
	
	/* 组织机构（校区）*/
	private Organization organization;
	
	/* 教室类型*/
	private DataDict classType;
	
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
	
	/* 创建人*/
	private User creator;
	
	/* 创建时间*/
	private String createTime;
	
	/* 修改人*/
	private User modifier;
	
	/* 修改时间*/
	private String modifyTime;

	public ClassroomManage() {
	}

	public ClassroomManage(String id) {
		this.id = id;
	}

	/*********************************************************************************************/
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

	@Column(name = "CLASS_ROOM")
	public String getClassroom() {
		return classroom;
	}

	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}

	@JoinColumn(name = "ORGANIZATION_ID")
	@ManyToOne(fetch = FetchType.LAZY)	
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@JoinColumn(name = "CLASS_TYPE")
	@ManyToOne(fetch = FetchType.LAZY)
	public DataDict getClassType() {
		return classType;
	}

	public void setClassType(DataDict classType) {
		this.classType = classType;
	}

	@Column(name = "CLASS_AREA", precision = 10)
	public BigDecimal getClassArea() {
		return classArea;
	}

	public void setClassArea(BigDecimal classArea) {
		this.classArea = classArea;
	}

	@Column(name = "CLASS_MEMBER")
	public int getClassMember() {
		return classMember;
	}

	public void setClassMember(int classMember) {
		this.classMember = classMember;
	}

	@Column(name = "CLASS_EQUITMENT")
	public String getClassEquitment() {
		return classEquitment;
	}

	public void setClassEquitment(String classEquitment) {
		this.classEquitment = classEquitment;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@JoinColumn(name = "CREATE_USER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	@Column(name = "CREATE_TIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@JoinColumn(name = "MODIFY_USER_ID")
	@ManyToOne(fetch = FetchType.LAZY)
	public User getModifier() {
		return modifier;
	}

	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	@Column(name = "MODIFY_TIME")
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
