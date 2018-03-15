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
 * StudentOrganization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENT_ORGANIZATION")
public class StudentOrganization implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private Organization organization;
	private User studyManager;
	private String isMainOrganization;

	// Constructors

	/** default constructor */
	public StudentOrganization() {
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATION_ID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "STUDY_MANAGER_ID")
	public User getStudyManager() {
		return studyManager;
	}

	public void setStudyManager(User studyManager) {
		this.studyManager = studyManager;
	}

	@Column(name = "IS_MAIN_ORGANIZATION", length = 1)
	public String getIsMainOrganization() {
		return this.isMainOrganization;
	}

	public void setIsMainOrganization(String isMainOrganization) {
		this.isMainOrganization = isMainOrganization;
	}

}