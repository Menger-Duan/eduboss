package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 2016-12-15 授课老师科目关联
 * @author lixuejun
 *
 */
@Entity
@Table(name = "teacher_subject_version")
public class TeacherSubjectVersion {

	private int id;
	private TeacherVersion teacherVersion; // 授课老师ID
	private DataDict subject; // 科目
	private DataDict grade; // 年级
	private User teacher; // 关联的用户
	private String versionDate; // 版本日期
	private int versionMonth; // 月份版本
	private Organization blCampus; // 所属校区
	private int isMonthVersion; // 1：月份版本，0：非月份版本
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	public TeacherSubjectVersion() {
		super();
	}

	public TeacherSubjectVersion(int id) {
		super();
		this.id = id;
	}

	public TeacherSubjectVersion(int id, TeacherVersion teacherVersion,
			DataDict subject, DataDict grade, User teacher, String versionDate,
			int versionMonth, Organization blCampus, int isMonthVersion, String createTime,
			String createUserId, String modifyTime, String modifyUserId) {
		super();
		this.id = id;
		this.teacherVersion = teacherVersion;
		this.subject = subject;
		this.grade = grade;
		this.teacher = teacher;
		this.versionDate = versionDate;
		this.versionMonth = versionMonth;
		this.blCampus = blCampus;
		this.isMonthVersion = isMonthVersion;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	public DataDict getGrade() {
		return grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "VERSION_DATE", length = 10)
	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

	@Column(name = "VERSION_MONTH", length = 6)
	public int getVersionMonth() {
		return versionMonth;
	}

	public void setVersionMonth(int versionMonth) {
		this.versionMonth = versionMonth;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Column(name = "IS_MONTH_VERSION", length = 1)
	public int getIsMonthVersion() {
		return isMonthVersion;
	}

	public void setIsMonthVersion(int isMonthVersion) {
		this.isMonthVersion = isMonthVersion;
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
