package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.FunctionsLevel;
import com.eduboss.common.TeacherFunctions;

/**
 * 2016-12-15 教师职能关联
 * @author lixuejun
 *
 */
@Entity
@Table(name = "teacher_function_version")
public class TeacherFunctionVersion {

	private int id; 
	private TeacherVersion teacherVersion; // 授课老师版本
	private TeacherFunctions teacherFunctions; // 老师职能
	private FunctionsLevel functionsLevel; // 职能级别
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public TeacherFunctionVersion() {
		super();
	}

	public TeacherFunctionVersion(int id) {
		super();
		this.id = id;
	}

	public TeacherFunctionVersion(int id, TeacherVersion teacherVersion,
			TeacherFunctions teacherFunctions, FunctionsLevel functionsLevel,
			String createTime, String createUserId, String modifyTime,
			String modifyUserId) {
		super();
		this.id = id;
		this.teacherVersion = teacherVersion;
		this.teacherFunctions = teacherFunctions;
		this.functionsLevel = functionsLevel;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_VERSION_ID")
	public TeacherVersion getTeacherVersion() {
		return teacherVersion;
	}

	public void setTeacherVersion(TeacherVersion teacherVersion) {
		this.teacherVersion = teacherVersion;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "TEACHER_FUNCATIONS", length = 32)
	public TeacherFunctions getTeacherFunctions() {
		return teacherFunctions;
	}

	public void setTeacherFunctions(TeacherFunctions teacherFunctions) {
		this.teacherFunctions = teacherFunctions;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "FUNCATIONS_LEVEL", length = 32)
	public FunctionsLevel getFunctionsLevel() {
		return functionsLevel;
	}

	public void setFunctionsLevel(FunctionsLevel functionsLevel) {
		this.functionsLevel = functionsLevel;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
}
