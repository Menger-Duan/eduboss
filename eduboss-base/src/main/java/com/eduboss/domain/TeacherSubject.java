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
import javax.persistence.Transient;

/**
 * TeacherSubject entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "teacher_subject")
public class TeacherSubject implements java.io.Serializable {

	// Fields

	private int id;
	private String subjectStatus;
	private String createTime;
	private String createUserId;

	private User teacher;
	private DataDict grade;
	private DataDict subject;
	
	//�����ֶ�
	private String updateTime;
    private String updateUserId;

	// Constructors

	/** default constructor */
	public TeacherSubject() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable=false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "SUBJECT_STATUS", length = 32)
	public String getSubjectStatus() {
		return this.subjectStatus;
	}

	public void setSubjectStatus(String subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grade")
	public DataDict getGrade() {
		return grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subjet")
	public DataDict getSubject() {
		return subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@Column(name = "UPDATE_TIME", length = 20)
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "UPDATE_USER_ID", length = 32)
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Transient
	public String getIdString() {
		return this.id + "-" + this.grade + "-" + this.subject;
		// return Json.toJson(this.id);
	}
}