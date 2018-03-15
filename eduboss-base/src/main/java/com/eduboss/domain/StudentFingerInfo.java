package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * StudentFingerInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENT_FINGER_INFO")
public class StudentFingerInfo implements java.io.Serializable {

	// Fields

	private String studentFingerNo;
	private String studentId;
	private String fingerInfo;

	// Constructors

	/** default constructor */
	public StudentFingerInfo() {
	}

	/** full constructor */
	public StudentFingerInfo(String studentFingerNo, String studentId, String fingerInfo) {
		this.studentFingerNo = studentFingerNo;
		this.studentId = studentId;
		this.fingerInfo = fingerInfo;
	}

	// Property accessors
	@Id
	@Column(name = "STUDENT_FINGER_NO", unique = true, nullable = false, length = 50)
	public String getStudentFingerNo() {
		return this.studentFingerNo;
	}

	public void setStudentFingerNo(String studentFingerNo) {
		this.studentFingerNo = studentFingerNo;
	}

	@Column(name = "STUDENT_ID", nullable = false, length = 32)
	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "FINGER_INFO", nullable = false, length = 65535)
	public String getFingerInfo() {
		return this.fingerInfo;
	}

	public void setFingerInfo(String fingerInfo) {
		this.fingerInfo = fingerInfo;
	}

}