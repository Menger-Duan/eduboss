package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CustomerEventType;
import com.eduboss.common.StudentEventType;

/**
 * student_dynamic_status entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "student_dynamic_status")
public class StudentDynamicStatus implements java.io.Serializable {

	// Fields
	private String id;
	private Student student;
	private StudentEventType dynamicStatusType;
	private String occourTime;
	private String description;
	private String referUrl;
	private User referuser;

	// Constructors

	/** default constructor */
	public StudentDynamicStatus() {
	}

	/** minimal constructor */
	public StudentDynamicStatus(String id) {
		this.id = id;
	}

	/** full constructor */
	public StudentDynamicStatus(String id, Student student,
			StudentEventType dynamicStatusType, String occourTime, String description,
			String referUrl, User referuser) {
		this.id = id;
		this.student = student;
		this.dynamicStatusType = dynamicStatusType;
		this.occourTime = occourTime;
		this.description = description;
		this.referUrl = referUrl;
		this.referuser = referuser;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	@Enumerated(EnumType.STRING)
	@Column(name = "DYNAMIC_STATUS_TYPE", length = 32)
	public StudentEventType getDynamicStatusType() {
		return this.dynamicStatusType;
	}

	public void setDynamicStatusType(StudentEventType dynamicStatusType) {
		this.dynamicStatusType = dynamicStatusType;
	}

	@Column(name = "OCCOUR_TIME", length = 20)
	public String getOccourTime() {
		return this.occourTime;
	}

	public void setOccourTime(String occourTime) {
		this.occourTime = occourTime;
	}

	@Column(name = "DESCRIPTION", length = 4096)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "REFER_URL", length = 1024)
	public String getReferUrl() {
		return this.referUrl;
	}

	public void setReferUrl(String referUrl) {
		this.referUrl = referUrl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REFERUSER_ID")
	public User getReferuser() {
		return this.referuser;
	}

	public void setReferuser(User referuser) {
		this.referuser = referuser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

}